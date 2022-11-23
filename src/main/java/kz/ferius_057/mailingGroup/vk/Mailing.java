package kz.ferius_057.mailingGroup.vk;

import api.longpoll.bots.methods.VkBotsMethods;
import api.longpoll.bots.methods.impl.other.Execute;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import kz.ferius_057.mailingGroup.data.Config;
import kz.ferius_057.mailingGroup.model.basic.ResultResponse;
import kz.ferius_057.mailingGroup.util.AttachmentUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 10.09.2022 | 23:56 ⭐
 */
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Mailing {
    static final Logger LOGGER = LogManager.getLogger(Mailing.class);
    VkBotsMethods vk;
    Config config;


    @NonFinal List<Integer> usersItem;
    static final int COUNT = 200; // max value 200 | кол-во диалогов которые нужно получать каждый запрос
    static final int COUNT_FOR_SEND = 100; // max value 100 | кол-во пользователей в 1 запросе отправки сообщения
    static final JsonArray RESPONSES = new JsonArray();
    static final Gson GSON = new Gson();
    static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();


    @SneakyThrows
    public void run() {
        val uploadPhoto = AttachmentUtil.parseAttachments(vk, config.getAttachments());

        // Проверяет если включен тест рассылки то берет id пользователей из списка 'user'
        // В ином случае получает id всех пользователей которым можно отправить сообщение
        usersItem = config.isTestModeEnable() ? config.getTestModeUsers() : getAvailableItems();
        val usersItemSize = usersItem.size();
        LOGGER.info("Пользователей с разрешенной отправкой сообщения: {}", usersItemSize);

        val usersList = Lists.partition(usersItem, COUNT_FOR_SEND); // Поделить что бы в каждом send запросе было не более COUNT_FOR_SEND
        val usersListSize = usersList.size();
        LOGGER.info("Количество запросов которое будет произведено: {}", usersListSize);

        val countQuery = new AtomicInteger(usersListSize-1);
        val attachments = uploadPhoto.join();
        LOGGER.debug("Вложения в сообщении: {}", attachments);
        usersList.forEach(user -> send(user, countQuery, attachments));

        LOGGER.info("Все {} запросов приступили к работе...", usersListSize);


        SCHEDULED_EXECUTOR_SERVICE.schedule(() -> {
            LOGGER.error("Произошла неизвестная ошибка, скрипт был выключен автоматически. | {}", RESPONSES.size());
            SCHEDULED_EXECUTOR_SERVICE.shutdownNow();
        }, 1, TimeUnit.HOURS); // если произойдет ошибка, то автоматическое выключение через час
    }

    @SneakyThrows
    private List<Integer> getAvailableItems() {
        val firstResponse = vk.messages.getConversations().setCount(COUNT).setExtended(true).execute().getResponse();
        val items = firstResponse.getItems();

        for (int i = 1; items.size() < firstResponse.getCount(); i++) {
            if (i % 3 == 0)
                LOGGER.info("Получение диалогов: {}...", (firstResponse.getCount() - items.size()));
            vk.messages.getConversations()
                    .setCount(COUNT)
                    .setOffset(i * COUNT)
                    .setExtended(true)
                    .executeAsync()
                    .thenAccept(response -> items
                            .addAll(response.getResponse().getItems()) // TODO: 31.10.2022 | проверка на DELETED
                    ).join();
        }

        LOGGER.info("Найдено диалогов: {}", items.size());

        return items.stream()
                .filter(item -> {
                    val conversation = item.getConversation();
                    return conversation.getPeer().getType().equalsIgnoreCase("user") // если это пользователь, а не чат
                            && conversation.getCanWrite().getAllowed(); // если пользователю можно писать в лс
                }).map(item -> item.getConversation().getPeer().getId())
                .collect(Collectors.toList());
    }

    private void send(final List<Integer> users, final AtomicInteger numberQuery, final String attachments) {
        vk.other.execute()
                .setCode(
                        "return API.messages.send({" +
                                "\"random_id\":0," +
                                "\"user_ids\":" + users + "," +
                                "\"attachment\":[\"" + attachments + "\"]," +
                                "\"message\":\"" + config.getMessage() + "\"," +
                                "\"dont_parse_links\":1," + // не создавать сниппет ссылки из сообщения
                                "\"disable_mentions\":1" + // отключить уведомление об упоминании в сообщении
                                "});"
                ).executeAsync()
                .exceptionally(throwable -> {
                    try {
                        // TODO: 05.11.2022 | проверять ошибки
                        LOGGER.error("Не удалось выполнить метод execute: {}", throwable.getCause().getMessage());
                        JsonObject jsonObject = new Gson().fromJson(throwable.getCause().getMessage(), JsonObject.class);
                        System.out.println(jsonObject);
                        System.out.println(jsonObject.get("error").getAsJsonObject().get("error_code").getAsInt());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return new Execute.ResponseBody();
                })
                .thenAccept(response -> {
                    synchronized (RESPONSES) {
                        val responseArray = response.getResponse().getAsJsonArray();
                          RESPONSES.addAll(responseArray);

                        val totalMemory = Runtime.getRuntime().totalMemory();
                        val freeMemory = Runtime.getRuntime().freeMemory();

                        LOGGER.info("Осталось {} запросов...", numberQuery.getAndDecrement());

                        LOGGER.debug("RESPONSES: {}  |  this response: {}  |  usersItem: {}" +
                                        "\nmem total: {}  |  free: {}  |  used: {}",
                                RESPONSES.size(), responseArray.size(), usersItem.size(),
                                totalMemory / 1048576, freeMemory / 1048576,
                                totalMemory - freeMemory);

                        isLastQuery(numberQuery.get()); // если последний запрос то завершение
                    }
                });
    }

    private void isLastQuery(final int numberQuery) {
        if (numberQuery < 0) {
            LOGGER.info("\n------------------------------------------------------------------------------");
            val response = GSON.fromJson(RESPONSES, ResultResponse.class);

            LOGGER.debug("Все ответы запросов: {}", RESPONSES);
            LOGGER.info("Успешно отправлено: {} из {}", response.getCountSuccessful(), usersItem.size());
            response.getErrors()
                    .forEach(errorResponse ->
                            LOGGER.warn("Ошибка №{}: {} не отправлено   |   {}",
                                    errorResponse.getCode(),
                                    errorResponse.getCountErrors(),
                                    errorResponse.getDescription()));

            SCHEDULED_EXECUTOR_SERVICE.shutdownNow();
        }
    }
}
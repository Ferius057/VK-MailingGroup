package kz.ferius_057.mailingGroup.vk;

import api.longpoll.bots.methods.VkBotsMethods;
import api.longpoll.bots.methods.impl.other.Execute;
import com.google.common.collect.Lists;
import com.google.gson.*;
import kz.ferius_057.mailingGroup.data.Config;
import kz.ferius_057.mailingGroup.model.basic.Response;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 10.09.2022 | 23:56 ⭐
 */
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Mailing {
    static final Logger LOGGER = LogManager.getLogger();
    VkBotsMethods vk;
    Config config;


    static List<Integer> usersItem;
    static final int COUNT = 200; // max value 200 | кол-во диалогов которые нужно получать каждый запрос
    static final int COUNT_FOR_SEND = 4; // max value 100 | кол-во пользователей в 1 запросе отправки сообщения
    static final JsonArray RESPONSES = new JsonArray();
    static final Gson GSON = new Gson();
    static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();


    @SneakyThrows
    public void run() {
        usersItem = getAvailableItems(); // Получает id всех пользователей которым можно отправить сообщение
        val usersItemSize = usersItem.size();
        LOGGER.info("Пользователей с разрешенной отправкой сообщения: {}", usersItemSize);

        val usersList = Lists.partition(usersItem, COUNT_FOR_SEND); // Поделить что бы в каждом send запросе было не более COUNT_FOR_SEND
        val usersListSize = usersList.size();
        LOGGER.info("Количество запросов которое будет произведено: {}", usersListSize);

        val countQuery = new AtomicInteger(usersListSize-1);
        usersList.forEach(users -> send(users, countQuery));

        LOGGER.info("Все {} запросов приступили к работе...", usersListSize);


        end(() -> {
            LOGGER.info("\n------------------------------------------------------------------------------");
            Response response = GSON.fromJson(RESPONSES, Response.class);

            LOGGER.info("Успешно отправлено: {} из {}", response.getCountSuccessful(), usersItemSize);
            response.getErrors()
                    .forEach(errorResponse ->
                            LOGGER.warn("Ошибка №{}: {}   |   {}",
                            errorResponse.getCode(),
                            errorResponse.getCountErrors(),
                            errorResponse.getDescription()));
        }, usersItemSize);
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

    private void send(final List<Integer> users, final AtomicInteger countQuery) {
        vk.other.execute()
                .setCode(
                        "return API.messages.send({" +
                                "\"random_id\":0," +
                                "\"user_ids\":" + users + "," +
                                //      "\"attachment\":\"article-206828643_72442_5c164e8ddf296f3f11\"," +
                                "\"message\":\"" + config.getMessage() + "\"" +
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
                        RESPONSES.addAll(response.getResponse().getAsJsonArray());
                    }
                    LOGGER.info("Осталось {} запросов...", countQuery.getAndDecrement());
                });
    }

    @SneakyThrows
    private void end(Runnable runnable, int countUsers) {
        SCHEDULED_EXECUTOR_SERVICE.schedule(() -> {
            LOGGER.error("Произошла неизвестная ошибка, скрипт был выключен автоматически. | {} | {}", RESPONSES.size(), countUsers);
            SCHEDULED_EXECUTOR_SERVICE.shutdown();
        }, 1, TimeUnit.HOURS); // вдруг что, и что бы через час выключилось

        // каждые 200 ms проверяет кол-во отправленных и всего пользователей, если совпадает значит всем отправилось и стоп
        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            if (RESPONSES.size() == countUsers) {
                SCHEDULED_EXECUTOR_SERVICE.shutdownNow();
                runnable.run();
            }
        }, 0, 200, TimeUnit.MILLISECONDS);
    }
}
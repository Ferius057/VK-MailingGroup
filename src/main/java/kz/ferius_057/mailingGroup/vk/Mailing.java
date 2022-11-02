package kz.ferius_057.mailingGroup.vk;

import api.longpoll.bots.methods.VkBotsMethods;
import api.longpoll.bots.methods.impl.other.Execute;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import kz.ferius_057.mailingGroup.data.Config;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 10.09.2022 | 23:56 ⭐
 */
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Mailing {
    VkBotsMethods vk;
    Config config;
    boolean running;


    static final Logger LOGGER = LogManager.getLogger();

    static final int COUNT = 200; // max value 200
    static final int COUNT_FOR_SEND = 1; // max value 100

    static final List<JsonElement> RESPONSES = new ArrayList<>();
    static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(1);
    ///final ScheduledFuture<Boolean> schedule = autoStop();


    @SneakyThrows
    public void run() {
        val items = getAvailableItems(); // Получает id всех пользователей которым можно отправить сообщение
        LOGGER.info("Пользователей с разрешенной отправкой сообщения: {}", items.size());

        val usersList = Lists.partition(items, COUNT_FOR_SEND); // Поделить что бы в каждом send запросе было не более COUNT_FOR_SEND
        LOGGER.info("usersList size: {}", usersList.size());


        AtomicInteger countQuery = new AtomicInteger(usersList.size());
        usersList.forEach(users -> send(users, countQuery));


        end(() -> {
            System.out.println(RESPONSES.size());
            System.out.println("done.");
        }, usersList.size());
    }

    @SneakyThrows
    private List<Integer> getAvailableItems() {
        val firstResponse = vk.messages.getConversations().setCount(COUNT).setExtended(true).execute().getResponse();
        val items = firstResponse.getItems();

        for (int i = 1; items.size() < firstResponse.getCount(); i++) {
            if (i % 5 == 0)
                LOGGER.info("Получение диалогов: {}...", (firstResponse.getCount() - items.size()));
            vk.messages.getConversations()
                    .setCount(COUNT)
                    .setOffset(i * COUNT)
                    .setExtended(true)
                    .executeAsync()
                    .thenAccept(response -> {
                               /* firstResponse.getProfiles()
                                        .addAll(response
                                                .getResponse()
                                                .getProfiles());*/ // TODO: 31.10.2022 | проверка на DELETED
                                items.addAll(response
                                        .getResponse()
                                        .getItems());
                            }
                    ).join();
        }


        LOGGER.info("Найдено диалогов: {}", items.size());

        return items.stream()
                .filter(item -> {
                    val conversation = item.getConversation();
                    return conversation.getPeer().getType().equalsIgnoreCase("user")
                            && conversation.getCanWrite().getAllowed();
                }).map(item -> item.getConversation().getPeer().getId())
                .collect(Collectors.toList());
        //   return List.of(615081290, 621876211, 519177901, 519087143);
    }

    private void send(final List<Integer> users, final AtomicInteger countQuery) {
        String code =
                "return API.messages.send({" +
                        "\"random_id\":0," +
                        "\"user_ids\":" + users + "," +
                        //       "\"attachment\":\"article-206828643_72442_5c164e8ddf296f3f11\"," +

                        "\"message\":\"" + config.getMessage() + "\"" +
                        "});";


        vk.other.execute()
                .setCode(code)
                .executeAsync()
                .exceptionally(throwable -> {
                    try {
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
                        RESPONSES.add(response.getResponse());
                    }
                    LOGGER.info("Осталось {} запросов... - {} - {}", countQuery.getAndDecrement(), response.getResponse(), RESPONSES.size());
                });
    }

    @SneakyThrows
    private void end(Runnable runnable, int countResponses) {
        SCHEDULED_EXECUTOR_SERVICE.schedule(() -> {
            running = false;
            SCHEDULED_EXECUTOR_SERVICE.shutdown();
        }, 1, TimeUnit.HOURS); // вдруг что, и что бы через час выключилось

        while (running) {
            if (RESPONSES.size() == countResponses) {
                SCHEDULED_EXECUTOR_SERVICE.shutdownNow();
                running = false;
                runnable.run();
            }
            Thread.sleep(200);
        }
    }
}
package kz.ferius_057.mailingGroup;

import kz.ferius_057.mailingGroup.data.Config;
import kz.ferius_057.mailingGroup.longpoll.LongPollListener;
import kz.ferius_057.mailingGroup.util.Update;
import kz.ferius_057.mailingGroup.vk.Mailing;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 10.09.2022 | 2:57 ⭐
 */
public class Main {
    static final Logger LOGGER = LogManager.getLogger(Main.class);
    static final String CURRENT_VERSION = getCurrentVersion();

    static {
        /* for logging */
        System.setErr(IoBuilder.forLogger().setLevel(Level.ERROR).buildPrintStream());
        System.setOut(IoBuilder.forLogger().setLevel(Level.INFO).buildPrintStream());
    }

    public static void main(String[] args) throws InterruptedException {
        showMenu();
        pingVKApi();

        Update.check(CURRENT_VERSION);

        val config = Config.load(Paths.get("config.yml"));
        if (Optional.ofNullable(config).isPresent()) {
            LOGGER.info("\n[✸] Проверьте данные с конфига:"
                    + "\n[✸] Тестовая рассылка: " +
                    (config.isTestModeEnable()
                            ? "Да"
                            + "\n[✸] Кол-во пользователей в тестовой рассылке: " + config.getTestModeUsers().size()
                            : "Нет")
                    + "\n[✸] Кол-во вложений: " + config.getAttachments().values().stream().mapToLong(List::size).sum()
                    + "\n[✸] Текст сообщений:\n" + config.getMessage().replace("<br>", "\n")
            );

            LOGGER.info("Нажмите [ENTER] что бы начать рассылку..." +
                    "\nКоманда [stop] остановит скрипт.");
            val nextLine = new Scanner(System.in).nextLine().toLowerCase();
            // заметил что в птеродактиле проблематично остановить скрипт когда он сам завершается
            // пока это единственное решение что я придумал
            if (nextLine.contains("stop")) {
                LOGGER.info("Скрипт был остановлен, завершите процесс удобным для себя способом.");
                Thread.sleep(Long.MAX_VALUE);
            }

            LOGGER.info("Начало рассылки...");
            new Mailing(
                    LongPollListener.create(config.getToken()),
                    config
            ).run();
        }
    }

    private static String getCurrentVersion() {
        val properties = new Properties();
        try (val resourceAsStream = Main.class.getResourceAsStream("/version.properties")) {
            properties.load(resourceAsStream);
            return properties.getProperty("version");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showMenu() {
        LOGGER.info(
                "\n╓———————————————————————————————————————————————————————————"
                        + "\n╟———→ MailingGroup v{}   by Ferius_057 aka Charles_Grozny"
                        + "\n╟———→ Разработчик: vk.com/ferius_057"
                        + "\n╟———→ Исходный код: github.com/Ferius057/VK-MailingGroup"
                        + "\n║"
                        + "\n╟———→ Running on {} {}"
                        + "\n╟———→ Java version: {} {}"
                        + "\n╙———————————————————————————————————————————————————————————"
                , CURRENT_VERSION
                , System.getProperty("os.name"), System.getProperty("os.arch")
                , System.getProperty("java.vm.vendor"), System.getProperty("java.vm.version")
        );
    }

    @SneakyThrows
    private static void pingVKApi() {
        CompletableFuture.runAsync(() -> {
            try {
                @Cleanup val socket = new Socket();
                val endpoint = new InetSocketAddress("api.vk.com", 80);
                val start = System.currentTimeMillis();

                socket.connect(endpoint, 0);

                LOGGER.debug("Пинг до api.vk.com - {} = {}ms", endpoint.getAddress(), System.currentTimeMillis() - start);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
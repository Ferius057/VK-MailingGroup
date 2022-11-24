package kz.ferius_057.mailingGroup;

import kz.ferius_057.mailingGroup.data.Config;
import kz.ferius_057.mailingGroup.longpoll.LongPollListener;
import kz.ferius_057.mailingGroup.util.Update;
import kz.ferius_057.mailingGroup.vk.Mailing;
import lombok.val;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 10.09.2022 | 2:57 ⭐
 */
public class Main {
    static final Logger LOGGER = LogManager.getLogger(Config.class);
    static final String CURRENT_VERSION = getCurrentVersion();

    static {
        /* for logging */
        System.setErr(IoBuilder.forLogger().setLevel(Level.ERROR).buildPrintStream());
        System.setOut(IoBuilder.forLogger().setLevel(Level.INFO).buildPrintStream());
    }

    public static void main(String[] args) {
        showMenu();

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

            LOGGER.info("Нажмите [ENTER] что бы начать рассылку...");
            new Scanner(System.in).nextLine();

            LOGGER.info("Начало рассылки...");
            new Mailing(
                    LongPollListener.create(config),
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
}
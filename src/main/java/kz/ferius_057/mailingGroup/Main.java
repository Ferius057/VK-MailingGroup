package kz.ferius_057.mailingGroup;

import kz.ferius_057.mailingGroup.data.Config;
import kz.ferius_057.mailingGroup.longpoll.LongPollListener;
import kz.ferius_057.mailingGroup.vk.Mailing;
import lombok.val;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;

import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 10.09.2022 | 2:57 ⭐
 */
public class Main {
    static final Logger LOGGER = LogManager.getLogger(Config.class);

    static {
        /* for logging */
        System.setErr(IoBuilder.forLogger().setLevel(Level.ERROR).buildPrintStream());
        System.setOut(IoBuilder.forLogger().setLevel(Level.INFO).buildPrintStream());
    }

    public static void main(String[] args) {
        val config = Config.load(Paths.get("config.yml"));
        if (Optional.ofNullable(config).isPresent()) {
            LOGGER.info("\n[*] Проверьте данные с конфига:"
                    + "\n[*] Тестовая рассылка: " +
                    (config.isTestModeEnable()
                            ? "Да"
                            + "\n[*] Кол-во пользователей в тестовой рассылке: " + config.getTestModeUsers().size()
                            : "Нет")
                    + "\n[*] Кол-во вложений: " + config.getAttachments().values().stream().mapToLong(List::size).sum()
                    + "\n[*] Текст сообщений:\n" + config.getMessage().replace("<br>", "\n")
            );

            LOGGER.info("Используйте Enter что бы начать рассылку...");
            new Scanner(System.in).nextLine();

            new Mailing(
                    LongPollListener.create(config),
                    config
            ).run();
        }
    }
}
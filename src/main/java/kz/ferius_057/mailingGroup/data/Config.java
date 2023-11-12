package kz.ferius_057.mailingGroup.data;

import kz.charles_grozny.bukkitConfig.configuration.file.YamlConfiguration;
import kz.ferius_057.mailingGroup.model.media.AttachmentType;
import kz.ferius_057.mailingGroup.util.FileUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 10.09.2022 | 13:21 ⭐
 */
@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Config {
    static final Logger LOGGER = LogManager.getLogger(Config.class);

    String token;

    String message;

    Map<AttachmentType, List<String>> attachments;

    boolean testModeEnable;
    List<Integer> testModeUsers;

    List<Integer> blackListUsers;

    @SuppressWarnings("unchecked")
    public static Config load(final Path path) {
        val config = YamlConfiguration.loadConfiguration(path.toFile());

        if (!Files.exists(path)) {
            FileUtil.saveResource(path.toFile());
            LOGGER.info("Создан файл конфигурации, заполните конфиг и запустите заново.");
            return null;
        } else
            return new Config(
                    config.getString("token"),
                    config.getString("message")
                            .replace("\n", "<br>")
                            .replace("\"", "&#8221;"),
                    config.getConfigurationSection("attachments")
                            .getValues(true)
                            .entrySet().stream()
                            .collect(Collectors.toMap(
                                    e -> AttachmentType.valueOf(e.getKey().toUpperCase()),
                                    e -> { // максимум вложения в 1 сообщении 10
                                        LOGGER.debug("Вложения полученные из конфига: {} - {}", e.getKey(), e.getValue());

                                        ArrayList<String> attachments = new ArrayList<>((Collection<String>) e.getValue());
                                        return attachments.subList(0, Math.min(attachments.size(), 10));
                                    }
                            )),
                    config.getBoolean("testMode.enable"),
                    config.getIntegerList("testMode.users"),
                    config.getIntegerList("blackList")
            );
    }
}
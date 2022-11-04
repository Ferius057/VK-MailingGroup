package kz.ferius_057.mailingGroup.data;

import kz.charles_grozny.bukkitConfig.configuration.file.YamlConfiguration;
import kz.ferius_057.mailingGroup.util.FileUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.val;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 10.09.2022 | 13:21 ⭐
 */
@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Config {

    String token;

    String message;

    public static Config load(final Path path) {
        val config = YamlConfiguration.loadConfiguration(path.toFile());

        if (!Files.exists(path)) {
            FileUtils.saveResource(path.toFile());
            System.out.println("Создан файл конфигурации, заполните конфиг и запустите заново.");
        }

        return new Config(
                config.getString("token"),
                config.getStringList("message")
                        .stream().map(String::valueOf)
                        .collect(Collectors.joining("<br>"))
        );
    }
}
package kz.ferius_057.mailingGroup.util;

import com.google.gson.Gson;
import kz.ferius_057.mailingGroup.model.basic.UpdateResponse;
import kz.ferius_057.mailingGroup.vk.Mailing;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.net.URI;
import java.util.Optional;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Update {
    static final Logger LOGGER = LogManager.getLogger(Mailing.class);
    static final URI URI_Releases = URI.create("https://api.github.com/repos/Ferius057/VK-MailingGroup/releases");

    @SneakyThrows
    public void check(final String currentVersion) {
        LOGGER.info("Проверка обновления...");

        try (val httpClient = HttpClients.createDefault()) {
            try (val response = httpClient.execute(
                    RequestBuilder.get()
                            .setUri(URI_Releases)
                            .build()
            )) {
                val entity = response.getEntity();
                if (Optional.ofNullable(entity).isPresent()) {
                    // TODO: 20.11.2022 | изменить после релиза
                    val entityString = EntityUtils.toString(entity);
                    UpdateResponse lastRelease;
                    if (!entityString.contains("Not Found")) {
                        lastRelease = new Gson().fromJson(
                                entityString,
                                UpdateResponse.class);
                    } else {
                        lastRelease = UpdateResponse.builder()
                                .tagName(currentVersion)
                                .body(currentVersion)
                                .build();
                    }

                    if (isNewVersion(currentVersion, lastRelease.getTagName())) {
                        LOGGER.info("Доступно обновление!" +
                                "\nНовая версия: {}" +
                                "\nТекущая версия: {}" +
                                "\nСведения об обновлении: \n{}",
                                lastRelease.getTagName(),
                                currentVersion,
                                lastRelease.getBody());

                    } else LOGGER.warn("Обновления не найдены.");
                } else LOGGER.warn("Не удалось проверить версию | {}", response);
            }
        }
    }

    private boolean isNewVersion(final String currentVersion, final String lastReleaseVersion) {
        return new DefaultArtifactVersion(currentVersion)
                .compareTo(new DefaultArtifactVersion(lastReleaseVersion)) < 0;
    }
}
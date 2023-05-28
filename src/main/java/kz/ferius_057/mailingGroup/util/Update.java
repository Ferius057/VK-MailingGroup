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

import java.io.PrintWriter;
import java.io.StringWriter;
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
                    val lastRelease = new Gson().fromJson(
                            EntityUtils.toString(entity),
                            UpdateResponse.class);

                    if (isNewVersion(currentVersion, lastRelease.getTagName())) {
                        LOGGER.info("\n---------------------------------------------------" +
                                        "\nДоступно обновление!" +
                                        "\nТекущая версия: {}" +
                                        "\nНовая версия: {}" +
                                        "\nСкачать можно тут: https://github.com/Ferius057/VK-MailingGroup/releases/{}" +
                                        "\nСведения об обновлении: \n{}" +
                                        "\n---------------------------------------------------",
                                currentVersion,
                                lastRelease.getTagName(),
                                lastRelease.getTagName(),
                                lastRelease.getBody());

                    } else LOGGER.info("Обновления не найдены.");
                } else LOGGER.warn("Не удалось проверить версию | {}", response);
            }
        } catch (Exception e) {
            val stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            LOGGER.debug(stringWriter.toString());
            LOGGER.error("Не удалось проверить обновление | Подробная информация в логах | {}", e.getMessage());
        }
    }

    private boolean isNewVersion(final String currentVersion, final String lastReleaseVersion) {
        return new DefaultArtifactVersion(currentVersion)
                .compareTo(new DefaultArtifactVersion(lastReleaseVersion)) < 0;
    }
}
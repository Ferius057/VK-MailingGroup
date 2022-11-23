package kz.ferius_057.mailingGroup.util;

import api.longpoll.bots.exceptions.VkApiException;
import api.longpoll.bots.methods.VkBotsMethods;
import api.longpoll.bots.methods.impl.upload.UploadPhoto;
import kz.ferius_057.mailingGroup.model.media.AttachmentType;
import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 06.11.2022 | 21:45 ⭐
 */
@UtilityClass
public class AttachmentUtil {
    static final Logger LOGGER = LogManager.getLogger();

    public CompletableFuture<String> parseAttachments(final VkBotsMethods vk, final Map<AttachmentType, List<String>> attachmentsConfig) {
        return CompletableFuture.supplyAsync(() -> {
            List<String> attachments = new ArrayList<>();

            attachmentsConfig.forEach((key, value) -> {
                try {
                    switch (key) {
                        case PHOTO: attachments.addAll(photo(vk, value)); break;
                        case WALL: attachments.add(wall(value)); break;
                    }
                } catch (VkApiException | IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // Синтаксис VKScript (execute) принимает только в виде массива ["photo*", "wall*"]
            // для этого пришлось использовать String.join()
            // Максимум вложений в 1 сообщений = 10
            return String.join("\", \"", attachments.subList(0, Math.min(attachments.size(), 10)));
        });
    }

    private List<String> photo(final VkBotsMethods vk, final List<String> photosName) throws VkApiException, IOException {
        List<String> photosAttachment = new ArrayList<>();

        val uploadServer = vk.photos.getMessagesUploadServer().execute().getResponse();
        for (val photoName : photosName) {
            if (!photoName.isEmpty()) {
                LOGGER.info("[PHOTO] Загрузка вложения {}...", photoName);
                @Cleanup val inputStream = Files.newInputStream(Paths.get(photoName));
                val uploadPhoto = new UploadPhoto(
                        uploadServer.getUploadUrl(), photoName, inputStream
                ).execute();

                vk.photos.saveMessagesPhoto()
                        .setHash(uploadPhoto.getHash())
                        .setPhoto(uploadPhoto.getPhoto())
                        .setServer(uploadPhoto.getServer())
                        .execute().getResponse()
                        .forEach(response -> photosAttachment.add(
                                String.format("photo%s_%s", response.getOwnerId(), response.getId())
                        ));
            }
        }

        return photosAttachment;
    }

    private String wall(final List<String> walls) {
        val wall = walls.get(0);
        if (!wall.isEmpty()) {
            LOGGER.info("[WALL] Загрузка вложения {}...", wall);
            return wall;
        } else return "";
    }
}
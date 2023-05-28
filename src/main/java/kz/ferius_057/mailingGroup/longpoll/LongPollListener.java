package kz.ferius_057.mailingGroup.longpoll;

import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.methods.VkBotsMethods;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 22.10.2022 | 21:10 ⭐
 */
@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LongPollListener extends LongPollBot {
    String accessToken;

    public static VkBotsMethods create(final String token) {
        return new LongPollListener(token).vk;
    }
}
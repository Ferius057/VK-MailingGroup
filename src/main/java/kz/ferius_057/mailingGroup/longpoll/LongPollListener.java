package kz.ferius_057.mailingGroup.longpoll;

import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.methods.VkBotsMethods;
import kz.ferius_057.mailingGroup.data.Config;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 22.10.2022 | 21:10 ⭐
 */
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LongPollListener extends LongPollBot {
    Config config;


    public static VkBotsMethods create(final Config config) {
        return new LongPollListener(config).vk;
    }

    @Override
    public String getAccessToken() {
        return config.getToken();
    }
}
package kz.ferius_057.mailingGroup.longpoll;

import api.longpoll.bots.LongPollBot;
import kz.ferius_057.mailingGroup.data.Config;
import kz.ferius_057.mailingGroup.vk.Mailing;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 22.10.2022 | 21:10 ⭐
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LongPollListener extends LongPollBot {
    Config config;

    public LongPollListener(Config config) {
        this.config = config;

        new Mailing(vk, config).run();
    }

    @Override
    public String getAccessToken() {
        return config.getToken();
    }
}
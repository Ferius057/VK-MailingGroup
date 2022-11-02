package kz.ferius_057.mailingGroup;

import kz.ferius_057.mailingGroup.data.Config;
import kz.ferius_057.mailingGroup.longpoll.LongPollListener;
import lombok.val;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.io.IoBuilder;

import java.nio.file.Paths;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 10.09.2022 | 2:57 ⭐
 */
public class Main {
    static {
        /* for logging */
        System.setErr(IoBuilder.forLogger().setLevel(Level.ERROR).buildPrintStream());
        System.setOut(IoBuilder.forLogger().setLevel(Level.INFO).buildPrintStream());
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("норм текст отображается?");
        System.out.println("stop esle text ne norm 5sec");

        //Thread.sleep(6000);

        val config = Config.load(Paths.get("config.yml"));
        if (config.getToken().isEmpty()) {
            System.err.println("Установите токен.");
            return;
        }

        new LongPollListener(config);
    }
}
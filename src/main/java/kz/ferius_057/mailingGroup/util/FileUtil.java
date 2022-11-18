package kz.ferius_057.mailingGroup.util;

import kz.ferius_057.mailingGroup.Main;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 10.09.2022 | 13:59 ⭐
 */
@UtilityClass
public class FileUtil {

    @SneakyThrows
    // TODO: 15.11.2022 | переписать
    public void saveResource(File file) {
        @Cleanup val in = Main.class.getResourceAsStream("/" + file.getName());

        val result = new StringBuilder();
        try (val reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in)))) {
            int i;
            while ((i = reader.read()) != -1) {

                result.append((char) i);
            }
        }

        try (val out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8))) {
            out.write(result.toString());
        }
    }
}
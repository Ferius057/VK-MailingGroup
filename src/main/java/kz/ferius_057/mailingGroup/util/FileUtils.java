package kz.ferius_057.mailingGroup.util;

import kz.ferius_057.mailingGroup.Main;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 10.09.2022 | 13:59 ⭐
 */
@UtilityClass
public class FileUtils {

    @SneakyThrows
    public void saveResource(File file) {
        InputStream in = Main.class.getResourceAsStream("/" + file.getName());

        System.out.println(in);

        StringBuilder result = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in)))) {
            int i;
            while((i=reader.read())!=-1){

                result.append((char) i);
            }
        }

        try (Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8))) {
            out.write(result.toString());
        }
    }
}
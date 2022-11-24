package kz.ferius_057.mailingGroup.util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 10.09.2022 | 13:59 ⭐
 */
@UtilityClass
public class FileUtil {

    @SneakyThrows
    public void saveResource(File file) {
        Files.asByteSink(file)
                .write(Resources.toString(
                        Resources.getResource(file.getName()),
                        Charsets.UTF_8
                ).getBytes());
    }
}
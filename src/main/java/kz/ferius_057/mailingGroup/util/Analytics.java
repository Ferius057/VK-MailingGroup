package kz.ferius_057.mailingGroup.util;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class Analytics {

    private static final String ENDPOINT = "https://www.google-analytics.com/mp/collect" +
            "?measurement_id=G-GQ97TZZF6X&api_secret=xww1oEE-TZGpCIoWodWHqg";

    private static final Path CLIENT_ID_FILE = Paths.get(".mailinggroup_id");
    private static final ZoneOffset ZONE = ZoneOffset.ofHours(5);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static void launch(final String version, final boolean testMode, final long pingMs) {
        CompletableFuture.runAsync(() -> {
            try {
                val clientId = getOrCreateClientId();
                val nowMicros = TimeUnit.MILLISECONDS.toMicros(System.currentTimeMillis());
                val dateTime = ZonedDateTime.now(ZONE).format(FORMATTER);
                val body = "{" +
                        "\"client_id\":\"" + clientId + "\"," +
                        "\"user_id\":\"" + clientId + "\"," +
                        "\"timestamp_micros\":" + nowMicros + "," +
                        "\"events\":[{" +
                        "\"name\":\"launch\"," +
                        "\"params\":{" +
                        "\"uuid\":\"" + clientId + "\"," +
                        "\"version\":\"" + version + "\"," +
                        "\"test_mode\":" + testMode + "," +
                        "\"ping_vkapi_ms\":" + pingMs + "," +
                        "\"date_time\":\"" + dateTime + "\"," +
                        "\"engagement_time_msec\":1" +
                        "}" +
                        "}]" +
                        "}";

                try (val client = HttpClients.createDefault()) {
                    val request = new HttpPost(ENDPOINT);
                    request.setEntity(new StringEntity(body, "UTF-8"));
                    request.setHeader("Content-Type", "application/json");
                    client.execute(request).close();
                }
            } catch (Exception ignored) {
            }
        });
    }

    public static void mailingResult(final String version, final long pingMs, final int successCount, final int totalCount, final long durationMs, final String durationFormatted) {
        try {
            val clientId = getOrCreateClientId();
            val nowMicros = TimeUnit.MILLISECONDS.toMicros(System.currentTimeMillis());
            val dateTime = ZonedDateTime.now(ZONE).format(FORMATTER);
            val body = "{" +
                    "\"client_id\":\"" + clientId + "\"," +
                    "\"user_id\":\"" + clientId + "\"," +
                    "\"timestamp_micros\":" + nowMicros + "," +
                    "\"events\":[{" +
                    "\"name\":\"mailing_result\"," +
                    "\"params\":{" +
                    "\"uuid\":\"" + clientId + "\"," +
                    "\"version\":\"" + version + "\"," +
                    "\"ping_vkapi_ms\":" + pingMs + "," +
                    "\"success_count\":" + successCount + "," +
                    "\"total_count\":" + totalCount + "," +
                    "\"duration_ms\":" + durationMs + "," +
                    "\"duration\":\"" + durationFormatted + "\"," +
                    "\"date_time\":\"" + dateTime + "\"," +
                    "\"engagement_time_msec\":1" +
                    "}" +
                    "}]" +
                    "}";


            try (val client = HttpClients.createDefault()) {
                val request = new HttpPost(ENDPOINT);
                request.setEntity(new StringEntity(body, "UTF-8"));
                request.setHeader("Content-Type", "application/json");
                client.execute(request).close();
            }
        } catch (Exception ignored) {
        }
    }

    private static String getOrCreateClientId() throws IOException {
        if (Files.exists(CLIENT_ID_FILE)) {
            return new String(Files.readAllBytes(CLIENT_ID_FILE)).trim();
        }
        val id = UUID.randomUUID().toString();
        Files.write(CLIENT_ID_FILE, id.getBytes());
        return id;
    }
}

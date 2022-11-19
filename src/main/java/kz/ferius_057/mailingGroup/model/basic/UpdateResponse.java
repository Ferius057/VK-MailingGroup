package kz.ferius_057.mailingGroup.model.basic;

import com.google.gson.annotations.JsonAdapter;
import kz.ferius_057.mailingGroup.adapters.deserializers.UpdateDeserializer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 19.11.2022 | 1:24 ⭐
 */
@Getter
@Builder
@JsonAdapter(UpdateDeserializer.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateResponse {
    String tagName, body;
}
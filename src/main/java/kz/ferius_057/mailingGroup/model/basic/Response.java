package kz.ferius_057.mailingGroup.model.basic;

import com.google.gson.annotations.JsonAdapter;
import kz.ferius_057.mailingGroup.adapters.deserializers.ResponseDeserializer;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 04.11.2022 | 1:53 ⭐
 */
@Getter
@Builder
@JsonAdapter(ResponseDeserializer.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Response {
    int countSuccessful;

    List<ErrorResponse> errors;
}
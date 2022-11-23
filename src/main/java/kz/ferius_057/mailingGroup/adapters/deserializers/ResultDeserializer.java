package kz.ferius_057.mailingGroup.adapters.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import kz.ferius_057.mailingGroup.model.basic.ErrorResponse;
import kz.ferius_057.mailingGroup.model.basic.ResultResponse;
import lombok.val;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Ferius_057 (Charles_Grozny)
 */
public class ResultDeserializer implements JsonDeserializer<ResultResponse> {

    @Override
    public ResultResponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        val responses = jsonElement.getAsJsonArray();

        val errorsJsonObject = responses.asList().stream().map(JsonElement::getAsJsonObject)
                .filter(jsonObject -> Optional.ofNullable(jsonObject.get("error")).isPresent())
                .map(jsonObject -> jsonObject.get("error").getAsJsonObject()).collect(Collectors.toList());

        return ResultResponse.builder()
                .countSuccessful(responses.size() - errorsJsonObject.size())
                .errors(errorsJsonObject.stream()
                        .distinct()
                        .map(jsonObject -> ErrorResponse.builder()
                                .code(jsonObject.get("code").getAsInt())
                                .description(jsonObject.get("description").getAsString())
                                .countErrors(Collections.frequency(errorsJsonObject, jsonObject))
                                .build()).collect(Collectors.toList()))
                .build();
    }
}
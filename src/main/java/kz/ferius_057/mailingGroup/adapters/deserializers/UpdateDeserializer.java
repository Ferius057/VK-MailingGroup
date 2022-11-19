package kz.ferius_057.mailingGroup.adapters.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import kz.ferius_057.mailingGroup.model.basic.UpdateResponse;
import lombok.val;

import java.lang.reflect.Type;

/**
 * @author Ferius_057 (Charles_Grozny)
 */
public class UpdateDeserializer implements JsonDeserializer<UpdateResponse> {

    @Override
    public UpdateResponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        val responses = jsonElement.getAsJsonArray().get(0).getAsJsonObject();

        return UpdateResponse.builder()
                .tagName(responses.get("tag_name").getAsString())
                .body(responses.get("body").getAsString())
                .build();
    }
}
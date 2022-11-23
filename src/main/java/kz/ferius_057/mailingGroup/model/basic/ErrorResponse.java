package kz.ferius_057.mailingGroup.model.basic;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 04.11.2022 | 1:45 ⭐
 */
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    int countErrors, code;
    String description;
}
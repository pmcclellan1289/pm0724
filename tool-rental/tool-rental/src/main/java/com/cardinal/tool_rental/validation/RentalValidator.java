package com.cardinal.tool_rental.validation;

import com.cardinal.tool_rental.domain.RentalCheckout;
import com.cardinal.tool_rental.enumeration.ToolInfoEnum;
import jakarta.validation.*;
import lombok.val;
import org.apache.commons.lang3.EnumUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

public class RentalValidator {

    public static void validateRentalRequest(RentalCheckout rq) throws ValidationException {
        Validator validator;
        try (ValidatorFactory factory = buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        Set<ConstraintViolation<RentalCheckout>> violations = validator.validate(rq);
        List<String> manualValidations = manualValidations(rq);
        List<String> messages = new ArrayList<>();
        if (!violations.isEmpty() || !manualValidations.isEmpty()) {
            messages.add("Rental Request Errors");
            for (val v : violations) {
                messages.add(v.getMessage());
            }
            messages.addAll(manualValidations);
            throw new ValidationException(String.valueOf(messages));
        }
    }

    private static List<String> manualValidations(RentalCheckout rq) {
        List<String> v = new ArrayList<>();

        validateToolCode(rq.getToolCode(), v);

        // future individual validation methods go here

        return v;
    }

    private static void validateToolCode(String toolCode, List<String> v) {
        if (!EnumUtils.isValidEnum(ToolInfoEnum.class, toolCode))
            v.add("Tool code is invalid");
    }
}

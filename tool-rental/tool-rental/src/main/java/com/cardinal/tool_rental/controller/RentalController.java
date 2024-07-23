package com.cardinal.tool_rental.controller;

import com.cardinal.tool_rental.domain.RentalAgreement;
import com.cardinal.tool_rental.domain.RentalCheckout;
import com.cardinal.tool_rental.service.RentalService;
import jakarta.validation.ValidationException;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.cardinal.tool_rental.validation.RentalValidator.validateRentalRequest;

@RestController
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @PostMapping(value = "/rent")
    public RentalAgreement rent(@RequestBody RentalCheckout rq) {
        validateRentalRequest(rq);

        val agreement = rentalService.processRental(rq);
        agreement.print();
        return agreement;
    }


    @ExceptionHandler({ValidationException.class})
    public List<String> handleExceptions(ValidationException validation) {
        List<String> messages = new ArrayList<>();
        messages.add(validation.getMessage());
        return messages;
    }
}
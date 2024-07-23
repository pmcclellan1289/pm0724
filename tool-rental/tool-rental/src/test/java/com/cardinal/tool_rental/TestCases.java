package com.cardinal.tool_rental;


import com.cardinal.tool_rental.controller.RentalController;
import com.cardinal.tool_rental.domain.RentalAgreement;
import com.cardinal.tool_rental.domain.RentalCheckout;
import com.cardinal.tool_rental.service.RentalService;
import jakarta.validation.ValidationException;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestCases {

    @InjectMocks
    RentalController tested;

    @Spy
    RentalService rentalService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTheOneWithTooMuchDiscount() {
        val rq = new RentalCheckout();
        rq.setToolCode("JAKR");
        rq.setCheckoutDate(LocalDate.of(2015, 9, 3));
        rq.setRentalDays(5);
        rq.setDiscount(101);

        Exception e = assertThrows(ValidationException.class, () -> tested.rent(rq));
        assertTrue(e.getMessage().contains("Discount percentage cannot exceed 100"));
    }

    @ParameterizedTest
    @CsvSource({
            "LADW, 2020, 7, 20, 3, 10, Ladder, 5.37",
            "CHNS, 2015, 7, 2, 5, 25, Chainsaw, 3.35", // Thursday, isHolidayCharge, 3 days paid
            "JAKD, 2015, 9, 3, 6, 0, Jackhammer, 8.97", // Thursday, !isHolidayCharge
            "JAKR, 2015, 7, 2, 9, 0, Jackhammer, 17.94", // Thursday, !isHolidayCharge
            "JAKR, 2020, 7, 2, 4, 50, Jackhammer, 1.49", // Thursday, !isHolidayCharge
    })
    void testScenariosFromInstructions(String toolCode, int year, int month, int day, int rentalDays, int discount,
                                       String toolType, double finalCharge) {
        val rq = new RentalCheckout();
        rq.setToolCode(toolCode);
        rq.setCheckoutDate(LocalDate.of(year, month, day));
        rq.setRentalDays(rentalDays);
        rq.setDiscount(discount);

        RentalAgreement result = tested.rent(rq);
        assertEquals(toolType, result.getToolType());
        assertEquals(finalCharge, result.getFinalCharge());
    }
}

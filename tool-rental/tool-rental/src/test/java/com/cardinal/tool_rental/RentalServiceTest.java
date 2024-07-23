package com.cardinal.tool_rental;

import com.cardinal.tool_rental.domain.RentalCheckout;
import com.cardinal.tool_rental.enumeration.ToolInfoEnum;
import com.cardinal.tool_rental.service.RentalService;
import org.apache.commons.lang3.EnumUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RentalServiceTest {

    private static RentalService tested;

    @BeforeEach
    void init () {
        tested = new RentalService();
    }


    @ParameterizedTest
    @CsvSource({
            "3, 2024, 7, 26, JAKR, 1", // no charge on weekends, start on Friday
            "3, 2024, 7, 3, JAKR, 2", // no charge on holiday, start on Wed July 3
            "3, 2024, 7, 3, LADW, 3", // is charge on holiday, start on Wed July 3
            "8, 2024, 8, 26, JAKR, 5", // no charge on holiday or weekend, start on Mon before Labor Day
            "4, 2026, 7, 2, JAKR, 1", // no charge on holiday, start on Thur July 2
            "5, 2027, 7, 2, JAKR, 2", // no charge on holiday, start on Friday July 2

    })
    void testCalculateChargeDays(int rdays, int year, int month, int day, String toolCode, int expected) {
        RentalCheckout rq = new RentalCheckout();
        ToolInfoEnum tool = EnumUtils.getEnum(ToolInfoEnum.class, toolCode);

        rq.setRentalDays(rdays);
        rq.setCheckoutDate(LocalDate.of(year, month, day));

        int chargeDays = ReflectionTestUtils.invokeMethod(tested, "calculateChargeDays", rq, tool);
        assertEquals(expected, chargeDays);
    }
}

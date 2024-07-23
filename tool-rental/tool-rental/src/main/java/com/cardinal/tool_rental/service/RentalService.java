package com.cardinal.tool_rental.service;

import com.cardinal.tool_rental.domain.RentalAgreement;
import com.cardinal.tool_rental.domain.RentalCheckout;
import com.cardinal.tool_rental.enumeration.ToolInfoEnum;
import lombok.val;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

@Service
public class RentalService {

    public RentalAgreement processRental(RentalCheckout rq) {
        // init and set obvious field mappings
        val agreement = RentalAgreement.builder()
                .rentalDays(rq.getRentalDays())
                .checkoutDate(rq.getCheckoutDate())
                .toolCode(rq.getToolCode())
                .discountPercent(rq.getDiscount())
                .build();

        val tool = EnumUtils.getEnum(ToolInfoEnum.class, rq.getToolCode());

        agreement.setToolBrand(tool.getBrand());
        agreement.setToolType(tool.getToolType());
        agreement.setDailyRentalCharge(tool.getDailyCharge());

        int chargeDays = calculateChargeDays(rq, tool);
        agreement.setChargeDays(chargeDays);

        agreement.calculateDiscountAndFinal(tool.getDailyCharge());
        agreement.setDueDate(agreement.getCheckoutDate().plusDays(rq.getRentalDays()));

        return agreement;
    }

    private int calculateChargeDays(RentalCheckout rq, ToolInfoEnum tool) {
        LocalDate toCheck = LocalDate.of(rq.getCheckoutDate().getYear(), rq.getCheckoutDate().getMonth(), rq.getCheckoutDate().getDayOfMonth());

        int chargeDays = rq.getRentalDays();
        for (int i = 0; i < rq.getRentalDays(); i++) {
            //Independence Day, July 4th - If falls on weekend, it is observed on the closest
            // weekday (if Sat, then Friday before, if Sunday, then Monday after)
            // Labor Day - First Monday in September
            if (!tool.isWeekendCharge() && isWeekend(toCheck)) {
                chargeDays--;
            }

            if (!tool.isHolidayCharge() && isHoliday(toCheck)) {
                chargeDays--;
            }

            toCheck = toCheck.plusDays(1L);

        }


        return chargeDays;

    }

    private boolean isWeekend(LocalDate toCheck) {
        return toCheck.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                || toCheck.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }

    private boolean isHoliday(LocalDate toCheck) {
        return isJulyFourth(toCheck) || isLaborDay(toCheck);
    }

    private boolean isJulyFourth(LocalDate toCheck) {
        boolean isJuly = toCheck.getMonth().equals(Month.JULY);

        // Weekday Independence Day
        if (!isWeekend(toCheck) && isJuly && toCheck.getDayOfMonth() == 4)
            return true;

        // Friday before weekend
        if (toCheck.getDayOfWeek().equals(DayOfWeek.FRIDAY) && isJuly && toCheck.getDayOfMonth() == 3)
            return true;

        // Monday after weekend
        return toCheck.getDayOfWeek().equals(DayOfWeek.MONDAY) && isJuly && toCheck.getDayOfMonth() == 5;
    }

    private boolean isLaborDay(LocalDate toCheck) {
        // First Monday in September
        return toCheck.getMonth().equals(Month.SEPTEMBER)
                && toCheck.getDayOfWeek().equals(DayOfWeek.MONDAY)
                && toCheck.getDayOfMonth() <= 7;
    }
}

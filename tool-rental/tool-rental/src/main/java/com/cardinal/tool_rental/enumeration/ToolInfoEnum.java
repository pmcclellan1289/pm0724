package com.cardinal.tool_rental.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ToolInfoEnum {

    // This would be better implemented as a database lookup
    // probably cached and refreshed on a regular basis, but for this
    // limited exercise I feel this enum is sufficient

    JAKR("Jackhammer", "Rigid", 2.99, true, false, false),
    LADW("Ladder", "Werner", 1.99, true, false, true),
    CHNS("Chainsaw", "Stihl", 1.49, true, false, true),
    JAKD("Jackhammer", "DeWalt", 2.99, true, false, false);

    private final String toolType;
    private final String brand;
    private final double dailyCharge;
    private final boolean isWeekdayCharge;
    private final boolean isWeekendCharge;
    private final boolean isHolidayCharge;
}

package com.cardinal.tool_rental.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalCheckout {

    // I generally prefer a more obscure message to slow down ne'er-do-wells
    @Pattern(regexp = "^[A-Z]{4}$", message = "Tool Code must be 4 uppercase letters")
    private String toolCode;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate checkoutDate;

    @Min(value = 1, message = "Rental must be at least one day")
    private int rentalDays;

    @Min(value = 0, message = "Discount percentage cannot be negative")
    @Max(value = 100, message = "Discount percentage cannot exceed 100")
    private int discount;
}

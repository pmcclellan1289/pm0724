package com.cardinal.tool_rental.domain;

import lombok.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalAgreement {

    private String toolCode;
    private String toolType;
    private String toolBrand;
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private double dailyRentalCharge;
    private int chargeDays;
    private double preDiscountCharge;
    private int discountPercent;
    private double discountAmount;
    private double finalCharge;


    public void print() {
        val money = NumberFormat.getCurrencyInstance();
        val dateFormat = DateTimeFormatter.ofPattern("MM/dd/yy");

        System.out.println("Tool Code: " + this.toolCode);
        System.out.println("Tool Type: " + this.toolType);
        System.out.println("Tool Brand: " + this.toolBrand);
        System.out.println("Rental Days: " + this.rentalDays);
        System.out.println("Checkout Date: " + this.checkoutDate.format(dateFormat));
        System.out.println("Due Date: " + this.dueDate.format(dateFormat));
        System.out.println("Daily Rental Charge: " + money.format(this.dailyRentalCharge));
        System.out.println("Charge Days: " + this.chargeDays);
        System.out.println("Pre-Discount Charge: " + money.format(this.preDiscountCharge));
        System.out.println("Discount Percent: " + this.getDiscountPercent() + "%");
        System.out.println("Discount Amount: " + money.format(this.getDiscountAmount()));
        System.out.println("Final Charge: " + money.format(this.finalCharge));
    }

    public void calculateDiscountAndFinal(double dailyCharge) {
        this.setPreDiscountCharge(roundTwoDecimals(dailyCharge * chargeDays));
        this.setDiscountAmount(roundTwoDecimals(this.getPreDiscountCharge() * ((double) this.getDiscountPercent() / 100)));
        this.setFinalCharge(roundTwoDecimals(this.getPreDiscountCharge() - this.getDiscountAmount()));
    }


    private double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.parseDouble(twoDForm.format(d));
    }

}

package com.myprivate.currency_converter;

import java.util.Locale;

public class Currency {

    private final String currencyName;
    private final String currencyShort;
    private final double currencyRate;

    public Currency(String currencyName, String currencyShort, double currencyRate) {

        this.currencyName = currencyName;
        this.currencyShort = currencyShort;
        this.currencyRate = currencyRate;
    }

    public double getCurrencyRate() {
        return currencyRate;
    }

    @Override
    public String toString() {
        String formattedCurrency= String.format(Locale.US,"%.2f",currencyRate);
        return this.currencyShort+"-/"+this.currencyName+"/"+formattedCurrency;
    }
}

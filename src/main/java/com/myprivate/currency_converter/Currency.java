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

        double tempCurrency;
        String formattedCurrency;

        if(this.currencyRate<0.01){
            tempCurrency=this.currencyRate*100;
            formattedCurrency = String.format(Locale.US,"%.2f",tempCurrency);
            return this.currencyShort+"-/"+this.currencyName+"/kurs w złotych: "+ formattedCurrency +"*-cena za 100 sztuk waluty";
        }
        formattedCurrency = String.format(Locale.US,"%.2f",currencyRate);
        return this.currencyShort+"-/"+this.currencyName+"/kurs w złotych: "+ formattedCurrency;
    }
}

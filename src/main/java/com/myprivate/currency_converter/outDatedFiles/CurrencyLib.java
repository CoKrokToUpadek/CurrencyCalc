package com.myprivate.currency_converter.outDatedFiles;

import com.myprivate.currency_converter.Currency;

import java.util.ArrayList;
import java.util.List;

public class CurrencyLib {
    private List<Currency> currencies;

    public CurrencyLib() {
        this.currencies = new ArrayList<>();
        currencies.add(new Currency("złoty", "PLN",1.0));
        currencies.add(new Currency("bat", "THB",0.1292));
        currencies.add(new Currency("euro", "EUR",4.6658));
    }

    public void add(Currency c){
        currencies.add(c);
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public double getElement(int i){
        return currencies.get(i).getCurrencyRate();
    }

}

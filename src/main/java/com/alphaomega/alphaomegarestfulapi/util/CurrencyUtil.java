package com.alphaomega.alphaomegarestfulapi.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CurrencyUtil {

    public static String convertToDisplayCurrency(BigDecimal amount) {
        Locale indonesia = new Locale("id", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(indonesia);

        return numberFormat.format(amount.doubleValue());
    }

    public static String getIndonesiaCurrencyCode() {
        Locale indonesia = new Locale("id", "ID");
        Currency currency = Currency.getInstance(indonesia);

        return currency.getCurrencyCode();
    }

}

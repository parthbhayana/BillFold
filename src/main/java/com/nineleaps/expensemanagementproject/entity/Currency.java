package com.nineleaps.expensemanagementproject.entity;

public enum Currency {

    EUR("€"), USD("$"), JPY("¥"), BGN("лв"), CZK("Kč"), DKK("kr"), GBP("£"), HUF("Ft"), PLN("zł"), RON("lei"), SEK("kr"), CHF("Fr"), ISK("kr"), NOK("kr"), HRK("kn"), RUB("₽"), TRY("₺"), AUD("$"), BRL("R$"), CAD("$"), CNY("¥"), HKD("$"), IDR("Rp"), ILS("₪"), INR("₹"), KRW("₩"), MXN("$"), MYR("RM"), NZD("$"), PHP("₱"), SGD("$"), THB("฿"), ZAR("R");

    private final String symbol;

    Currency(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

}

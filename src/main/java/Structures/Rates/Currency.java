package Structures.Rates;

import java.util.List;

public class Currency {
    String table;
    String currency;
    String code;
    List<Rate> rates;

    public Currency() {
    }

    public String getTable() {
        return table;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCode() {
        return code;
    }

    public List<Rate> getRates() {
        return rates;
    }
}

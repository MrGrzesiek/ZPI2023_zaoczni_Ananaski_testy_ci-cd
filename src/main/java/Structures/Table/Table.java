package Structures.Table;

import java.util.List;

public class Table {
    String table;
    String no;
    String effectiveDate;
    List<Currency> rates;

    public String getTable() {
        return table;
    }

    public String getNo() {
        return no;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public List<Currency> getRates() {
        return rates;
    }
}

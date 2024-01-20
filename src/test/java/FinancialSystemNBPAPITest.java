import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FinancialSystemNBPAPITest {
    @Test
    public void connection_shouldReturnNotEmptyData()
    {
        // Arrange
        String table = "A";
        String currencyCode = "USD";
        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);
        String endDate = "";
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DATE, -7);
        endDate = df.format(c.getTime());


        String address = "/rates/" + table + "/" + currencyCode + "/" + endDate + "/" + todayAsString;

        // Act
        String response = FinancialSystemNBPAPI.connection(address);

        // Assert
        assertNotNull(response);
        assertTrue(response.length() > 0);
    }

    @ParameterizedTest
    @CsvSource({
            ",,,",
            "A,,,",
            "A,USD,,",
            "A,USD,2021-01-01,"
    })
    public void connection_shouldThrowArgumentException(String table, String currencyCode, String endDate, String todayAsString) {
        // Arrange & Act & Assert
        boolean thrown = false;

        try {
            FinancialSystemNBPAPI.connection("/rates/" + table + "/" + currencyCode + "/" + endDate + "/" + todayAsString);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }

        assertTrue(thrown);
    }
}

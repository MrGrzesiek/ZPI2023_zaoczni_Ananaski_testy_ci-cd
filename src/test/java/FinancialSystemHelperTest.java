import Structures.Rates.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class FinancialSystemHelperTest {
    @Test
    public void getData_shouldReturnCurrency()
    {
        // Arrange & Act
        Currency currency = FinancialSystemHelper.getData("one week", "A", "USD");

        // Assert
        assertNotNull(currency);
        assertEquals("USD", currency.getCode());
        assertEquals("dolar amerykański", currency.getCurrency());
        assertEquals("A", currency.getTable());
    }

    @ParameterizedTest
    @ValueSource(strings = {"one week", "two weeks", "month", "quarter"})
    public void getData_RatesShouldGetData(String timeOption)
    {
        // Arrange & Act
        Currency currency = FinancialSystemHelper.getData(timeOption, "A", "USD");

        // Assert
        assertNotNull(currency);
        assertNotNull(currency.getRates());
        assertTrue("Rates should not be empty", currency.getRates().size() > 0);
    }

    @Test
    public void getData_passedEmptyParameters_shouldThrowException()
    {
        // Arrange & Act & Assert
        try {
            FinancialSystemHelper.getData("", "", "");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        assertTrue(false);
    }

    @ParameterizedTest
    @ValueSource(strings = {"sessionsCalculations", "staticMeasurements", "valueDistribution"})
    public void generateSessionsCalculations_shouldCreateFile(String command)
    {
        // Arrange & Act
        if (command.equals("sessionsCalculations"))
            FinancialSystemHelper.generateSessionsCalculations("one week", "A", "USD");
        else if (command.equals("staticMeasurements"))
            FinancialSystemHelper.generateStaticMeasurements("one week", "A", "USD");
        else if (command.equals("valueDistribution"))
            FinancialSystemHelper.generateValueDistribution("one week", "A", "USD", "A", "EUR");


        // Assert
        File file = new File(getDownloadFolderPath() + generateFileName(command));
        assertTrue(file.exists());
    }

    @ParameterizedTest
    @CsvSource({
            "sessionsCalculations,,,,,",
            "staticMeasurements,,,,,",
            "valueDistribution,,,,,"
    })
    public void generateSessionsCalculations_passedEmptyParameters_shouldThrowException(String command, String timeOption, String table1, String currencyCode1, String table2, String currencyCode2)
    {
        // Arrange & Act & Assert
        try {
            if (command.equals("sessionsCalculations"))
                FinancialSystemHelper.generateSessionsCalculations(timeOption, table1, currencyCode1);
            else if (command.equals("staticMeasurements"))
                FinancialSystemHelper.generateStaticMeasurements(timeOption, table1, currencyCode1);
            else if (command.equals("valueDistribution"))
                FinancialSystemHelper.generateValueDistribution(timeOption, table1, currencyCode1, table2, currencyCode2);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        assertTrue(false);
    }

    private static String getDownloadFolderPath() {
        String home = System.getProperty("user.home");
        return home + File.separator + "Downloads" + File.separator;
    }

    private static String generateFileName(String command) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

        Date currentTime = new Date();
        String formattedTime = dateFormat.format(currentTime);

        return command + "_" + formattedTime + ".csv";
    }
}

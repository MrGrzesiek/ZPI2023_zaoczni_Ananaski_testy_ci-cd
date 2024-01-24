import Structures.Interval.Interval;
import Structures.Rates.Currency;
import Structures.Rates.Rate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class FinancialSystemHelper {

    /**
     * Method used to generate Sessions Calculations
     * @param timeOption time option value
     * @param table table name for currency
     * @param currencyCode Currency code in ISO 4217
     */
    public static void generateSessionsCalculations(String timeOption, String table, String currencyCode) {
        Currency currency = FinancialSystemHelper.getData(timeOption, table, currencyCode);

        List<Rate> rates = currency.getRates();

        Integer growthSession = 0;
        Integer downSession = 0;
        Integer noChangeSession = 0;
        Boolean isGrowing = false;
        Boolean isFalling = false;
        Boolean isStable = false;

        for (Integer i = 0; i < rates.size() - 1; i++) {
            Rate currentRate = rates.get(i);
            Rate nextRate = rates.get(i + 1);

            if (currentRate.getMid() > nextRate.getMid() && !isFalling) {
                downSession++;
                isFalling = true;
                isGrowing = false;
                isStable = false;
            } else if (currentRate.getMid() < nextRate.getMid() && !isGrowing) {
                growthSession++;
                isGrowing = true;
                isFalling = false;
                isStable = false;
            } else if (currentRate.getMid().equals(nextRate.getMid()) && !isStable) {
                noChangeSession++;
                isStable = true;
                isFalling = false;
                isGrowing = false;
            }
        }

        String fileName = generateFileName("sessionsCalculations");
        List<String> headers = new ArrayList<>(Arrays.asList("growthSession", "downSession", "noChangeSession"));
        List<List<String>> values = new ArrayList<>();
        List<String> result = new ArrayList<>();
        result.add(growthSession.toString());
        result.add(downSession.toString());
        result.add(noChangeSession.toString());
        values.add(result);

        generateCSVFile(fileName, headers, values);
    }

    /**
     * Method used to generate Static Measurements
     * @param timeOption time option value
     * @param table table name for currency
     * @param currencyCode Currency code in ISO 4217
     */
    public static void generateStaticMeasurements(String timeOption, String table, String currencyCode) {
        Currency currency = FinancialSystemHelper.getData(timeOption, table, currencyCode);

        Double median;
        ArrayList<Double> mids = new ArrayList<>();

        for (Rate rate : currency.getRates()) {
            mids.add(rate.getMid());
        }

        Collections.sort(mids);

        if (mids.size() % 2 == 0) {
            median = (mids.get(mids.size() / 2) + mids.get(mids.size() / 2 - 1)) / 2;
        } else {
            median = mids.get(mids.size() / 2);
        }

        Double standardDeviation;

        Double sum = 0.0;

        for (Double mid : mids) {
            sum += mid;
        }

        Double avg = sum / mids.size();
        standardDeviation = 0.0;
        for (Double mid : mids) {
            standardDeviation += Math.pow((mid - avg), 2);
        }

        standardDeviation = standardDeviation / mids.size();
        standardDeviation = Math.sqrt(standardDeviation);

        Double coefficientOfVariation = standardDeviation / avg * 100;

        Map<Double, Integer> countMap = new HashMap<>();

        for (Double element : mids) {
            if (countMap.containsKey(element)) {
                countMap.put(element, countMap.get(element) + 1);
            } else {
                countMap.put(element, 1);
            }
        }

        Double dominant = mids.get(0);
        int count = countMap.get(dominant);

        for (Map.Entry<Double, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > count) {
                dominant = entry.getKey();
                count = entry.getValue();
            }
        }

        String fileName = generateFileName("staticMeasurements");
        List<String> headers = new ArrayList<>(Arrays.asList("median", "standardDeviation", "coefficientOfVariation", "dominant"));
        List<List<String>> values = new ArrayList<>();
        List<String> result = new ArrayList<>();
        result.add(median.toString());
        result.add(standardDeviation.toString());
        result.add(coefficientOfVariation.toString());
        result.add(dominant.toString());
        values.add(result);

        generateCSVFile(fileName, headers, values);
    }

    /**
     * Method used to generate Value Distribution
     * @param timeOption time option value
     * @param table1 table name for first currency
     * @param currencyCode1 Currency code in ISO 4217
     * @param table2 table name for second currency
     * @param currencyCode2 Currency code in ISO 4217
     */
    public static void generateValueDistribution(
            String timeOption,
            String table1,
            String currencyCode1,
            String table2,
            String currencyCode2) {

        Currency currency = FinancialSystemHelper.getData(timeOption, table1, currencyCode1);
        Currency currency2 = FinancialSystemHelper.getData(timeOption, table2, currencyCode2);

        ArrayList<Double> changes = new ArrayList<>();

        Double tempMidDiff = 0.0;
        for(int i=0; i < currency.getRates().size(); i++){

            Double mid  = currency.getRates().get(i).getMid();
            Double mid2 = currency2.getRates().get(i).getMid();

            Double midDiff = mid/mid2;
            if(i != 0){
                Double change = midDiff - tempMidDiff;
                changes.add(change);
            }

            tempMidDiff = midDiff;
        }

        Double max = Collections.max(changes);
        Double min = Collections.min(changes);

        Double intervalSize = (max - min)/10;

        List<Interval> intervals = new ArrayList<>();

        Double start = min;

        while (start < max){
            Double end = start+intervalSize;
            Interval interval = new Interval(start, end, 0);
            for(Double change : changes){
                if(change >= start && change < end){
                    interval.setCount(interval.getCount()+1);
                }
            }
            intervals.add(interval);
            start = end;
        }

        String fileName = generateFileName("valueDistribution");
        List<String> headers = new ArrayList<>(Arrays.asList("start", "end", "count"));
        List<List<String>> values = new ArrayList<>();
        for (Interval interval : intervals) {
            List<String> result = new ArrayList<>();
            result.add(interval.getStart().toString());
            result.add(interval.getEnd().toString());
            result.add(interval.getCount().toString());
            values.add(result);
        }
        generateCSVFile(fileName, headers, values);
    }

    /**
     * Method used to get data from NBP
     * @param timeOption time option value
     * @param table NBP API table Name
     * @param currencyCode Currency code in ISO 4217
     * @return Rate Surrency object
     */
    public static Currency getData(String timeOption, String table, String currencyCode) {
        if(timeOption.isBlank() || table.isBlank() || currencyCode.isBlank()) {
            throw new IllegalArgumentException("Parameter Error");
        }
        String pattern = "yyyy-MM-dd";

        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);

        String endDate = "";

        Calendar c = Calendar.getInstance();
        c.setTime(today);

        switch (timeOption) {
            case "one week":
                c.add(Calendar.DATE, -7);
                endDate = df.format(c.getTime());
                break;
            case "two weeks":
                c.add(Calendar.DATE, -14);
                endDate = df.format(c.getTime());
                break;
            case "month":
                c.add(Calendar.MONTH, -1);
                endDate = df.format(c.getTime());
                break;
            case "quarter":
                c.add(Calendar.MONTH, -3);
                endDate = df.format(c.getTime());
                break;
        }
        String data = "";
        try {
            data = FinancialSystemNBPAPI
                    .connection("/rates/" + table + "/" + currencyCode + "/" + endDate + "/" + todayAsString);
        } catch (Exception e) {
            System.exit(0);
        }

        Gson gson = new Gson();
        TypeToken<Currency> myToken = new TypeToken<>() {
        };

        Currency currency = gson.fromJson(data, myToken.getType());

        return currency;
    }

    /**
     * Method used to generate file name
     * @param command command name
     * @return new file name
     */
    private static String generateFileName(String command) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

        Date currentTime = new Date();
        String formattedTime = dateFormat.format(currentTime);

        return command + "_" + formattedTime + ".csv";
    }

    /**
     * Method used to generate and save CSV file
     * @param fileName file name
     * @param headers CSV headers
     * @param values CSV next lines with values
     */
    private static void generateCSVFile(String fileName, List<String> headers, List<List<String>> values) {
        String downloadFolderPath = getDownloadFolderPath();

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(downloadFolderPath + fileName), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
            csvWriter.writeNext(headers.toArray(new String[0]));

            for (List<String> recordValues: values) {
                csvWriter.writeNext(recordValues.toArray(new String[0]));
            }

            System.out.println("The CSV file was successfully generated in the Download folder.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get user dowlonad folder path
     * @return Download folder path
     */
    public static String getDownloadFolderPath() {
        String home = System.getProperty("user.home");
        return home + File.separator + "Downloads" + File.separator;
    }
}

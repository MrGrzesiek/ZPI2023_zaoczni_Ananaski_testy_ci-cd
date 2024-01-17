import Structures.Interval.Interval;
import Structures.Rates.Currency;
import Structures.Rates.Rate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class FinancialSystemHelper {
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
    }

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
    }

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

        System.out.println(changes);

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
    }

    public static Currency getData(String timeOption, String table, String currencyCode) {
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

        String data = FinancialSystemNBPAPI
                .connection("/rates/" + table + "/" + currencyCode + "/" + endDate + "/" + todayAsString);

        Gson gson = new Gson();
        TypeToken<Currency> myToken = new TypeToken<>() {
        };

        Currency currency = gson.fromJson(data, myToken.getType());

        return currency;
    }
}
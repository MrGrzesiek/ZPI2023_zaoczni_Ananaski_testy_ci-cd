import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class FinancialSystemNBPAPI {
    private static String baseUrl = "http://api.nbp.pl/api/exchangerates/";
    public static String connection(String address) {
        if(address.contains("//") || address.endsWith("/")) {
            throw new IllegalArgumentException("Wrong link address");
        }
        URL url = null;
        try {
            url = new URL(baseUrl + address + "/?format=json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        int responseCode = 0;
        try {
            assert url != null;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {
            StringBuilder inline = new StringBuilder();
            Scanner scanner = null;
            try {
                scanner = new Scanner(url.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (scanner != null && scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            if (scanner != null) {
                scanner.close();
            }
            return inline.toString();
        }

    }
}

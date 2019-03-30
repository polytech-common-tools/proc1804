import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class UpdatesChecker {

    private static final String currentTag = "v0.1";
    private static final String address = "https://api.github.com/repos/Hiraev/proc1804/releases/latest";
    private static final String releases_url = "https://github.com/Hiraev/proc1804/releases";
    private static final int MAX_CONNECTION_TIMEOUT = 7000;

    static void checkUpdates() {
        System.out.println("Checking for updates...");
        try { //get response
            var url = new URL(address);
            var httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(MAX_CONNECTION_TIMEOUT);
            httpURLConnection.setRequestMethod("GET");

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("Unexpected response code from the server");
                System.out.println("Check updates manually " + releases_url);
                return;
            }

            var in = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));

            printMessage(in);

            in.close();

        } catch (SocketTimeoutException e) {
            System.out.println("Connection takes too much time. Check your Internet connection.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printMessage(final BufferedReader in) throws IOException {
        String inputLine;
        var content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        final JsonElement response = new JsonParser().parse(content.toString());
        final String newTag = response.getAsJsonObject().get("tag_name").getAsString();
        final String body = response.getAsJsonObject().get("body").getAsString();
        final String url = response.getAsJsonObject().get("html_url").getAsString();

        if (newTag.equals(currentTag)) {
            System.out.println("No updates, you have the latest version!");
        } else {
            System.out.println("Version : " + newTag + " is available");
            System.out.println("Changes:\n" + body);
            System.out.println("Download it " + url);
        }
    }
}

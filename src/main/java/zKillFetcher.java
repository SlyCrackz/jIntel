import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.text.DecimalFormat;

public class zKillFetcher {
    private final ToonRetriever toonRetriever;

    public zKillFetcher(ToonRetriever toonRetriever) {
        this.toonRetriever = toonRetriever;
    }

    public String fetchZKillInfo(long characterId) throws IOException {
        // Construct the zKillboard URL based on the character ID
        String zKillUrl = "https://zkillboard.com/api/stats/characterID/" + characterId + "/";

        // Use an HTTP client to make a request to zKillboard
        OkHttpClient httpClient = new OkHttpClient();
        String zKillJson = toonRetriever.fetchJsonData(zKillUrl);

        // System.out.println("Raw zKill JSON: " + zKillJson); // Debugging line


        // Parse the JSON response from zKillboard
        JsonObject zKillData = toonRetriever.parseJsonResponse(zKillJson);

        // Format the JSON data into a readable string
        return formatZKillData(zKillData);
    }

    private int safeGetInt(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        return element != null ? element.getAsInt() : 0;
    }

    private double safeGetDouble(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        return element != null ? element.getAsDouble() : 0.0;
    }


    private String formatZKillData(JsonObject zKillData) {
        StringBuilder zKillDetails = new StringBuilder();

        // Ships Lost
        int shipsLost = safeGetInt(zKillData, "shipsLost");
        zKillDetails.append("Ships Lost: ").append(shipsLost).append("\n");

        // Ships Destroyed
        int shipsDestroyed = safeGetInt(zKillData, "shipsDestroyed");
        zKillDetails.append("Ships Destroyed: ").append(shipsDestroyed).append("\n");

        // ISK Lost
        double iskLost = safeGetDouble(zKillData, "iskLost");
        zKillDetails.append("ISK Lost: ").append(String.format("%,.2f", iskLost)).append("\n");

        // ISK Destroyed
        double iskDestroyed = safeGetDouble(zKillData, "iskDestroyed");
        zKillDetails.append("ISK Destroyed: ").append(String.format("%,.2f", iskDestroyed)).append("\n");

        // Add additional fields as necessary
        // For example, if you want to include points lost and destroyed
        // int pointsLost = safeGetInt(zKillData, "pointsLost");
        // zKillDetails.append("Points Lost: ").append(pointsLost).append("\n");

        // int pointsDestroyed = safeGetInt(zKillData, "pointsDestroyed");
        // zKillDetails.append("Points Destroyed: ").append(pointsDestroyed).append("\n");

        // Other fields can be added similarly, based on what data is available in your JSON response

        return zKillDetails.toString();
    }



}

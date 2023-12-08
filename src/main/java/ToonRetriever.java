import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

public class ToonRetriever {
    private final OkHttpClient httpClient;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public ToonRetriever(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Long getCharacterId(String characterName) throws IOException {
        String url = HttpUrl.parse("https://esi.evetech.net/latest/universe/ids/")
                .newBuilder()
                .build()
                .toString();

        JsonArray nameArray = new JsonArray();
        nameArray.add(characterName);
        String jsonBody = nameArray.toString();

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

            if (jsonResponse.has("characters")) {
                JsonArray characters = jsonResponse.getAsJsonArray("characters");
                if (characters.size() > 0) {
                    JsonObject character = characters.get(0).getAsJsonObject();
                    return character.get("id").getAsLong();
                }
            }
        }
        return null;
    }

    public String fetchCharacterDetails(Long characterId) throws IOException {
        String url = "https://esi.evetech.net/latest/characters/" + characterId + "/";

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            JsonObject characterJson = JsonParser.parseString(responseBody).getAsJsonObject();

            JsonArray idArray = new JsonArray();
            idArray.add(characterId);
            JsonObject affiliationInfo = fetchAffiliationInfo(idArray);

            return formatCharacterDetails(characterId, characterJson, affiliationInfo);
        }
    }

    private JsonObject fetchAffiliationInfo(JsonArray characterIds) throws IOException {
        String url = "https://esi.evetech.net/latest/characters/affiliation/";
        String jsonBody = characterIds.toString();

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            JsonArray responseArray = JsonParser.parseString(responseBody).getAsJsonArray();
            return responseArray.size() > 0 ? responseArray.get(0).getAsJsonObject() : new JsonObject();
        }
    }

    private String formatCharacterDetails(Long characterId, JsonObject characterJson, JsonObject affiliationInfo) throws IOException {
        StringBuilder detailsBuilder = new StringBuilder();

        detailsBuilder.append("Character ID: ").append(characterId).append("\n");
        detailsBuilder.append("Creation Date: ").append(characterJson.get("birthday").getAsString()).append("\n");

        if (affiliationInfo.has("corporation_id")) {
            long corporationId = affiliationInfo.get("corporation_id").getAsLong();
            String corporationName = resolveEntityName(corporationId);
            detailsBuilder.append("Corporation: ").append(corporationName).append("\n");
        }

        if (affiliationInfo.has("alliance_id")) {
            long allianceId = affiliationInfo.get("alliance_id").getAsLong();
            String allianceName = resolveEntityName(allianceId);
            detailsBuilder.append("Alliance: ").append(allianceName).append("\n");
        }

        detailsBuilder.append("Security Status: ").append(characterJson.get("security_status").getAsDouble()).append("\n");

        return detailsBuilder.toString();
    }

    private String resolveEntityName(long entityId) throws IOException {
        // Make a request to the /universe/names/ endpoint
        String url = "https://esi.evetech.net/latest/universe/names/";

        JsonArray idArray = new JsonArray();
        idArray.add(entityId);
        String jsonBody = idArray.toString();

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            JsonArray responseArray = JsonParser.parseString(responseBody).getAsJsonArray();
            if (responseArray.size() > 0) {
                JsonObject entityObject = responseArray.get(0).getAsJsonObject();
                return entityObject.get("name").getAsString();
            }
        }
        return "Unknown";
    }

    public String fetchJsonData(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = this.httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public JsonObject parseJsonResponse(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }

}
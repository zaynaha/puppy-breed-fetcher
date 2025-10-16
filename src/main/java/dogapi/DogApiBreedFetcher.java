package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */

    /**
     * Helper method to make an HTTP GET request to a given URL
     * and return the raw JSON response as a String.
     */
    public String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    public List<String> getSubBreeds(String breed) {
        // make an empty list to hold sub-breed names we will collect
        List<String> subBreeds = new ArrayList<>();
        // build the exact web address (URL) that returns sub-breeds for the given breed
        String url = "https://dog.ceo/api/breed/" + breed + "/list";

        try {
            // call the helper method to fetch the text the server sends back
            String responseBody = run(url);
            // turn that text (which is JSON) into an object we can read from
            JSONObject json = new JSONObject(responseBody);
            // get the array stored under the key "message" (that array lists the sub-breeds)
            JSONArray messageArray = json.getJSONArray("message");
            // go through each item in the array and add it to our list
            for (int i = 0; i < messageArray.length(); i++) {
                subBreeds.add(messageArray.getString(i));
            }

        } catch (IOException e) {
            // if there was a problem fetching the data, print an error so we can see what went wrong
            System.err.println("Error fetching sub-breeds for " + breed + ": " + e.getMessage());
        }
        // return the list (may be empty if there were no sub-breeds or an error happened)
        return subBreeds;
    }
}
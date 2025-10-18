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


    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {

        // build the exact web address (URL) that returns sub-breeds for the given breed
        String url = "https://dog.ceo/api/breed/" + breed + "/list";

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new BreedNotFoundException(breed);
            }

            String responseBody = response.body().string();

            // turn that text (which is JSON) into an object we can read from
            JSONObject json = new JSONObject(responseBody);
            String status = json.getString("status");

            // check if the API returned an error
            if (status.equals("error")) {
                throw new BreedNotFoundException(breed);
            }

            // get the array stored under the key "message" (that array lists the sub-breeds)
            JSONArray messageArray = json.getJSONArray("message");
            // make an empty list to hold sub-breed names we will collect
            List<String> subBreeds = new ArrayList<>();
            // go through each item in the array and add it to our list
            for (int i = 0; i < messageArray.length(); i++) {
                subBreeds.add(messageArray.getString(i));
            }
            return subBreeds;

        } catch (IOException | org.json.JSONException e) {
            // convert any IO or JSON parsing errors into BreedNotFoundException
            throw new BreedNotFoundException(breed);
        }
    }
}
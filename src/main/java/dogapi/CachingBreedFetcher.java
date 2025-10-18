package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    private BreedFetcher fetcher;          // store the real fetcher
    private Map<String, List<String>> cache = new HashMap<>();  // store cached results

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;    // keep the real fetcher
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // Step 1: check cache first
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }
        callsMade++;                                          // count the call
        // Step 2: fetch from real fetcher if not in cache
        List<String> subBreeds = fetcher.getSubBreeds(breed) ; // real fetch
        cache.put(breed, subBreeds);                        // save in cache
        return subBreeds;                                   // return result
    }

    public int getCallsMade() {
        return callsMade;
    }
}
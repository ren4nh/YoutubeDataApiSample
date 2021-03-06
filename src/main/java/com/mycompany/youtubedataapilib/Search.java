package com.mycompany.youtubedataapilib;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Prints a list of videos based on a search term.
 *
 * @author Jeremy Walker
 */
public class Search {

    /**
     * Global instance of the HTTP transport.
     */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * Global instance of the max number of videos we want returned (50 = upper
     * limit per page).
     */
    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

    /**
     * Global instance of Youtube object to make all API requests.
     */
    private static YouTube youtube;

    private String apiKey = "AIzaSyAIIAJ4-cCR27-whnXPBqnGIxgGn8QO4Hs";
    private String channelId = "UCcdw9PRuUeSimZDP877kc9w";
    private String queryTerm;
    private String applicationName = "youtube-cmdline-search-sample";

    public Search() {
    }

    public Search(String apiKey, String channelId, String queryTerm, String applicationName) {
        this.apiKey = apiKey;
        this.channelId = channelId;
        this.queryTerm = queryTerm;
        this.applicationName = applicationName;
    }

    /**
     * Initializes YouTube object to search for videos on YouTube
     * (Youtube.Search.List). The program then prints the names and thumbnails
     * of each of the videos (only first 50 videos).
     *
     * @param args command line args.
     * @return list with video info
     */
    public List<VideoInfo> execute() {
        // Read the developer key from youtube.properties
        List<VideoInfo> result = new ArrayList<>();
        try {
            /*
       * The YouTube object is used to make all API requests. The last argument is required, but
       * because we don't need anything initialized when the HttpRequest is initialized, we override
       * the interface and provide a no-op function.
             */
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName(applicationName).build();

            // Get query term from user.
            YouTube.Search.List search = youtube.search().list("id,snippet").setOrder("date");
            /*
       * It is important to set your developer key from the Google Developer Console for
       * non-authenticated requests (found under the API Access tab at this link:
       * code.google.com/apis/). This is good practice and increased your quota.
             */
//            String apiKey = properties.getProperty("youtube.apikey");
            search.setKey(apiKey);
            search.setChannelId(channelId);
            if (queryTerm != null) {
                search.setQ(queryTerm);
            }
            /*
       * We are only searching for videos (not playlists or channels). If we were searching for
       * more, we would add them as a string like this: "video,playlist,channel".
             */
            search.setType("video");
            /*
       * This method reduces the info returned to only the fields we need and makes calls more
       * efficient.
             */
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            SearchListResponse searchResponse = search.execute();

            List<SearchResult> searchResultList = searchResponse.getItems();
            
            if (searchResultList != null) {
                result  = prettyPrint(searchResultList.iterator());
            }
            
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    /*
   * Prints out all SearchResults in the Iterator. Each printed line includes title, id, and
   * thumbnail.
   *
   * @param iteratorSearchResults Iterator of SearchResults to print
   *
   * @param query Search query (String)
     */
    private List<VideoInfo>  prettyPrint(Iterator<SearchResult> iteratorSearchResults) {
        List<VideoInfo> result = new ArrayList<>();
        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Double checks the kind is video.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("default");
                result.add(new VideoInfo(rId.getVideoId(), thumbnail.getUrl(), singleVideo.getSnippet().getTitle()));
            }
        }
        return result;
    }

}

package playBot.Services;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import playBot.Models.Song;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;

public class YTServices {

    private static final String DEVELOPER_KEY = "AIzaSyCAJnika5WB4oIDIm9Vyjc5d6Kgcgez6MU";

    private static final String APPLICATION_NAME = "playBot";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public Song searchSong(String query, int searchNum) throws GeneralSecurityException, IOException {
        Scanner scanner = new Scanner(System.in);
        Song song = new Song();
        YouTube youtubeService = getService();
        YouTube.Search.List request = youtubeService.search()
                .list(Collections.singletonList("snippet"));
        SearchListResponse searchResponse = request.setKey(DEVELOPER_KEY)
                .setMaxResults((long) searchNum)
                .setType(Collections.singletonList("video"))
                .setVideoCategoryId("10")
                .setQ(query)
                .execute();
        List<SearchResult> searchResultList = searchResponse.getItems();
        if (searchResultList != null) {
            song = listSong(searchResultList.iterator());
        }

        return song;
    }

    private Song listSong(Iterator<SearchResult> iteratorSearchResults) {
        Song song = new Song();
        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }
        String vidURL = "Hello World";
        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            song.setTitle(singleVideo.getSnippet().getTitle());
            song.setVidURL(rId.getVideoId());

            if (rId.getKind().equals("youtube#video")) {
                System.out.println(" Video Id: " + song.getVidURL());
                System.out.println(" Title: " + song.getTitle());
                System.out.println("\n-------------------------------------------------------------\n");
            }
        }
        return song;
    }

    public Queue<Song> playSong(Queue<Song> songQueue) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String vidURL = "https://www.youtube.com/watch?v=" + songQueue.poll().getVidURL();
        Process process = Runtime.getRuntime().exec("mpv " + vidURL +" --no-video");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        return songQueue;
    }

    public boolean checkIfRunning() {
        String line;
        boolean isRunning = false;
        try {
            Process p = Runtime.getRuntime().exec("pidof mpv");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            if ((line = input.readLine()) != null) {
//                System.out.println(line);
                isRunning = true;
            }
        } catch (Exception err) {
            System.out.println(err);
        }
        return isRunning;
    }

    public Queue<Song> addToQueue(Queue<Song> songQueue, Song song) {
        songQueue.add(song);
        System.out.println("Adding to queue");
        return songQueue;
    }

    public int getSearchNum (String cmnd) {
        int searchNum = 1;
        if (cmnd.length() > 1) {
            searchNum = Integer.parseInt(String.valueOf(cmnd.charAt(1)));
        }
        return searchNum;
    }
}
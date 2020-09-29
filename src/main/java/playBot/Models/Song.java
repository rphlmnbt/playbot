package playBot.Models;

public class Song {
    private String title;
    private String vidURL;

    public Song() {

    }

    public Song(String title, String vidURL) {
        this.title = title;
        this.vidURL = vidURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVidURL() {
        return vidURL;
    }

    public void setVidURL(String vidURL) {
        this.vidURL = vidURL;
    }
}

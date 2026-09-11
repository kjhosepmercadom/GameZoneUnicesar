package com.gamezone.model;

/**
 * Represents a video game product, identified by its platform and genre.
 *
 * @author Dair
 */
public class VideoGame extends Product {

    private String platform;
    private String genre;

    public VideoGame(String id, String name, double price, int stock,
                     String platform, String genre) {
        super(id, name, price, stock);
        this.platform = platform;
        this.genre = genre;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String getDescription() {
        return getName() + " (Video game - " + genre + ", " + platform + ")";
    }
}
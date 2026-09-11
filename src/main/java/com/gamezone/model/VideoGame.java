package com.gamezone.model;

/**
 * Represents a video game product, identified by its platform and genre.
 *
 * @author Dair
 */
public class VideoGame extends Product {

    private String platform;
    private String genre;

    /**
     * Creates a new video game.
     *
     * @param id       unique identifier of the product
     * @param name     display name of the product
     * @param price    unit price, must not be negative
     * @param stock    available quantity, must not be negative
     * @param platform platform the game was developed for
     * @param genre    genre of the game
     */
    public VideoGame(String id, String name, double price, int stock,
                     String platform, String genre) {
        super(id, name, price, stock);
        this.platform = platform;
        this.genre = genre;
    }

    /**
     * Returns the platform this game was developed for.
     *
     * @return the platform name
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * Sets the platform this game was developed for.
     *
     * @param platform the new platform name
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * Returns the genre of the game.
     *
     * @return the genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the genre of the game.
     *
     * @param genre the new genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Builds a description including the game's genre and platform.
     *
     * @return the full description of the video game
     */
    @Override
    public String getDescription() {
        return getName() + " (Video game - " + genre + ", " + platform + ")";
    }
}
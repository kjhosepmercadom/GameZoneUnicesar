package com.gamezone.model;

/**
 * Video game sold by the store. A video game is characterized by the
 * platform it was developed for, its genre and its recommended age rating.
 */
public class VideoGame extends Product {

    private String platform;
    private String genre;
    private String ageRating;

    /**
     * Creates a video game with all its information.
     *
     * @param id        unique product identifier
     * @param title     commercial title of the video game
     * @param price     unit price in Colombian pesos
     * @param stock     quantity available in inventory
     * @param platform  platform the game was developed for
     * @param genre     genre of the game
     * @param ageRating recommended age rating
     */
    public VideoGame(String id, String title, double price, int stock,
                     String platform, String genre, String ageRating) {
        super(id, title, price, stock);
        this.platform = platform;
        this.genre = genre;
        this.ageRating = ageRating;
    }

    /**
     * Returns the platform the game was developed for.
     *
     * @return the platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * Sets the platform the game was developed for.
     *
     * @param platform the new platform
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
     * Returns the recommended age rating.
     *
     * @return the age rating
     */
    public String getAgeRating() {
        return ageRating;
    }

    /**
     * Sets the recommended age rating.
     *
     * @param ageRating the new age rating
     */
    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

    /**
     * Builds the video game description including platform, genre and
     * age rating.
     *
     * @return the full video game description
     */
    @Override
    public String getDescription() {
        return String.format("[Videojuego] %s - %s | Plataforma: %s | Genero: %s | Clasificacion: %s"
                        + " | Precio: $%,.0f | Stock: %d",
                getId(), getTitle(), platform, genre, ageRating, getPrice(), getStock());
    }

    /**
     * Returns the category of this product type.
     *
     * @return {@code "VIDEOGAME"}
     */
    @Override
    public String getCategory() {
        return "VIDEOGAME";
    }
}

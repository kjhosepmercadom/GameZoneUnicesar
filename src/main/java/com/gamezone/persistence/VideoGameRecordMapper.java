package com.gamezone.persistence;

import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;

/**
 * CSV mapper of video games: {@code VIDEOGAME;id;title;price;stock;platform;genre;ageRating}.
 */
class VideoGameRecordMapper implements RecordMapper<Product> {

    private static final String SEPARATOR = ";";

    /**
     * Returns the discriminator of video games.
     *
     * @return {@code "VIDEOGAME"}
     */
    @Override
    public String getTypeCode() {
        return "VIDEOGAME";
    }

    /**
     * Tells whether the product is a video game.
     *
     * @param value the product to store
     * @return {@code true} for video games
     */
    @Override
    public boolean supports(Product value) {
        return value instanceof VideoGame;
    }

    /**
     * Builds the columns of a video game.
     *
     * @param value the video game to store
     * @return the columns separated by semicolons
     */
    @Override
    public String toColumns(Product value) {
        VideoGame videoGame = (VideoGame) value;
        return CsvFile.clean(videoGame.getId()) + SEPARATOR + CsvFile.clean(videoGame.getTitle()) + SEPARATOR
                + videoGame.getPrice() + SEPARATOR + videoGame.getStock() + SEPARATOR
                + CsvFile.clean(videoGame.getPlatform()) + SEPARATOR + CsvFile.clean(videoGame.getGenre())
                + SEPARATOR + CsvFile.clean(videoGame.getAgeRating());
    }

    /**
     * Rebuilds a video game from the columns of a line.
     *
     * @param fields every column of the line
     * @return the video game
     */
    @Override
    public Product fromFields(String[] fields) {
        return new VideoGame(fields[1], fields[2], Double.parseDouble(fields[3]), Integer.parseInt(fields[4]),
                fields[5], fields[6], fields[7]);
    }
}

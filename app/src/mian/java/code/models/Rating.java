package edu.illinois.cs.cs125.fall2020.mp.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Rating class for storing client ratings of courses.
 */
public class Rating {
    /** Rating indicating that the course has not been rated yet. */

  public static final double NOT_RATED = -1.0;
  private String id;
  private double rating;

    /**
     * Create a Rating with the provided fields.
     *
     * @param setId the clientID
     * @param setRating the rating of the course
     */

  @JsonCreator
    public Rating(
            @JsonProperty("id")  final String setId,
            @JsonProperty("rating") final double setRating) {
    id = setId;
    rating = setRating;
  }

  /**
     * Get the clinetID.
     *
     * @return the clientID
   */

  public String getId() {
    return id;
  }

  /**
     * Get the rating of the course.
     *
     * @return the rating
   **/

  public double getRating() {
    return rating;
  }
}

package edu.illinois.cs.cs125.fall2020.mp.models;

/**
 * Model holding the course information with detailed description.
 */
public class Course extends Summary {
  private String description;

   /**
     * Get the description for this Course.
     *
     * @return the description for this Course
     */

  public String getDescription() {
    return description;
  }

   /** Create an empty Course. */
  public Course() {}

}

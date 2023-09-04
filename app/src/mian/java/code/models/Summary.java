package edu.illinois.cs.cs125.fall2020.mp.models;

import androidx.annotation.NonNull;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Model holding the course summary information shown in the course list.
 *
 * <p>You will need to complete this model for MP0.
 */
public class Summary implements SortedListAdapter.ViewModel {
  private String year;

  /**
   * Get the year for this Summary.
   *
   * @return the year for this Summary
   */
  public final String getYear() {
    return year;
  }

  private String semester;

  /**
   * Get the semester for this Summary.
   *
   * @return the semester for this Summary
   */
  public final String getSemester() {
    return semester;
  }

  private String department;

  /**
   * Get the department for this Summary.
   *
   * @return the department for this Summary
   */
  public final String getDepartment() {
    return department;
  }

  private String number;

  /**
   * Get the number for this Summary.
   *
   * @return the number for this Summary
   */
  public final String getNumber() {
    return number;
  }

  private String title;

  /**
   * Get the title for this Summary.
   *
   * @return the title for this Summary
   */
  public final String getTitle() {
    return title;
  }

  /**
   * creating empty Summary constructor.
   */
  public Summary() {}

  private String clientID;

  /**
   * Get the clientID.
   *
   * @return the clientID
   */
  public final String getClientID() {
    return  clientID;
  }

  /**
   * Create a Summary with the provided fields.
   *
   * @param setYear the year for this Summary
   * @param setSemester the semester for this Summary
   * @param setDepartment the department for this Summary
   * @param setNumber the number for this Summary
   * @param setTitle the title for this Summary
   */
  public Summary(
      final String setYear,
      final String setSemester,
      final String setDepartment,
      final String setNumber,
      final String setTitle) {
    year = setYear;
    semester = setSemester;
    department = setDepartment;
    number = setNumber;
    title = setTitle;
  }

  /*
  public Summary(
          final String setYear,
          final String setSemester,
          final String setDepartment,
          final String setNumber,
          final String setclinetID) {
    year = setYear;
    semester = setSemester;
    department = setDepartment;
    number = setNumber;
    clientID = setclinetID;
  }

   */


  /** {@inheritDoc} */
  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof Summary)) {
      return false;
    }
    Summary course = (Summary) o;
    return Objects.equals(year, course.year)
        && Objects.equals(semester, course.semester)
        && Objects.equals(department, course.department)
        && Objects.equals(number, course.number);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(year, semester, department, number);
  }

  /** {@inheritDoc} */
  @Override
  public <T> boolean isSameModelAs(@NonNull final T model) {
    return equals(model);
  }

  /** {@inheritDoc} */
  @Override
  public <T> boolean isContentTheSameAs(@NonNull final T model) {
    return equals(model);
  }

  /**
   * Compare two summaries in the order department, number and title.
   */
  public static final Comparator<Summary> COMPARATOR =
      (courseModel1, courseModel2) -> {
        if (courseModel1.department.compareTo(courseModel2.department) > 0) {
          return 1;
        } else if (courseModel1.department.compareTo(courseModel2.department) < 0) {
          return -1;
        } else {
          if (courseModel1.number.compareTo(courseModel2.number) > 0) {
            return 1;
          } else if (courseModel1.number.compareTo(courseModel2.number) < 0) {
            return -1;
          } else {
            return Integer.compare(courseModel1.title.compareTo(courseModel2.title), 0);
          }
        }
      };

  /**
   * Filter and get a new list that contains the typed text.
   *
   * @param courses
   * @param text
   * @return a new list that contains the typed text
   */
  public static List<Summary> filter(
      @NonNull final List<Summary> courses, @NonNull final String text) {
    List<Summary> arr = new ArrayList<>();
    for (int i = 0; i < courses.size(); i++) {
      Summary course = courses.get(i);
      if (course.getFullTitle().toLowerCase().contains(text.toLowerCase())) {
        arr.add(course);
      }
    }
    return arr;
  }

  /**
   * return a full title of the course by adding department, number and title.
   *
   * @return return a full title
   */
  public String getFullTitle() {
    return department + " " + number + ": " + title;
  }
}

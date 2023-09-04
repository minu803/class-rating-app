package edu.illinois.cs.cs125.fall2020.mp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


import edu.illinois.cs.cs125.fall2020.mp.R;
import edu.illinois.cs.cs125.fall2020.mp.application.CourseableApplication;
import edu.illinois.cs.cs125.fall2020.mp.databinding.ActivityCourseBinding;
import edu.illinois.cs.cs125.fall2020.mp.models.Course;
import edu.illinois.cs.cs125.fall2020.mp.models.Rating;
import edu.illinois.cs.cs125.fall2020.mp.models.Summary;
import edu.illinois.cs.cs125.fall2020.mp.network.Client;

/**
 *  Course activity showing the course description.
 */

public final class CourseActivity extends AppCompatActivity
        implements RatingBar.OnRatingBarChangeListener,
          Client.CourseClientCallbacks {
  private ObjectMapper mapper = new ObjectMapper();
  private static final String TAG = CourseActivity.class.getSimpleName();
  private ActivityCourseBinding binding;


  /**
   * Called when CourseActivity is created.
   *
   * The method is called when the filtered course is clicked.
   *
   * @param savedInstanceState saved instance state.
   */
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    Log.d(TAG, intent.getStringExtra("COURSE"));
    binding = DataBindingUtil.setContentView(this, R.layout.activity_course);
    binding.textview.setText(intent.getStringExtra("COURSE"));
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    try {
      Summary summary = mapper.readValue(intent.getStringExtra("COURSE"), Summary.class);
      CourseableApplication application = (CourseableApplication) getApplication();
      application.getCourseClient().getCourse(summary, this);
      application.getCourseClient().getRating(summary, application.getClientID(), this);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    binding.rating.setOnRatingBarChangeListener(this);
  }

    /**
     * getting response from the client and display the description.
     * @param summary the summary
     * @param course the course
     */

  @Override
  public void courseResponse(final Summary summary, final Course course) {
    binding.textview.setText(summary.getFullTitle());
    binding.textview1.setText(course.getDescription());
    //set the text
  }

  @Override
  public void yourRating(final Summary summary, final Rating rating) {
    //set the text
    double r = rating.getRating();
    if (r != Rating.NOT_RATED) {
      binding.rating.setRating((float) r);
    }
  }


  @Override
  public void onRatingChanged(final RatingBar ratingBar, final float rating, final boolean fromUser) {
    try {
      Intent intent = getIntent();
      Summary summary = mapper.readValue(intent.getStringExtra("COURSE"), Summary.class);
      CourseableApplication application = (CourseableApplication) getApplication();
      Rating ratingObject = new Rating(application.getClientID(), (double) rating);
      application.getCourseClient().postRating(summary, ratingObject, this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}

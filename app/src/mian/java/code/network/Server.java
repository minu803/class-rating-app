package edu.illinois.cs.cs125.fall2020.mp.network;


import androidx.annotation.NonNull;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.illinois.cs.cs125.fall2020.mp.application.CourseableApplication;
import edu.illinois.cs.cs125.fall2020.mp.models.Rating;
import edu.illinois.cs.cs125.fall2020.mp.models.Summary;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;


import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

/**
 * Development course API server.
 *
 * <p>Normally you would run this server on another machine, which the client would connect to over
 * the internet. For the sake of development, we're running the server right alongside the app on
 * the same device. However, all communication between the course API client and course API server
 * is still done using the HTTP protocol. Meaning that eventually it would be straightforward to
 * move this server to another machine where it could provide data for all course API clients.
 *
 * <p>You will need to add functionality to the server for MP1 and MP2.
 */
public final class Server extends Dispatcher {
  @SuppressWarnings({"unused", "RedundantSuppression"})
  private static final String TAG = Server.class.getSimpleName();

  private final Map<String, String> summaries = new HashMap<>();

  private MockResponse getSummary(@NonNull final String path) {
    String[] parts = path.split("/");
    if (parts.length != 2) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }

    String summary = summaries.get(parts[0] + "_" + parts[1]);
    if (summary == null) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
    }
    return new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(summary);
  }

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  private final Map<Summary, String> courses = new HashMap<>();

  private MockResponse getCourse(@NonNull final String path) {
    String[] parts = path.split("/");
    final int len = 4;
    if (parts.length != len) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
    Summary course = new Summary(parts[0], parts[1], parts[2], parts[3], "");

    String c = courses.get(course);
    if (c == null) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
    }
    return new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(c);
  }


  //Map<Summary, Map<Summary, Rating>> ratings = new HashMap<>();
  //Map<Summary, Map<uuid, Rating>>
  //private final Map<String, String> ratings = new HashMap<>();
  // second private final Map<Summary, Map<String, Rating>> ratings = new HashMap<>();

  private final Map<Summary, Map<UUID, Rating>> ratings = new HashMap<>();

  private MockResponse getRating(@NonNull final String path) {
    if (!path.contains("?client=")) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
    String[] path1 = path.split("\\?client=");
    UUID clientID = null;
    try {
      clientID = UUID.fromString(path1[1]);
    } catch (IllegalArgumentException e) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
    String[] parts = path1[0].split("/");
    final int len = 4;
    final int numID = 36;
    if (parts.length != len) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
    if (clientID.toString().length() != numID) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
    if (path1[0].substring(path1[0].length() - 1).equals("/")) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }

    //summary object
    //private final Map<Summary, Map<UUID, Rating>> ratings = new HashMap<>();
    // ratings.get(key): Map<UUID, Rating> <- put UUID, Rating
    Summary key = new Summary(parts[0], parts[1], parts[2], parts[3], "");

    if (courses.get(key) == null) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
    }

    Map<UUID, Rating> rating = ratings.getOrDefault(key, new HashMap<>());
    Rating r1 = rating.get(clientID);

    if (rating.get(clientID) == null) {
      Rating defaultRating = new Rating(clientID.toString(), Rating.NOT_RATED);
      try {
        rating.put(clientID, defaultRating);
        ratings.put(key, rating);
        String defaultRatingStr = mapper.writeValueAsString(defaultRating);
        return new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(defaultRatingStr);
      } catch (JsonProcessingException e) {
        return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
      }
    }
    String r1str = null;
    try {
      r1str = mapper.writeValueAsString(r1);
      System.out.println(r1str);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(r1str);
  }


  // CREATE Mapping.....avoid weird errors
    //HashMap<UUID, Rating> temp;
    //rating.put(key, temp);



//----------------------------------------------------------------------

  private MockResponse postRating(@NonNull final String path, @NonNull final String requestbody) {
    if (!path.contains("?client=")) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
    String[] path1 = path.split("\\?client=");
    UUID clientID = null;
    try {
      clientID = UUID.fromString(path1[1]);
    } catch (IllegalArgumentException e) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
    String[] parts = path1[0].split("/");
    final int len = 4;
    final int numID = 36;
    if (parts.length != len) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
    if (clientID.toString().length() != numID) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
    if (path1[0].substring(path1[0].length() - 1).equals("/")) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }

    //summary object
    Summary key = new Summary(parts[0], parts[1], parts[2], parts[3], "");
    if (courses.get(key) == null) {
      System.out.println("no course error");
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
    }

    Map<UUID, Rating> rating = ratings.get(key);

    try {
      Rating newRating = mapper.readValue(requestbody, Rating.class);
      rating.put(clientID, newRating);
      ratings.put(key, rating);
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_MOVED_TEMP).setHeader(
              "Location", "/rating/" + path
      );
    } catch (Exception e) {
      e.printStackTrace();
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
  }





  @NonNull
  @Override
  public MockResponse dispatch(@NonNull final RecordedRequest request) {
    try {
      String path = request.getPath();
      System.out.println("[test]" + path);
      if (path == null || request.getMethod() == null) {
        return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
      } else if (path.equals("/") && request.getMethod().equalsIgnoreCase("HEAD")) {
        return new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK);
      } else if (path.startsWith("/summary/")) {
        return getSummary(path.replaceFirst("/summary/", ""));
      } else if (path.startsWith("/course/")) {
        return getCourse(path.replaceFirst("/course/", ""));
      } else if (path.startsWith("/rating/") && request.getMethod().equals("GET")) {
        return getRating(path.replaceFirst("/rating/", ""));
      } else if (path.startsWith("/rating/") && request.getMethod().equals("POST")) {
        String requestBody = request.getBody().readUtf8();
        return postRating(path.replaceFirst("/rating/", ""), requestBody);
      }
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
    } catch (Exception e) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }
  }

  private static boolean started = false;

  /**
   * Start the server if has not already been started.
   *
   * <p>We start the server in a new thread so that it operates separately from and does not
   * interfere with the rest of the app.
   */
  public static void start() {
    if (!started) {
      new Thread(Server::new).start();
      started = true;
    }
  }

  private final ObjectMapper mapper = new ObjectMapper();

  private Server() {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    loadSummary("2020", "fall");
    loadCourses("2020", "fall");

    try {
      MockWebServer server = new MockWebServer();
      server.setDispatcher(this);
      server.start(CourseableApplication.SERVER_PORT);

      String baseUrl = server.url("").toString();
      if (!CourseableApplication.SERVER_URL.equals(baseUrl)) {
        throw new IllegalStateException("Bad server URL: " + baseUrl);
      }
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  @SuppressWarnings("SameParameterValue")
  private void loadSummary(@NonNull final String year, @NonNull final String semester) {
    String filename = "/" + year + "_" + semester + "_summary.json";
    String json =
        new Scanner(Server.class.getResourceAsStream(filename), "UTF-8").useDelimiter("\\A").next();
    summaries.put(year + "_" + semester, json);
  }

  @SuppressWarnings("SameParameterValue")
  private void loadCourses(@NonNull final String year, @NonNull final String semester) {
    String filename = "/" + year + "_" + semester + ".json";
    String json =
        new Scanner(Server.class.getResourceAsStream(filename), "UTF-8").useDelimiter("\\A").next();
    try {
      JsonNode nodes = mapper.readTree(json);
      for (Iterator<JsonNode> it = nodes.elements(); it.hasNext(); ) {
        JsonNode node = it.next();
        Summary course = mapper.readValue(node.toString(), Summary.class);
        courses.put(course, node.toPrettyString());
      }
    } catch (JsonProcessingException e) {
      throw new IllegalStateException(e);
    }
  }
}

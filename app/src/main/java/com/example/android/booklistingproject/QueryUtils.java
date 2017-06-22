package com.example.android.booklistingproject;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static final String LOG_TAG = MainActivity.class.getName();

    public QueryUtils() {
    }

    public static List<Book> fetchData(String query) {

        URL url = createUrl(query);
        String response = null;

        try {
            response = makeHttpRequest(url);
        } catch (IOException e) {

            Log.e(LOG_TAG, "Problem with the HTTP request", e);
        }
        List<Book> books = extractFromJson(response);

        return books;

    }
    private static URL createUrl(String query) {

        URL url = null;

        try {
            url = new URL("https://www.googleapis.com/books/v1/volumes?q=intitle:" + query + "&maxResults=10");
        } catch (MalformedURLException e) {

            Log.e(LOG_TAG, "URL creation failed", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection connection = null;
        InputStream stream = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {

                stream = connection.getInputStream();
                jsonResponse = readFromStream(stream);
            } else {

                Log.e(LOG_TAG, "Error Response Code: " + connection.getResponseCode());
            }
        } catch (IOException e) {

            Log.e(LOG_TAG, "URL creation failed", e);
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
            if (stream != null) {
                stream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream stream) throws IOException {

        StringBuilder builder = new StringBuilder();

        if (stream != null) {

            InputStreamReader streamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            String line = bufferedReader.readLine();
            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return builder.toString();
    }

    private static List<Book> extractFromJson(String responseJson) {

        if (TextUtils.isEmpty(responseJson)) {
            return null;
        }

        List<Book> books = new ArrayList<Book>();

        try {
            JSONObject volumes = new JSONObject(responseJson);
            JSONArray items = volumes.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject volume = item.getJSONObject("volumeInfo");
                String title = volume.getString("title");
                String author;
                if (volume.has("authors")) {
                    author = volume.getJSONArray("authors").get(0).toString();
                } else {
                    author = "Unknown author";
                }
                String link = volume.getString("infoLink");

                Book book = new Book(author, title, link);
                books.add(book);
            }

        } catch (JSONException e) {

            Log.e(LOG_TAG, "Error extracting the data from the JSON response", e);
        }

        return books;

    }

}

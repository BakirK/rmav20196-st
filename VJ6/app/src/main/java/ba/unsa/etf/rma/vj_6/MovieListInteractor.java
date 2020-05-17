package ba.unsa.etf.rma.vj_6;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MovieListInteractor extends AsyncTask<String, Integer, Void> implements IMovieListInteractor {
    private String tmdb_api_key="764b519aaf6c76992c258c5441a3de09";
    ArrayList<Movie> movies;
    private OnMoviesSearchDone caller;

    public MovieListInteractor(OnMoviesSearchDone p) {
        caller = p;
        movies = new ArrayList<Movie>();
    };


    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

    @Override
    protected Void doInBackground(String... strings) {
        String query = null;
        try {
            query = URLEncoder.encode(strings[0], "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url1 = "https://api.themoviedb.org/3/search/movie?api_key="+tmdb_api_key+"&query=" + query;
        try {
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = convertStreamToString(in);
            JSONObject jo = new JSONObject(result);
            JSONArray results = jo.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);
                String title = movie.getString("title");
                Integer id = movie.getInt("id");
                String posterPath = movie.getString("poster_path");
                String overview = movie.getString("overview");
                String releaseDate = movie.getString("release_date");
                movies.add(new Movie(id,title,overview,releaseDate,posterPath));
                if (i==4) break;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        caller.onDone(movies);
    }


    public interface OnMoviesSearchDone{
        public void onDone(ArrayList<Movie> results);
    }
/*
    @Override
    public ArrayList<Movie> get() {
        return MoviesModel.movies;
    }

    @Override
    public void addMovie(Movie m) {
        MoviesModel.movies.add(m);
    }*/
}

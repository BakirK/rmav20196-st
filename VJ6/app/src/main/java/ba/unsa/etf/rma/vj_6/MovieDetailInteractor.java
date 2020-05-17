package ba.unsa.etf.rma.vj_6;

import android.os.AsyncTask;
import android.util.Log;

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

public class MovieDetailInteractor extends AsyncTask<String, Integer, Void> implements IMovieListInteractor {
    private String tmdb_api_key="764b519aaf6c76992c258c5441a3de09";
    Movie movie;
    private OnMovieSearchDone caller;

    public MovieDetailInteractor(OnMovieSearchDone p) {
        caller = p;
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
        Log.d("lalala", "lalala");
        String query = null;
        try {
            query = URLEncoder.encode(strings[0], "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url1 = "https://api.themoviedb.org/3/movie/"+query+"?api_key="+tmdb_api_key;
        try {
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = convertStreamToString(in);
            JSONObject jsonObject = new JSONObject(result);
            String title = jsonObject.getString("title");
            Integer id = jsonObject.getInt("id");
            String posterPath = jsonObject.getString("poster_path");
            String overview = jsonObject.getString("overview");
            String releaseDate = jsonObject.getString("release_date");
            String homepage = jsonObject.getString("homepage");
            String genre = jsonObject.getJSONArray("genres").getJSONObject(0).getString("name");
            movie = new Movie(title,overview,releaseDate,homepage,posterPath,id,genre);
            Log.d("movieObjekat", jsonObject.toString());
            AddCast(query);
            AddSimilarMovies(query);
        } catch (MalformedURLException e) {
            Log.d("ovdje1", "doInBackground");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("ovdje2", "doInBackground");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.d("ovdje3", "doInBackground");
            e.printStackTrace();
        }
        return null;
    }

    protected Void AddCast(String... params)
    {
        try {
            String  query = URLEncoder.encode(params[0], "utf-8");

            URL url = null;
            String url1 = "https://api.themoviedb.org/3/movie/"+query+"/credits?api_key="+tmdb_api_key;
            try {
                url = new URL(url1);
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String rezultat = convertStreamToString(in);
            JSONObject jo = new JSONObject(rezultat);
            JSONArray items = jo.getJSONArray("cast");
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < items.length(); i++) {
                JSONObject song = items.getJSONObject(i);
                String name = song.getString("name");
                list.add(name);
                if (i==4) break;
            }
            movie.setActors(list);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Void AddSimilarMovies(String... params)
    {
        try {
            String  query = URLEncoder.encode(params[0], "utf-8");

            URL url = null;
            String url1 = "https://api.themoviedb.org/3/movie/"+query+"/similar?api_key="+tmdb_api_key;
            try {
                url = new URL(url1);
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String rezultat = convertStreamToString(in);
            JSONObject jo = new JSONObject(rezultat);
            JSONArray items = jo.getJSONArray("results");
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < items.length(); i++) {
                JSONObject slicni= items.getJSONObject(i);
                String title = slicni.getString("title");
                list.add(title);
                if (i==4) break;
            }
            movie.setSimilarMovies(list);
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
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        caller.onDone(movie);
    }

    public interface OnMovieSearchDone{
        void onDone(Movie result);
    }
}

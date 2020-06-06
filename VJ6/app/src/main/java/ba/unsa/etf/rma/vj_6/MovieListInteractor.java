package ba.unsa.etf.rma.vj_6;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;

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

public class MovieListInteractor extends IntentService implements IMovieListInteractor {
    final public static int STATUS_RUNNING=0;
    final public static int STATUS_FINISHED=1;
    final public static int STATUS_ERROR=2;
    //private String tmdb_api_key= BuildConfig.API_KEY;
    private String tmdb_api_key="764b519aaf6c76992c258c5441a3de09";
    ArrayList<Movie> movies;
    private MovieDBOpenHelper movieDBOpenHelper;
    SQLiteDatabase database;
    private Movie movie;

    public MovieListInteractor() {
        super(null);
    };
    public MovieListInteractor(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle bundle = new Bundle();
        receiver.send(STATUS_RUNNING, Bundle.EMPTY);
        String params = intent.getStringExtra("query");
        String query = null;
        try {
            query = URLEncoder.encode(params, "utf-8");
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
            movies=new ArrayList<>();
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
        }  catch (ClassCastException e) {
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(STATUS_ERROR, bundle);
            return;
        } catch (MalformedURLException e) {
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(STATUS_ERROR, bundle);
            return;
        } catch (IOException e) {
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(STATUS_ERROR, bundle);
            return;
        } catch (JSONException e) {
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(STATUS_ERROR, bundle);
            return;
        }
        bundle.putParcelableArrayList("result", movies);
        receiver.send(STATUS_FINISHED, bundle);
    }

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
    public ArrayList<Movie> getMovies(Context context) {
        movies = new ArrayList<>();
        movieDBOpenHelper = new MovieDBOpenHelper(context);
        database = movieDBOpenHelper.getWritableDatabase();
        String query = "SELECT *"  + " FROM "
                + MovieDBOpenHelper.MOVIE_TABLE +" ORDER BY " +MovieDBOpenHelper.MOVIE_INTERNAL_ID + " DESC LIMIT 5";
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst()) {
            do{
                int idPos = cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_ID);
                int titlePos = cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_TITLE);
                int genrePos = cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_GENRE);
                int homepagePos = cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_HOMEPAGE);
                int posterPos = cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_POSTERPATH);
                int overviewPos = cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_OVERVIEW);
                int releasePos = cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_RELEASEDATE);
                movie = new Movie(cursor.getString(titlePos), cursor.getString(overviewPos),
                        cursor.getString(releasePos),cursor.getString(homepagePos), cursor.getString(posterPos),
                        cursor.getInt(idPos), cursor.getString(genrePos));
                Details();
                movies.add(movie);
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return movies;
    }

    public void Details(){

        String query;
        query = "SELECT " + MovieDBOpenHelper.CAST_NAME + " FROM " +
                MovieDBOpenHelper.CAST_TABLE + " WHERE "+
                MovieDBOpenHelper.CAST_MOVIE_ID + " = ?";

        Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(movie.getId())});
        ArrayList<String> cast = new ArrayList<String>();

        if(cursor.moveToFirst()) {
            do {
                int pos = cursor.getColumnIndexOrThrow(MovieDBOpenHelper.CAST_NAME);
                cast.add(cursor.getString(pos));
            } while (cursor.moveToNext());
        }
        movie.setActors(cast);
        cursor.close();

        query = "SELECT " + MovieDBOpenHelper.SMOVIE_TITLE + " FROM "
                + MovieDBOpenHelper.SIMILIAR_MOVIES + " WHERE "
                + MovieDBOpenHelper.SMOVIES_MOVIE_ID + " = ?";

        Cursor cursor1 = database.rawQuery(query, new String[]{Integer.toString(movie.getId())});
        ArrayList<String> similar = new ArrayList<String>();

        if(cursor1.moveToFirst()) {
            do {
                int pos = cursor1.getColumnIndexOrThrow(MovieDBOpenHelper.SMOVIE_TITLE);
                similar.add(cursor1.getString(pos));
            } while (cursor1.moveToNext());
        }
        movie.setSimilarMovies(similar);
        cursor1.close();
    }
}

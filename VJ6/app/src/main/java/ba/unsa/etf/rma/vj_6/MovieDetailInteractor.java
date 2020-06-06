package ba.unsa.etf.rma.vj_6;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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

public class MovieDetailInteractor extends IntentService implements IMovieDetailInteractor {
    final public static int STATUS_RUNNING=0;
    final public static int STATUS_FINISHED=1;
    final public static int STATUS_ERROR=2;
    private MovieDBOpenHelper movieDBOpenHelper;
    SQLiteDatabase database;
    private String tmdb_api_key="764b519aaf6c76992c258c5441a3de09";
    Movie movie;

    public MovieDetailInteractor() {
        super(null);
    };
    public MovieDetailInteractor(String name) {
        super(name);
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
            AddCast(query);
            AddSimilarMovies(query);
        } catch (ClassCastException e) {
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
        bundle.putParcelable("result", movie);
        receiver.send(STATUS_FINISHED, bundle);
    }

    @Override
    public void save(Movie movie, Context context) {
        //movieDBOpenHelper = new MovieDBOpenHelper(context);
        //database = movieDBOpenHelper.getWritableDatabase();
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Uri moviesURI = Uri.parse("content://rma.provider.movies/elements");
        Uri castURI = Uri.parse("content://rma.provider.cast/elements");
        Uri similarURI = Uri.parse("content://rma.provider.similar/elements");
        ContentValues values = new ContentValues();
        values.put(MovieDBOpenHelper.MOVIE_ID, movie.getId());
        values.put(MovieDBOpenHelper.MOVIE_TITLE, movie.getTitle());
        values.put(MovieDBOpenHelper.MOVIE_GENRE, movie.getGenre());
        values.put(MovieDBOpenHelper.MOVIE_HOMEPAGE, movie.getHomepage());
        values.put(MovieDBOpenHelper.MOVIE_OVERVIEW, movie.getOverview());
        values.put(MovieDBOpenHelper.MOVIE_POSTERPATH, movie.getPosterPath());
        values.put(MovieDBOpenHelper.MOVIE_RELEASEDATE, movie.getReleaseDate());
        cr.insert(moviesURI,values);
        //database.insert(MovieDBOpenHelper.MOVIE_TABLE, null, values);

        for (int i = 0; i < movie.getActors().size(); i++){
            String name = movie.getActors().get(i);
            ContentValues cast = new ContentValues();
            cast.put(MovieDBOpenHelper.CAST_NAME,name);
            cast.put(MovieDBOpenHelper.CAST_MOVIE_ID, movie.getId());
            cr.insert(castURI,cast);
            //database.insert(MovieDBOpenHelper.CAST_TABLE, null, cast);
        }

        for (int i = 0; i < movie.getSimilarMovies().size(); i++){
            String title = movie.getSimilarMovies().get(i);
            ContentValues similar = new ContentValues();
            similar.put(MovieDBOpenHelper.SMOVIE_TITLE,title);
            similar.put(MovieDBOpenHelper.SMOVIES_MOVIE_ID, movie.getId());
            cr.insert(similarURI,similar);
            //database.insert(MovieDBOpenHelper.SIMILIAR_MOVIES, null, similar);
        }
    }


    @Override
    public Movie getMovie(Context context, Integer id) {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        String[] kolone = null;
        Uri adresa = ContentUris.withAppendedId(Uri.parse("content://rma.provider.movies/elements"),id);
        String where = null;
        String whereArgs[] = null;
        String order = null;
        Cursor cursor = cr.query(adresa,kolone,where,whereArgs,order);
        if (cursor != null){
            cursor.moveToFirst();
            int idPos = cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_ID);
            int internalId = cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_INTERNAL_ID);
            int titlePos = cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_TITLE);
            int genrePos = cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_GENRE);
            int homepagePos = cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_HOMEPAGE);
            int posterPos = cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_POSTERPATH);
            int overviewPos = cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_OVERVIEW);
            int releasePos = cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_RELEASEDATE);
            movie = new Movie(cursor.getString(titlePos), cursor.getString(overviewPos),
                    cursor.getString(releasePos), cursor.getString(homepagePos), cursor.getString(posterPos),
                    cursor.getInt(idPos), cursor.getString(genrePos),cursor.getInt(internalId));
        }
        cursor.close();
        return movie;
    }


    @Override
    public Cursor getCastCursor(Context context, int id) {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        String[] kolone = new String[]{
                MovieDBOpenHelper.CAST_NAME,
                MovieDBOpenHelper.CAST_ID
        };
        Uri adresa = ContentUris.withAppendedId(Uri.parse("content://rma.provider.cast/elements"), id);
        String where = null;
        String whereArgs[] = null;
        String order = null;
        Cursor cur = cr.query(adresa, kolone, where, whereArgs, order);
        return cur;
    }

    @Override
    public Cursor getSimilarCursor(Context context, int id) {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        String[] kolone = new String[]{
                MovieDBOpenHelper.SMOVIES_ID,
                MovieDBOpenHelper.SMOVIE_TITLE
        };
        Uri adresa = ContentUris.withAppendedId(Uri.parse("content://rma.provider.similar/elements"), id);
        String where = null;
        String whereArgs[] = null;
        String order = null;
        Cursor cur = cr.query(adresa, kolone, where, whereArgs, order);
        return cur;
    }
}

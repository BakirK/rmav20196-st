package ba.unsa.etf.rma.vj_6;

import android.os.Parcelable;

import java.util.ArrayList;

public interface IMovieDetailPresenter {
    //Movie getMovieByTitle(String title);
    Movie getMovie();
    void setMovie(Parcelable movie);
    void searchMovie(String query);
    void create(String title, String overview, String releaseDate, String genre, String homepage, ArrayList<String> actors);
}

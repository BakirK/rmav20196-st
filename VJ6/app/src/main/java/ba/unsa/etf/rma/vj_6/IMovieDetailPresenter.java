package ba.unsa.etf.rma.vj_6;

import android.os.Parcelable;

public interface IMovieDetailPresenter {
    Movie getMovieByTitle(String title);
    void addMovie(Movie m);
    Movie getMovie();
    void setMovie(Parcelable movie);
}

package ba.unsa.etf.rma.vj_6;

import android.os.Parcelable;


public interface IMovieDetailPresenter {
    Movie getMovie();
    void setMovie(Parcelable movie);
    void searchMovie(String query);
}

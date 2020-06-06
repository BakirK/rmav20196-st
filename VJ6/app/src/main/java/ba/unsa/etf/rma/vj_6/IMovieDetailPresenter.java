package ba.unsa.etf.rma.vj_6;

import android.database.Cursor;
import android.os.Parcelable;


public interface IMovieDetailPresenter {
    Movie getMovie();
    void setMovie(Parcelable movie);
    void searchMovie(String query);
    void getDatabaseMovie(int id);
    Cursor getCastCursor(int id);
    Cursor getSimilarCursor(int id);
}

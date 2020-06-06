package ba.unsa.etf.rma.vj_6;

import android.database.Cursor;

import java.util.ArrayList;

interface IMovieListView {
    void setMovies(ArrayList<Movie> movies);
    void notifyMovieListDataSetChanged();
    void showToast(String text);
    void setCursor(Cursor cursor);
}

package ba.unsa.etf.rma.vj_6;

import android.content.Context;
import android.database.Cursor;

public interface IMovieDetailInteractor {
    public void save(Movie movie, Context context);
    Movie getMovie(Context context, Integer id);
    Cursor getCastCursor(Context context, int id);
    Cursor getSimilarCursor(Context context, int id);
}

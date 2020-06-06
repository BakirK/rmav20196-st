package ba.unsa.etf.rma.vj_6;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public interface IMovieListInteractor {
    Cursor getMovieCursor(Context context);
}

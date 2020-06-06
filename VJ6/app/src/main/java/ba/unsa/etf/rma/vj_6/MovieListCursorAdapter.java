package ba.unsa.etf.rma.vj_6;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MovieListCursorAdapter extends ResourceCursorAdapter {

    public TextView titleView;
    public TextView genreView;
    public ImageView imageView;
    private String posterPath="https://image.tmdb.org/t/p/w342";

    public MovieListCursorAdapter(Context context, int layout, Cursor c, boolean autoRequery) {
        super(context, layout, c, autoRequery);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        titleView = view.findViewById(R.id.title);
        genreView = view.findViewById(R.id.genre);
        imageView = view.findViewById(R.id.icon);
        titleView.setText(cursor.getString(cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_TITLE)));
        genreView.setText(cursor.getString(cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_RELEASEDATE)));
        Glide.with(context)
                .load(posterPath+cursor.getString(cursor.getColumnIndexOrThrow(MovieDBOpenHelper.MOVIE_POSTERPATH)))
                .centerCrop()
                .placeholder(R.drawable.cam)
                .error(R.drawable.cam)
                .fallback(R.drawable.cam)
                .into(imageView);
    }
}

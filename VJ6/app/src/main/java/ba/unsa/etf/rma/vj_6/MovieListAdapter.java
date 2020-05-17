package ba.unsa.etf.rma.vj_6;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MovieListAdapter extends ArrayAdapter<Movie> {
    private int resource;
    public TextView titleView;
    public TextView genreView;
    public ImageView imageView;
    private String posterPath="https://image.tmdb.org/t/p/w342";
    public MovieListAdapter(@NonNull Context context, int resource, @NonNull List<Movie> items) {
        super(context, resource, items);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        // Kreiranje i inflate-anje view klase
        LinearLayout newView;
        if (convertView == null) {
            // Ukoliko je ovo prvi put da se pristupa klasi convertView,
            //odnosno nije upadate
            // Potrebno je kreirati novi objekat i inflate-ati ga
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(resource, newView, true);
        } else {
            // Ukoliko je update potrebno je samo
            //izmjeniti vrijednosti polja
            newView = (LinearLayout)convertView;
        }
        Movie movie = getItem(position);
        // Ovdje mozete dohvatiti reference na View
        // i popuniti ga sa vrijednostima polja iz objekta
        titleView = newView.findViewById(R.id.title);
        genreView = newView.findViewById(R.id.genre);
        titleView.setText(movie.getTitle());
        genreView.setText(movie.getGenre());


        imageView = newView.findViewById(R.id.icon);
        /*String genreMatch = movie.getGenre();
        try {
            Class res = R.drawable.class;
            Field field = res.getField(genreMatch);
            int drawableId = field.getInt(null);
            imageView.setImageResource(drawableId);
        }
        catch (Exception e) {
            imageView.setImageResource(R.drawable.cam);
        }*/
        Glide.with(getContext())
                .load(posterPath+movie.getPosterPath())
                .centerCrop()
                .placeholder(R.drawable.cam)
                .error(R.drawable.cam)
                .fallback(R.drawable.cam)
                .into(imageView);

        return newView;

    }

    public void setMovies(ArrayList<Movie> movies) {
        this.addAll(movies);
    }

    public Movie getMovie(int position) {
            return super.getItem(position);
    }
}

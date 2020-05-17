package ba.unsa.etf.rma.vj_6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MovieListFragment extends Fragment implements IMovieListView {
    private OnItemClick onItemClick;
    private ListView listView;
    private EditText editText;
    private MovieListAdapter movieListAdapter;
    private IMovieListPresenter movieListPresenter;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_list, container, false);
        movieListAdapter = new MovieListAdapter(getActivity(), R.layout.list_element, new ArrayList<Movie>());
        listView = (ListView) fragmentView.findViewById(R.id.listView);
        listView.setAdapter(movieListAdapter);
        //getPresenter().refreshMovies();
        //listView.setOnItemClickListener(listItemClickListener);
        editText = (EditText) fragmentView.findViewById(R.id.editText);

        //napuni search bar - maznuto od mikija
        if (getActivity().getIntent().getAction().equals(Intent.ACTION_SEND)) {
            editText.setText(getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT));
        }
            try {
            //u sljede´coj liniji dohvatamo referencu na roditeljsku aktivnost
            //kako ona implementira interfejs OnItemClick
            //moguce ju je castati u taj interfejs
            onItemClick = (OnItemClick)getActivity();
        } catch (ClassCastException e) {
            //u slucaju da se u roditeljskoj aktivnosti nije implementirao interfejs
            //baca se izuzetak
            throw new ClassCastException(getActivity().toString() + "Treba implementirati OnItemClick");
        }
        //ukoliko je aktivnost uspjesno cast-ana u interfejs
        //tada njoj prosljedujemo event
        listView.setOnItemClickListener(listItemClickListener);
        button = fragmentView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          getPresenter().searchMovies(editText.getText().toString());
                                      }
                                    }
        );
            return fragmentView;
            //return inflater.inflate(R.layout.fragment_list, container, false);

    }

    @Override
    public void setMovies(ArrayList<Movie> movies) {
        movieListAdapter.setMovies(movies);
    }

    @Override
    public void notifyMovieListDataSetChanged() {
        movieListAdapter.notifyDataSetChanged();
    }

    public interface OnItemClick {
        void onItemClicked(Movie movie);
    }
    private AdapterView.OnItemClickListener listItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    /* old listener
                    Intent movieDetailIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
                    Movie movie = adapter.getMovie(position);
                    //movieDetailIntent.putExtra("title", movie.getTitle());
                    movieDetailIntent.putExtra("title", movie.getTitle());
                    MainActivity.this.startActivity(movieDetailIntent);
                    */
                    Movie movie = movieListAdapter.getMovie(position);
                    onItemClick.onItemClicked(movie);
                }
            };

    public IMovieListPresenter getPresenter() {
        if (movieListPresenter == null) {
            movieListPresenter = new MovieListPresenter(this, getActivity());
        }
        return movieListPresenter;
    }
}

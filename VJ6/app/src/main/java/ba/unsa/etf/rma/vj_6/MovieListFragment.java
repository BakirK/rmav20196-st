package ba.unsa.etf.rma.vj_6;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MovieListFragment extends Fragment implements IMovieListView {
    private OnItemClick onItemClick;
    private ListView listView;
    private EditText editText;
    private MovieListAdapter movieListAdapter;
    private MovieListCursorAdapter movieListCursorAdapter;
    private IMovieListPresenter movieListPresenter;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_list, container, false);
        movieListAdapter=new MovieListAdapter(getActivity(), R.layout.list_element, new ArrayList<Movie>());
        movieListCursorAdapter= new MovieListCursorAdapter(getActivity(), R.layout.list_element,null,false);
        listView= fragmentView.findViewById(R.id.listView);
        listView.setAdapter(movieListCursorAdapter);
        listView.setOnItemClickListener(listCursorItemClickListener);
        editText = fragmentView.findViewById(R.id.editText);
        onItemClick= (OnItemClick) getActivity();
        getPresenter().getMoviesCursor();
        Intent intent = getActivity().getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    editText.setText(sharedText);
                }
            }
        }
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
        listView.setAdapter(movieListAdapter);
        listView.setOnItemClickListener(listItemClickListener);
        movieListAdapter.setMovies(movies);
    }

    @Override
    public void notifyMovieListDataSetChanged() {
        movieListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    public interface OnItemClick {
        void onItemClicked(Boolean inDatabase, int id);
    }
    private AdapterView.OnItemClickListener listItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie movie = movieListAdapter.getMovie(position);
                    onItemClick.onItemClicked(false,movie.getId());
                }
            };

    private AdapterView.OnItemClickListener listCursorItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            if(cursor != null) {
                onItemClick.onItemClicked(true, cursor.getInt(cursor.getColumnIndex(MovieDBOpenHelper.MOVIE_INTERNAL_ID)));
            }
        }
    };

    public IMovieListPresenter getPresenter() {
        if (movieListPresenter == null) {
            movieListPresenter = new MovieListPresenter(this, getActivity());
        }
        return movieListPresenter;
    }

    @Override
    public void setCursor(Cursor cursor) {
        listView.setAdapter(movieListCursorAdapter);
        listView.setOnItemClickListener(listCursorItemClickListener);
        movieListCursorAdapter.changeCursor(cursor);
    }
}

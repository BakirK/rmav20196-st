package ba.unsa.etf.rma.vj_6;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SimilarFragment extends Fragment {
    private IMovieDetailPresenter presenter;

    public IMovieDetailPresenter getPresenter() {
        if (presenter == null) {
            presenter = new MovieDetailPresenter(getActivity());
        }
        return presenter;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View iv = inflater.inflate(R.layout.fragment_cast, container, false);
        ListView listView = iv.findViewById(R.id.cast_list);
        if (getArguments().containsKey("similar")) {
            ArrayList<String> cast = getArguments().getStringArrayList("similar");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cast);
            listView.setAdapter(adapter);
        }
        else {
            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    getPresenter().getSimilarCursor(getArguments().getInt("id")),
                    new String[] {MovieDBOpenHelper.SMOVIE_TITLE},
                    new int[] {android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            listView.setAdapter(simpleCursorAdapter);
        }
        return iv;
    }
}

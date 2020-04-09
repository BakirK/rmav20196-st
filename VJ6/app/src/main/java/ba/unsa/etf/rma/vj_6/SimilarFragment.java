package ba.unsa.etf.rma.vj_6;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SimilarFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View iv = inflater.inflate(R.layout.fragment_cast, container, false);
        ListView listView = iv.findViewById(R.id.cast_list);
        ArrayList<String> similar = getArguments().getStringArrayList("similar");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, similar);
        listView.setAdapter(adapter);
        return iv;
    }
}

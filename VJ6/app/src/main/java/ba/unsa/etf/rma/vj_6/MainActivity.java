package ba.unsa.etf.rma.vj_6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IMovieListView {
    String msg = "Android log poruka : ";
    Button button;
    EditText editText;
    ListView listView;
    //ArrayList<String> entries;
    private IMovieListPresenter presenter;
    private MovieListAdapter adapter;

    public IMovieListPresenter getPresenter() {
        if(presenter == null) {
            presenter = new MovieListPresenter(this, this);
        }
        return presenter;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(msg, "onCreate() metoda");
        adapter = new MovieListAdapter(getApplicationContext(), R.layout.list_element, new ArrayList<Movie>());
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        getPresenter().refreshMovies();
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "dabadabadab");
                sendIntent.setType("text/plain");
                // Provjera da li postoji aplikacija koja moˇze obaviti navedenu akciju
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendIntent);
                }


            }
        });

        /*button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);
        entries = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.list_element, R.id.itemName, entries);

        //povezivanje adaptera sa listom
        listView.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entries.add(0, editText.getText().toString());
                adapter.notifyDataSetChanged();
                editText.setText("");
            }

        });
        */

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(msg, "onStart() metoda");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "onResume() metoda");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "onPause() metoda");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "onStop() metoda");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(msg, "onDestroy() metoda");
    }

    @Override
    public void setMovies(ArrayList<Movie> movies) {
        adapter.setMovies(movies);
    }

    @Override
    public void notifyMovieListDataSetChanged() {
        adapter.notifyDataSetChanged();
    }
}

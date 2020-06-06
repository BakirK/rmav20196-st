package ba.unsa.etf.rma.vj_6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieListFragment.OnItemClick {
    String msg = "Android log poruka : ";

    //ArrayList<String> entries;
    private MovieListAdapter adapter;
    private MovieBroadcastReceiver receiver;
    // twoPaneMode je privatni atribut klase Pocetni koji je tipa boolean
    // ovu variablu ´cemo koristiti da znamo o kojem layoutu se radi
    // ako je twoPaneMode true tada se radi o ˇsirem layoutu (dva fragmenta)
    // ako je twoPaneMode false tada se radi o poˇcetnom layoutu (jedan fragment)
    private boolean twoPaneMode;



    private AdapterView.OnItemClickListener listItemClickListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //dohvatanje FragmentManager-a
        FragmentManager fragmentManager = getSupportFragmentManager();
        FrameLayout details = findViewById(R.id.movie_detail);
        //slucaj layouta za ˇsiroke ekrane
        if (details != null) {
            twoPaneMode = true;
            MovieDetailFragment detailFragment = (MovieDetailFragment)
                    fragmentManager.findFragmentById(R.id.movie_detail);
        //provjerimo da li je fragment detalji ve´c kreiran
            if (detailFragment==null) {
        //kreiramo novi fragment FragmentDetalji ukoliko ve´c nije kreiran
                detailFragment = new MovieDetailFragment();
                fragmentManager.beginTransaction().replace(R.id.movie_detail, detailFragment).commit();
            }
        } else {
            twoPaneMode = false;
        }
        //Dodjeljivanje fragmenta MovieListFragment
        Fragment listFragment =
                fragmentManager.findFragmentByTag("list");
        //provjerimo da li je vec kreiran navedeni fragment
        if (listFragment==null){
        //ukoliko nije, kreiramo
            listFragment = new MovieListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.movies_list,listFragment,"list")
                    .commit();
        } else {
        //slucaj kada mijenjamo orijentaciju uredaja
        //iz portrait (uspravna) u landscape (vodoravna)
        //a u aktivnosti je bio otvoren fragment MovieDetailFragment
        //tada je potrebno skinuti MovieDetailFragment sa steka
        //kako ne bi bio dodan na mjesto fragmenta MovieListFragment
            fragmentManager.popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
/*
        receiver = new MovieBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_SERVICE);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, intentFilter);*/



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
    public void onItemClicked(Boolean inDatabase, int id) {
        //Priprema novog fragmenta FragmentDetalji
        Bundle arguments = new Bundle();
        if (!inDatabase)
            arguments.putInt("id", id);
        else
            arguments.putInt("internal_id",id);
        MovieDetailFragment detailFragment = new MovieDetailFragment();
        detailFragment.setArguments(arguments);
        if (twoPaneMode){
            //Slucaj za ekrane sa sirom dijagonalom
            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail, detailFragment).commit();
        }
        else {
            //Slucaj za ekrane sa pocetno zadanom sirinom
            getSupportFragmentManager().beginTransaction().replace(R.id.movies_list,detailFragment).addToBackStack(null).commit();
        }
    }
}

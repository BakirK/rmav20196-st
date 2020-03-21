package ba.unsa.etf.rma.vj_6;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;

public class MovieDetailActivity extends AppCompatActivity implements IMovieDetailView {
    private ImageView icon;
    private TextView title;
    private TextView overview;
    private TextView genre;
    private TextView homepage;
    private TextView releaseDate;
    private Movie movie;
    private IMovieDetailPresenter presenter;
    private Button shareButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movie = getPresenter().getMovie();

        icon = (ImageView) findViewById(R.id.icon);
        String genreMatch = movie.getGenre();
        try {
            Class res = R.drawable.class;
            Field field = res.getField(genreMatch);
            int drawableId = field.getInt(null);
            icon.setImageResource(drawableId);
        }
        catch (Exception e) {
            icon.setImageResource(R.drawable.picture1);
        }

        title = (TextView) findViewById(R.id.title);
        title.setText(movie.getTitle());

        overview = (TextView) findViewById(R.id.overview);
        overview.setText(movie.getOverview());

        genre = (TextView) findViewById(R.id.genre);
        genre.setText(movie.getGenre());

        homepage = (TextView) findViewById(R.id.homepage);
        homepage.setText(movie.getHomepage());

        releaseDate = (TextView) findViewById(R.id.releaseDate);
        releaseDate.setText(movie.getReleaseDate());



        AdapterView.OnClickListener pageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = movie.getHomepage();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        };


        homepage.setOnClickListener(pageClickListener);

        shareButton = (Button) findViewById(R.id.share);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, movie.getOverview());
                sendIntent.setType("text/plain");
                // Provjera da li postoji aplikacija koja moze obaviti navedenu akciju
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendIntent);
                }
            }
        });

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.youtube.com/results?search_query=" + movie.getTitle();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                MovieDetailActivity.this.startActivity(intent);
            }
        });




    }

    public IMovieDetailPresenter getPresenter() {
        if(presenter == null) {
            presenter = new MovieDetailPresenter(getIntent().getStringExtra("title"), this);
        }
        return presenter;
    }
}

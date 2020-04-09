package ba.unsa.etf.rma.vj_6;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import java.lang.reflect.Field;

public class MovieDetailFragment extends Fragment {
    private ImageView icon;
    private TextView title;
    private TextView overview;
    private TextView genre;
    private TextView homepage;
    private TextView releaseDate;
    private Movie movie;
    private IMovieDetailPresenter presenter;
    private Button shareButton;
    private ToggleButton toggleButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        movie = getPresenter().getMovie();
        if (getArguments() != null && getArguments().containsKey("movie")) {
            getPresenter().setMovie(getArguments().getParcelable("movie"));
            title = view.findViewById(R.id.title);
            Movie movie = getPresenter().getMovie();
            title.setText(movie.getTitle());

            icon = (ImageView) view.findViewById(R.id.icon);
            String genreMatch = movie.getGenre();
            try {
                Class res = R.drawable.class;
                Field field = res.getField(genreMatch);
                int drawableId = field.getInt(null);
                icon.setImageResource(drawableId);
            }
            catch (Exception e) {
                icon.setImageResource(R.drawable.cam);
            }
            overview = (TextView) view.findViewById(R.id.overview);
            overview.setText(movie.getOverview());

            genre = (TextView) view.findViewById(R.id.genre);
            genre.setText(movie.getGenre());

            homepage = (TextView) view.findViewById(R.id.homepage);
            homepage.setText(movie.getHomepage());

            releaseDate = (TextView) view.findViewById(R.id.releaseDate);
            releaseDate.setText(movie.getReleaseDate());

            AdapterView.OnClickListener homepageClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = getPresenter().getMovie().getHomepage();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(i);
                    }
                }
            };


            homepage.setOnClickListener(homepageClickListener);

            shareButton = (Button) view.findViewById(R.id.share);

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, getPresenter().getMovie().getOverview());
                    sendIntent.setType("text/plain");
                    // Provjera da li postoji aplikacija koja moze obaviti navedenu akciju
                    if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(sendIntent);
                    }
                }
            });

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEARCH);
                    intent.setPackage("com.google.android.youtube");
                    intent.putExtra("query", getPresenter().getMovie().getTitle() + " trailer");
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });


            toggleButton = view.findViewById(R.id.toggle_button);
            toggleButton.setOnCheckedChangeListener(toggleOnCheckedChangeListener);

            //init cast list
            Bundle arguments = new Bundle();
            arguments.putStringArrayList("cast", movie.getActors());
            CastFragment castFragment = new CastFragment();
            castFragment.setArguments(arguments);
            getChildFragmentManager().beginTransaction().add(R.id.frame, castFragment).commit();
        }
        return view;
    }

    public IMovieDetailPresenter getPresenter() {
        if(presenter == null) {
            presenter = new MovieDetailPresenter(getActivity().getIntent().getStringExtra("title"), getActivity());
        }
        return presenter;
    }

    private CompoundButton.OnCheckedChangeListener toggleOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                Bundle arguments = new Bundle();
                arguments.putStringArrayList("similar", getPresenter().getMovie().getSimilarMovies());
                SimilarFragment similarFragment = new SimilarFragment();
                similarFragment.setArguments(arguments);
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.frame, similarFragment).commit();
            } else {
                Bundle arguments = new Bundle();
                arguments.putStringArrayList("cast", getPresenter().getMovie().getActors());
                CastFragment castFragment = new CastFragment();
                castFragment.setArguments(arguments);
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.frame, castFragment).commit();
            }
        }
    };
}


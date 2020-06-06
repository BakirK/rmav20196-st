package ba.unsa.etf.rma.vj_6;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;

public class MovieDetailFragment extends Fragment implements IMovieDetailView {

    private TextView title;
    private TextView genre;
    private TextView overview;
    private TextView releaseDate;
    private TextView homepage;
    private ListView actors;
    private Button share;
    private ToggleButton toggleButton;
    private ImageView imageView;
    private String posterPath="https://image.tmdb.org/t/p/w342";

    private IMovieDetailPresenter presenter;

    public IMovieDetailPresenter getPresenter() {
        if (presenter == null) {
            presenter = new MovieDetailPresenter(this,getActivity());
        }
        return presenter;
    }
    private View.OnClickListener homepageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = getPresenter().getMovie().getHomepage();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    };

    private View.OnClickListener titleOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_SEARCH);
            intent.setPackage("com.google.android.youtube");
            intent.putExtra("query", getPresenter().getMovie().getTitle() + " trailer");
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    };

    private View.OnClickListener shareOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, getPresenter().getMovie().getOverview());
            intent.setType("text/plain");
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    };

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
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        Log.d("onCreateView", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        title = view.findViewById(R.id.title);
        genre = view.findViewById(R.id.genre);
        overview = view.findViewById(R.id.overview);
        releaseDate = view.findViewById(R.id.releaseDate);
        homepage = view.findViewById(R.id.homepage);
        share = view.findViewById(R.id.share);
        toggleButton = view.findViewById(R.id.toggle_button);
        imageView = view.findViewById(R.id.icon);
        homepage.setOnClickListener(homepageOnClickListener);
        title.setOnClickListener(titleOnClickListener);
        share.setOnClickListener(shareOnClickListener);
        toggleButton.setOnCheckedChangeListener(toggleOnCheckedChangeListener);
        if (getArguments() != null && getArguments().containsKey("id")) {
            int id = getArguments().getInt("id");
            getPresenter().searchMovie(String.valueOf(id));
        }
        if (getArguments() != null && getArguments().containsKey("movie")) {
            Movie movie = getArguments().getParcelable("movie");
            getPresenter().setMovie(movie);
            refreshView();
        }
        return  view;
    }

    @Override
    public void refreshView() {
        Log.d("refresh", "refreshView");
        Movie movie = getPresenter().getMovie();
        title.setText(movie.getTitle());
        genre.setText(movie.getGenre());
        overview.setText(movie.getOverview());
        releaseDate.setText(movie.getReleaseDate());
        homepage.setText(movie.getHomepage());
        Glide.with(getContext())
                .load(posterPath+movie.getPosterPath())
                .centerCrop()
                .placeholder(R.drawable.cam)
                .error(R.drawable.cam)
                .fallback(R.drawable.cam)
                .into(imageView);
        Bundle arguments = new Bundle();
        arguments.putStringArrayList("cast", movie.getActors());
        CastFragment castFragment = new CastFragment();
        castFragment.setArguments(arguments);
        getChildFragmentManager().beginTransaction()
                .add(R.id.frame, castFragment)
                .commit();

    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

}

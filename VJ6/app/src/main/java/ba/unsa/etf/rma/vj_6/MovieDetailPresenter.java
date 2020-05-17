package ba.unsa.etf.rma.vj_6;

import android.content.Context;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

public class MovieDetailPresenter implements IMovieDetailPresenter, MovieDetailInteractor.OnMovieSearchDone {
    //private IMovieListInteractor interactor;
    private Context context;
    private IMovieDetailView view;
    private Movie movie;


    /*public MovieDetailPresenter(String title, Context context) {
        this.interactor = new MovieListInteractor();
        this.context = context;
        this.movie = getMovieByTitle(title);
    }*/

    public MovieDetailPresenter(IMovieDetailView view, Context context) {
        this.view       = view;
        this.context    = context;
    }

    /*@Override
    public Movie getMovieByTitle(String title) {
        ArrayList<Movie> movies = interactor.get();
        for (Movie m : movies) {
            if (m.getTitle().equals(title)) {
                return m;
            }
        }
        return null;
    }*/

    @Override
    public Movie getMovie() {
        return movie;
    }
    @Override
    public void setMovie(Parcelable movie) {
        this.movie = (Movie)movie;
    }

    @Override
    public void searchMovie(String query) {
        new MovieDetailInteractor((MovieDetailInteractor.OnMovieSearchDone) this).execute(query);
    }

    @Override
    public void create(String title, String overview, String releaseDate, String genre, String homepage, ArrayList<String> actors) {
        this.movie = new Movie(title,overview,releaseDate,homepage,genre,actors);
    }

    @Override
    public void onDone(Movie result) {
        movie = result;
        Log.d("res", result.toString());
        view.refreshView();
    }
}

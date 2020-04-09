package ba.unsa.etf.rma.vj_6;

import android.content.Context;
import android.os.Parcelable;

import java.util.ArrayList;

public class MovieDetailPresenter implements IMovieDetailPresenter {
    private IMovieListInteractor interactor;
    private Movie movie;
    private Context context;



    public MovieDetailPresenter(String title, Context context) {
        this.interactor = new MovieListInteractor();
        this.context = context;
        this.movie = getMovieByTitle(title);
    }


    @Override
    public Movie getMovieByTitle(String title) {
        ArrayList<Movie> movies = interactor.get();
        for (Movie m : movies) {
            if (m.getTitle().equals(title)) {
                return m;
            }
        }
        return null;
    }

    @Override
    public void addMovie(Movie m) {
        interactor.addMovie(m);
    }

    @Override
    public Movie getMovie() {
        return movie;
    }
    @Override
    public void setMovie(Parcelable movie) {
        this.movie = (Movie)movie;
    }
}

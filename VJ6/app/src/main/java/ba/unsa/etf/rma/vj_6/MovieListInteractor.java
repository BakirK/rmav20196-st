package ba.unsa.etf.rma.vj_6;

import java.util.ArrayList;

public class MovieListInteractor implements IMovieListInteractor {

    @Override
    public ArrayList<Movie> get() {
        return MoviesModel.movies;
    }

    @Override
    public void addMovie(Movie m) {
        MoviesModel.movies.add(m);
    }
}

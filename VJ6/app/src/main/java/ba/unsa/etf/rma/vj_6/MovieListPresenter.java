package ba.unsa.etf.rma.vj_6;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class MovieListPresenter implements IMovieListPresenter, MovieListInteractor.OnMoviesSearchDone {
    //private IMovieListInteractor interactor;
    private IMovieListView view;
    private Context context;

    public MovieListPresenter(IMovieListView view, Context context) {
        //this.interactor = new MovieListInteractor();
        this.view = view;
        this.context = context;
    }

/*
    @Override
    public void refreshMovies() {
        view.setMovies(interactor.get());
        view.notifyMovieListDataSetChanged();
    }*/

    @Override
    public void onDone(ArrayList<Movie> results) {
        view.setMovies(results);
        view.notifyMovieListDataSetChanged();
    }

    @Override
    public void searchMovies(String query) {
        Log.d("aaaa", "here");
        new MovieListInteractor((MovieListInteractor.OnMoviesSearchDone) this).execute(query);
    }
}

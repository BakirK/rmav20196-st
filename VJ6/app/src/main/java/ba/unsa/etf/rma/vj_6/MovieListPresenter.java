package ba.unsa.etf.rma.vj_6;

import android.content.Context;

public class MovieListPresenter implements IMovieListPresenter {
    private IMovieListInteractor interactor;
    private IMovieListView view;
    private Context context;

    public MovieListPresenter(IMovieListView view, Context context) {
        this.interactor = new MovieListInteractor();
        this.view = view;
        this.context = context;
    }


    @Override
    public void refreshMovies() {
        view.setMovies(interactor.get());
        view.notifyMovieListDataSetChanged();
    }
}

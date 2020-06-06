package ba.unsa.etf.rma.vj_6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

public class MovieListPresenter implements IMovieListPresenter, MovieListResultReceiver.Receiver {
    private IMovieListInteractor movieListInteractor;
    private MovieListResultReceiver movieListResultReceiver;
    private IMovieListView view;
    private Context context;

    public MovieListPresenter(IMovieListView view, Context context) {
        this.movieListInteractor = new MovieListInteractor();
        this.view = view;
        this.context = context;
    }

/*
    @Override
    public void onDone(ArrayList<Movie> results) {
        view.setMovies(results);
        view.notifyMovieListDataSetChanged();
    }*/

    @Override
    public void searchMovies(String query) {
        //new MovieListInteractor((MovieListInteractor.OnMoviesSearchDone) this).execute(query);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, MovieListInteractor.class);
        intent.putExtra("query", query);
        movieListResultReceiver = new MovieListResultReceiver(new Handler());
        movieListResultReceiver.setReceiver(MovieListPresenter.this);
        intent.putExtra("receiver", movieListResultReceiver);
        context.getApplicationContext().startService(intent);
    }

    @Override
    public void getMoviesCursor() {
        view.setCursor(movieListInteractor.getMovieCursor(context.getApplicationContext()));
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case MovieListInteractor.STATUS_RUNNING:
                view.showToast(context.getResources().getString(R.string.searching));
                break;
            case MovieListInteractor.STATUS_FINISHED:
                ArrayList<Movie> results = resultData.getParcelableArrayList("result");
                view.showToast(context.getResources().getString(R.string.success));
                view.setMovies(results);
                break;
            case MovieListInteractor.STATUS_ERROR:
                view.showToast(context.getResources().getString(R.string.error));
                break;
        }
    }
}

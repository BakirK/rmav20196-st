package ba.unsa.etf.rma.vj_6;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;

public class MovieDetailPresenter implements IMovieDetailPresenter, MovieDetailResultReceiver.Receiver {
    private IMovieDetailInteractor movieDetailInteractor;
    private MovieDetailResultReceiver movieDetailResultReceiver;
    private Context context;
    private IMovieDetailView view;
    private Movie movie;

    public MovieDetailPresenter(IMovieDetailView view, Context context) {
        this.view       = view;
        this.context    = context;
        this.movieDetailInteractor = new MovieDetailInteractor();
    }

    public MovieDetailPresenter(Context context) {
        this.context    = context;
        this.movieDetailInteractor = new MovieDetailInteractor();
    }

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
        //new MovieDetailInteractor((MovieDetailInteractor.OnMovieSearchDone) this).execute(query);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, MovieDetailInteractor.class);
        intent.putExtra("query", query);
        movieDetailResultReceiver = new MovieDetailResultReceiver(new Handler());
        movieDetailResultReceiver.setReceiver(MovieDetailPresenter.this);
        intent.putExtra("receiver", movieDetailResultReceiver);
        context.getApplicationContext().startService(intent);
    }

    @Override
    public void getDatabaseMovie(int id) {
        movie = movieDetailInteractor.getMovie(context, id);
    }

    @Override
    public Cursor getCastCursor(int id) {
        return movieDetailInteractor.getCastCursor(context, id);
    }

    @Override
    public Cursor getSimilarCursor(int id) {
        return movieDetailInteractor.getSimilarCursor(context, id);
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case MovieListInteractor.STATUS_RUNNING:
                view.showToast(context.getResources().getString(R.string.searching));
                break;
            case MovieListInteractor.STATUS_FINISHED:
                Movie result = resultData.getParcelable("result");
                view.showToast(context.getResources().getString(R.string.success));
                movie = result;
                movieDetailInteractor.save(result,context.getApplicationContext());
                view.refreshView();
                break;
            case MovieListInteractor.STATUS_ERROR:
                view.showToast(context.getResources().getString(R.string.error));
                break;
        }
    }
}

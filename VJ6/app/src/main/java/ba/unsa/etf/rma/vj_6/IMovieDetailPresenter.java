package ba.unsa.etf.rma.vj_6;

public interface IMovieDetailPresenter {
    Movie getMovieByTitle(String title);
    void addMovie(Movie m);
    Movie getMovie();
}

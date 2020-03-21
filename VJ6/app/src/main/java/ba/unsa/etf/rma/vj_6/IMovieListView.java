package ba.unsa.etf.rma.vj_6;

import java.util.ArrayList;

interface IMovieListView {
    void setMovies(ArrayList<Movie> movies);
    void notifyMovieListDataSetChanged();
}

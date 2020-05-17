package ba.unsa.etf.rma.vj_6;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Movie implements Parcelable {
    private String title;
    private String genre;
    private String releaseDate;
    private String homepage;
    private String overview;
    private String posterPath;
    private Integer id;

    public Movie(Integer id, String title, String overview, String releaseDate, String posterPath) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;

    }

    public Movie(String title, String overview, String releaseDate, String homepage, String posterPath, Integer id, String genre) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.homepage = homepage;
        this.genre = genre;
    }

    public Movie(String title, String overview, String releaseDate, String homepage, String genre, ArrayList<String> actors) {
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.homepage = homepage;
        this.genre = genre;
        this.actors = actors;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(ArrayList<String> actors) {
        this.actors = actors;
    }

    private ArrayList<String> actors;

    public ArrayList<String> getSimilarMovies() {
        return similarMovies;
    }

    public void setSimilarMovies(ArrayList<String> similarMovies) {
        this.similarMovies = similarMovies;
    }

    private ArrayList<String> similarMovies;

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Movie(String title, String overview, String releaseDate, String homepage, String genre, ArrayList<String> actors, String posterPath, Integer id) {
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.homepage = homepage;
        this.overview = overview;
        this.actors = actors;
        this.posterPath = posterPath;
        this.id = id;
    }

    public Movie(String title, String overview, String releaseDate, String homepage, String genre, ArrayList<String> actors, ArrayList<String> similarMovies) {
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.homepage = homepage;
        this.overview = overview;
        this.actors = actors;
        this.similarMovies = similarMovies;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        genre = in.readString();
        releaseDate = in.readString();
        homepage = in.readString();
        overview = in.readString();
        actors = in.createStringArrayList();
        similarMovies = in.createStringArrayList();
        id = in.readInt();
        posterPath = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(genre);
        dest.writeString(releaseDate);
        dest.writeString(homepage);
        dest.writeString(overview);
        dest.writeStringList(actors);
        dest.writeStringList(similarMovies);
        dest.writeInt(id);
        dest.writeString(posterPath);
    }
}

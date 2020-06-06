package ba.unsa.etf.rma.vj_6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "RMADataBase.db";
    public static final int DATABASE_VERSION = 2;


    public MovieDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MovieDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String MOVIE_TABLE = "movies";
    public static final String MOVIE_ID = "id";
    public static final String MOVIE_INTERNAL_ID = "_id";
    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_GENRE = "genre";
    public static final String MOVIE_HOMEPAGE = "homepage";
    public static final String MOVIE_POSTERPATH = "posterPath";
    public static final String MOVIE_RELEASEDATE = "releaseDate";
    public static final String MOVIE_OVERVIEW = "overview";
    private static final String MOVIE_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + MOVIE_TABLE + " ("  + MOVIE_INTERNAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + MOVIE_ID + " INTEGER UNIQUE, "
                    + MOVIE_TITLE + " TEXT NOT NULL, "
                    + MOVIE_GENRE + " TEXT, "
                    + MOVIE_HOMEPAGE + " TEXT, "
                    + MOVIE_POSTERPATH + " TEXT, "
                    + MOVIE_RELEASEDATE + " TEXT, "
                    + MOVIE_OVERVIEW + " TEXT);";

    private static final String MOVIE_DROP = "DROP TABLE IF EXISTS " + MOVIE_TABLE;



    public static final String CAST_TABLE = "actors";
    public static final String CAST_ID = "_id";
    public static final String CAST_NAME = "name";
    public static final String CAST_MOVIE_ID = "movie_id";


    private static final String CAST_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + CAST_TABLE + " (" + CAST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CAST_NAME + " TEXT NOT NULL, "
                    + CAST_MOVIE_ID + " INTEGER NOT NULL, "+" FOREIGN KEY ("+CAST_MOVIE_ID+") REFERENCES "+ MOVIE_TABLE +"("+MOVIE_ID+"));";


    private static final String CAST_TABLE_DROP = "DROP TABLE IF EXISTS " + CAST_TABLE;



    public static final String SIMILIAR_MOVIES = "similar";
    public static final String SMOVIES_ID = "_id";
    public static final String SMOVIE_TITLE = "title";
    public static final String SMOVIES_MOVIE_ID = "movie_id";


    private static final String SMOVIES_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + SIMILIAR_MOVIES + " (" + SMOVIES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SMOVIE_TITLE + " TEXT NOT NULL, "
                    + SMOVIES_MOVIE_ID + " INTEGER NOT NULL, "+" FOREIGN KEY ("+ SMOVIES_MOVIE_ID +") REFERENCES "+ MOVIE_TABLE +"("+MOVIE_ID+"));";

    private static final String SMOVIES_DROP = "DROP TABLE IF EXISTS " + SIMILIAR_MOVIES;



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MOVIE_TABLE_CREATE);
        db.execSQL(CAST_TABLE_CREATE);
        db.execSQL(SMOVIES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CAST_TABLE_DROP);
        db.execSQL(SMOVIES_DROP);
        db.execSQL(MOVIE_DROP);
        onCreate(db);
    }
}

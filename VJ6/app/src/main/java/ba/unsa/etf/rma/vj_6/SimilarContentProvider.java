package ba.unsa.etf.rma.vj_6;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SimilarContentProvider extends ContentProvider {

    private static final int MOVIEID =1;
    private static final UriMatcher uM;

    static {
        uM = new UriMatcher(UriMatcher.NO_MATCH);
        uM.addURI("rma.provider.similar","elements/#",MOVIEID);
    }

    MovieDBOpenHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new MovieDBOpenHelper(getContext(),
                MovieDBOpenHelper.DATABASE_NAME,null,
                MovieDBOpenHelper.DATABASE_VERSION);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArg, @Nullable String sortOrder) {
        SQLiteDatabase database;
        try{
            database=mHelper.getWritableDatabase();
        }catch (SQLiteException e){
            database=mHelper.getReadableDatabase();
        }
        String groupby=null;
        String having=null;
        SQLiteQueryBuilder squery = new SQLiteQueryBuilder();

        switch (uM.match(uri)){
            case MOVIEID:
                String idRow = uri.getPathSegments().get(1);
                squery.appendWhere(MovieDBOpenHelper.SMOVIES_MOVIE_ID+"="+idRow);
            default:break;
        }
        squery.setTables(MovieDBOpenHelper.SIMILIAR_MOVIES);
        Cursor cursor = squery.query(database,projection,selection,selectionArg,groupby,having,sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uM.match(uri)){
            case MOVIEID:
                return "vnd.android.cursor.dir/vnd.rma.elemental";
            default:
                throw new IllegalArgumentException("Unsuported uri: "+uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database;
        try{
            database=mHelper.getWritableDatabase();
        }catch (SQLiteException e){
            database=mHelper.getReadableDatabase();
        }
        long id = database.insert(MovieDBOpenHelper.SIMILIAR_MOVIES, null, contentValues);
        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

}


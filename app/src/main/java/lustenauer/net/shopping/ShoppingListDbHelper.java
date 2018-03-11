package lustenauer.net.shopping;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Patric Hollenstein on 25.07.17.
 *
 * @author Patric Hollenstein
 */
public class ShoppingListDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = ShoppingListDbHelper.class.getSimpleName();

    public static final String DB_NAME = "shopping_list.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_SHOPPING_LIST = "shopping_list";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCT = "product";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_SHOP = "shop";
    public static final String COLUMN_DATE = "date";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_SHOPPING_LIST +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT + " TEXT NOT NULL, " +
                    COLUMN_PRICE + " REAL NOT NULL, " +
                    COLUMN_SHOP + " TEXT NOT NULL, " +
                    COLUMN_DATE + " TEXT NOT NULL);";



    public ShoppingListDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper create the database: " + getDatabaseName());
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            database.execSQL(SQL_CREATE);
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

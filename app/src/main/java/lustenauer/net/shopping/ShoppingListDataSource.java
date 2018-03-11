package lustenauer.net.shopping;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Patric Hollenstein on 25.07.17.
 *
 * @author Patric Hollenstein
 */
public class ShoppingListDataSource {
    private static final String LOG_TAG = ShoppingListDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private ShoppingListDbHelper dbHelper;

    private String[] columns = {
            ShoppingListDbHelper.COLUMN_ID,
            ShoppingListDbHelper.COLUMN_PRODUCT,
            ShoppingListDbHelper.COLUMN_PRICE,
            ShoppingListDbHelper.COLUMN_SHOP,
            ShoppingListDbHelper.COLUMN_DATE
    };

    public ShoppingListDataSource(Context context) {
        Log.d(LOG_TAG, "Create dbHelper.");
        dbHelper = new ShoppingListDbHelper(context);
        open();
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }


    public ShoppingEntry createShoppingListEntry(String product, float price, String shop) {
        ContentValues values = new ContentValues();
        values.put(ShoppingListDbHelper.COLUMN_PRODUCT, product);
        values.put(ShoppingListDbHelper.COLUMN_PRICE, price);
        values.put(ShoppingListDbHelper.COLUMN_SHOP, shop);
        values.put(ShoppingListDbHelper.COLUMN_DATE, new Date().toString());

        long insertId = database.insert(ShoppingListDbHelper.TABLE_SHOPPING_LIST, null, values);

        Cursor cursor = database.query(ShoppingListDbHelper.TABLE_SHOPPING_LIST,
                columns, ShoppingListDbHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        ShoppingEntry entry = cursorToShoppingEntry(cursor);
        cursor.close();

        return entry;
    }

    private ShoppingEntry cursorToShoppingEntry(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(ShoppingListDbHelper.COLUMN_ID);
        int idProduct = cursor.getColumnIndex(ShoppingListDbHelper.COLUMN_PRODUCT);
        int idPrice = cursor.getColumnIndex(ShoppingListDbHelper.COLUMN_PRICE);
        int idShop = cursor.getColumnIndex(ShoppingListDbHelper.COLUMN_SHOP);
        int idDate = cursor.getColumnIndex(ShoppingListDbHelper.COLUMN_DATE);

        String product = cursor.getString(idProduct);
        float price = cursor.getFloat(idPrice);
        String shop = cursor.getString(idShop);
        String date = cursor.getString(idDate);

        long id = cursor.getLong(idIndex);

        ShoppingEntry entry = new ShoppingEntry(product, price, shop, date, id);

        return entry;
    }

    public List<ShoppingEntry> getAllShoppingEntrys() {
        List<ShoppingEntry> shoppingEntryList = new ArrayList<>();

        Cursor cursor = database.query(ShoppingListDbHelper.TABLE_SHOPPING_LIST,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        ShoppingEntry entry;

        while (!cursor.isAfterLast()) {
            entry = cursorToShoppingEntry(cursor);
            shoppingEntryList.add(entry);
            Log.d(LOG_TAG, "ID: " + entry.getId() + ", Inhalt: " + entry.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return shoppingEntryList;
    }

    public List<String> getAllShops() {
        List<String> shopList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT DISTINCT shop FROM shopping_list", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            shopList.add(cursor.getString(cursor.getColumnIndex(ShoppingListDbHelper.COLUMN_SHOP)));
            cursor.moveToNext();
        }
        cursor.close();

        return shopList;
    }

    public List<String> getAllProducts() {
        List<String> productList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT DISTINCT product FROM shopping_list", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            productList.add(cursor.getString(cursor.getColumnIndex(ShoppingListDbHelper.COLUMN_PRODUCT)));
            cursor.moveToNext();
        }
        cursor.close();

        return productList;
    }

    public List<ShoppingEntry> findProducts(String product) {
        List<ShoppingEntry> entryList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM shopping_list WHERE product LIKE ? ORDER BY price ASC ", new String[]{"%" + product + "%"});

        cursor.moveToFirst();
        ShoppingEntry entry;

        while (!cursor.isAfterLast()) {
            entry = cursorToShoppingEntry(cursor);
            entryList.add(entry);
            cursor.moveToNext();
        }
        cursor.close();

        return entryList;
    }
}

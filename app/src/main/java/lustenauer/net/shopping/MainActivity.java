package lustenauer.net.shopping;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.List;

/**
 * Created by Patric Hollenstein on 25.07.17.
 *
 * @author Patric Hollenstein
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText editTextPrice;
    private AutoCompleteTextView acViewProduct;
    private AutoCompleteTextView acViewShop;

    private ShoppingListDataSource dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "Das Datenquellen-Objekt wird angelegt.");
        dataSource = new ShoppingListDataSource(this);

        editTextPrice = (EditText) findViewById(R.id.editText_price);
        acViewProduct = (AutoCompleteTextView) findViewById(R.id.acView_product);
        acViewShop = (AutoCompleteTextView) findViewById(R.id.acView_shop);

        acViewProduct.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    updateListEntrys();
                }
            }
        });

        updateShopAdapter();
        updateProductAdapter();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.i("Main", "settings");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

        Log.d(TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        updateListEntrys();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();
    }

    private void updateListEntrys() {
        String productText = acViewProduct.getText().toString();

        if (productText.isEmpty()) return;

        List<ShoppingEntry> shoppingEntries = dataSource.findProducts(productText);
        ArrayAdapter<ShoppingEntry> shoppingMemoArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shoppingEntries);

        ListView shoppingMemosListView = (ListView) findViewById(R.id.listview_shopping_list);
        shoppingMemosListView.setAdapter(shoppingMemoArrayAdapter);
    }


    private void updateShopAdapter() {
        List<String> shopsList = dataSource.getAllShops();
        acViewShop.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, shopsList));
    }

    private void updateProductAdapter() {
        List<String> productList = dataSource.getAllProducts();
        acViewProduct.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, productList));
    }


    public void doSearch(View view) {
        Log.d(TAG, "doSearch: ");
        updateListEntrys();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void doAdd(View view) {
        String price = editTextPrice.getText().toString();
        String shop = acViewShop.getText().toString();
        String product = acViewProduct.getText().toString();

        boolean error = false;

        if (TextUtils.isEmpty(price)) {
            editTextPrice.setError(getString(R.string.editText_errorMessage));
            error = true;
        }
        if (TextUtils.isEmpty(shop)) {
            acViewShop.setError(getString(R.string.editText_errorMessage));
            error = true;
        }
        if (TextUtils.isEmpty(product)) {
            acViewProduct.setError(getString(R.string.editText_errorMessage));
            error = true;
        }

        if (error) return;

        editTextPrice.setText("");
        acViewShop.setText("");
        acViewProduct.setText("");

        acViewProduct.requestFocus();

        dataSource.createShoppingListEntry(product, Float.parseFloat(price), shop);

        InputMethodManager inputMethodManager;
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        updateListEntrys();
        updateShopAdapter();
        updateProductAdapter();

    }
}

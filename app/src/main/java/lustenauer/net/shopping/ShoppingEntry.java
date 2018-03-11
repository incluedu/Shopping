package lustenauer.net.shopping;

import java.util.Date;

/**
 * Created by Patric Hollenstein on 25.07.17.
 *
 * @author Patric Hollenstein
 */
public class ShoppingEntry {

    private String product;
    private float price;
    private String shop;
    private String date;
    private long id;


    public ShoppingEntry(String product, float price, String shop, String date, long id) {
        this.product = product;
        this.price = price;
        this.shop = shop;
        this.date = date;
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return product + " bei " + shop + " um " + price + "€";
    }
}

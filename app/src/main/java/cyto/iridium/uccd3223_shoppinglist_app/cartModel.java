package cyto.iridium.uccd3223_shoppinglist_app;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class cartModel {

    // Model entity for cart items in gallery fragments (shopping list)

    String id;
    String name;
    double price;
    int qty;

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    // Getting the double with trailing zero via the formatter
    public String getPriceS() {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(price);
    }


    public void setPrice(double price) {
        this.price = price;
    }

}

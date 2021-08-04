package cyto.iridium.uccd3223_shoppinglist_app;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class itemModel {

    // Model entity for items in the home fragments

    String id;
    String name;
    double price;

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

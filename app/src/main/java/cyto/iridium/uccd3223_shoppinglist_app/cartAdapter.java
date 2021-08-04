package cyto.iridium.uccd3223_shoppinglist_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class cartAdapter extends BaseAdapter {
    // Adapter to be used for displaying multiple items in the JSONArray for views
    Context context;
    ArrayList<cartModel> arrayList;

    public cartAdapter(Context context, ArrayList<cartModel> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView ==  null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_listing, parent, false);
        }

        // Declaration
        TextView name, price,  qty, batch;
        Button btnDlt;
        name = (TextView) convertView.findViewById(R.id.store_name);
        price = (TextView) convertView.findViewById(R.id.store_price);
        qty = (TextView) convertView.findViewById(R.id.store_qty);
        batch = (TextView) convertView.findViewById(R.id.store_batch);
        btnDlt = (Button) convertView.findViewById(R.id.btnDlt);

        // Setting views for the listview
        name.setText(arrayList.get(position).getName());
        price.setText(arrayList.get(position).getPriceS() + " CR");
        qty.setText("Quantity: " + arrayList.get(position).getQty());

        // Batch amount calculation, which is quantity multiples by price of an item
        double bp = arrayList.get(position).getQty() * arrayList.get(position).getPrice();
        NumberFormat formatter = new DecimalFormat("#0.00");
        batch.setText("Batch: " + formatter.format(bp) + " CR");

        btnDlt.setTag(arrayList.get(position).getId());

        return convertView;
    }

    // Method used to calculate the total credits for all the items in batch in a cart list
    public String CalculateTotal(){
        double x = 0.00;
        for(int i = 0; i < arrayList.size(); i++){
            int qty = arrayList.get(i).getQty();
            double price = arrayList.get(i).getPrice();

            x += qty*price;
        }
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(x);
    }
}

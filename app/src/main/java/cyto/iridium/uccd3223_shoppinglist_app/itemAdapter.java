package cyto.iridium.uccd3223_shoppinglist_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class itemAdapter extends BaseAdapter {
    // Adapter to be used for displaying multiple items in the JSONArray for views
    Context context;
    ArrayList<itemModel> arrayList;

    public itemAdapter(Context context, ArrayList<itemModel> arrayList){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.store_listing, parent, false);
        }

        // Declaration
        TextView name, price;
        Button btnAdd;
        name = (TextView) convertView.findViewById(R.id.store_name);
        price = (TextView) convertView.findViewById(R.id.store_price);
        btnAdd = (Button) convertView.findViewById(R.id.btnAdd);

        // Generating the JSON string to be appended to the button for items in the home fragments
        String JSONStr =
                "{"
                        + "\"id\" : "  + "\"" + arrayList.get(position).getId() + "\","
                        + "\"name\" : "  + "\"" + arrayList.get(position).getName() + "\","
                        + "\"price\" : "  + "\"" + arrayList.get(position).getPriceS() + "\","
                        + "\"qty\" : "  + "\"" + "1" + "\""
                        + "}";

        // Setting views for the items in listview
        name.setText(arrayList.get(position).getName());
        price.setText(arrayList.get(position).getPriceS() + " CR");
        btnAdd.setTag(JSONStr);

        return convertView;
    }
}

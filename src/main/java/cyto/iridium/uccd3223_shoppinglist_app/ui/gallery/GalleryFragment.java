package cyto.iridium.uccd3223_shoppinglist_app.ui.gallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import cyto.iridium.uccd3223_shoppinglist_app.R;
import cyto.iridium.uccd3223_shoppinglist_app.cartAdapter;
import cyto.iridium.uccd3223_shoppinglist_app.cartModel;
import cyto.iridium.uccd3223_shoppinglist_app.databinding.FragmentGalleryBinding;
import cyto.iridium.uccd3223_shoppinglist_app.itemAdapter;
import cyto.iridium.uccd3223_shoppinglist_app.itemModel;

public class GalleryFragment extends Fragment {
    // Gallery (Shopping cart) Fragments for displaying items in the home section

    ListView listView;
    ArrayList<cartModel> arrayList;
    TextView textView;

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;

    // Template code binding
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Fragment refresh
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        //Parsing list from shared preferences
        arrayList = new ArrayList<>();
        listView = (ListView) getView().findViewById(R.id.cartList);
        textView = (TextView) getView().findViewById(R.id.textSummary);

        Boolean flag = false;

        SharedPreferences pref = getActivity().getSharedPreferences("Sievert_Iridium", Context.MODE_PRIVATE);
        String listing = pref.getString("Cart_List","{}");
        JSONObject objList = null;
        try{
            // Appending items from JSON to be displayed via an adapter
            objList = new JSONObject(listing);
            JSONArray arr = objList.getJSONArray("cart");
            if(arr.length() <= 0){
                textView.setText("No records found");
                flag = true;
            }else {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject itemObj = arr.getJSONObject(i);
                    String id = itemObj.getString("id");
                    String name = itemObj.getString("name");
                    double price = itemObj.getDouble("price");
                    int qty = itemObj.getInt("qty");

                    // Appending item attribute to model entity
                    cartModel model = new cartModel();
                    model.setId(id);
                    model.setName(name);
                    model.setPrice(price);
                    model.setQty(qty);
                    arrayList.add(model);
                }
            }
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        // Generating the listview with the appending items in the adapter via an ArrayList
        cartAdapter adapter = new cartAdapter(this.getActivity(), arrayList);
        // If list is not empty, will calculate the total credits too
        if(!flag) {
            textView.setText("Total: " + adapter.CalculateTotal() + " CR");
        }
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
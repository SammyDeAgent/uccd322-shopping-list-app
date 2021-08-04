package cyto.iridium.uccd3223_shoppinglist_app.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cyto.iridium.uccd3223_shoppinglist_app.R;
import cyto.iridium.uccd3223_shoppinglist_app.databinding.FragmentHomeBinding;
import cyto.iridium.uccd3223_shoppinglist_app.itemAdapter;
import cyto.iridium.uccd3223_shoppinglist_app.itemModel;

public class HomeFragment extends Fragment {
    // Home Fragments for displaying items in the home section

    ListView listView;
    ArrayList<itemModel> arrayList;

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    // Template code binding
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
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

        SharedPreferences pref = getActivity().getSharedPreferences("Sievert_Iridium",Context.MODE_PRIVATE);
        String listing = pref.getString("Store_List","{}");
        JSONObject objList = null;
        try{
            // Appending items from JSON to be displayed via an adapter
            objList = new JSONObject(listing);
            JSONArray arr = objList.getJSONArray("item");
            for(int i = 0; i < arr.length(); i++){
                JSONObject itemObj = arr.getJSONObject(i);
                String id = itemObj.getString("id");
                String name = itemObj.getString("name");
                double price = itemObj.getDouble("price");

                // Appending item attribute to model entity
                itemModel model = new itemModel();
                model.setId(id);
                model.setName(name);
                model.setPrice(price);
                arrayList.add(model);
            }
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        // Generating the listview with the appending items in the adapter via an ArrayList
        itemAdapter adapter = new itemAdapter(this.getActivity(), arrayList);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
package cyto.iridium.uccd3223_shoppinglist_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cyto.iridium.uccd3223_shoppinglist_app.databinding.ActivityMainBinding;
import cyto.iridium.uccd3223_shoppinglist_app.ui.gallery.GalleryFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    // Using the drawer navigation activity template provided by Android Studio
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting Preliminary SharedPreference Static Item List Data as JSON
        JSONObject obj = null;
        try{
            obj = new JSONObject(readJSON());
        }catch(JSONException ex){
            ex.printStackTrace();
        }

        SharedPreferences s_list = getSharedPreferences(
                "Sievert_Iridium",
                MODE_PRIVATE
        );
        SharedPreferences.Editor s_listEdit = s_list.edit();
        try{
            s_listEdit.putString(
                    "Store_List",
                    String.valueOf(obj)
            );
        }catch(Exception ignore){
        }
        s_listEdit.commit();

        // Setting Preliminary SharedPreference Cart List Data as JSON
        JSONObject temp = null;
        try{
            temp = new JSONObject("{cart:[]}");
        }catch(JSONException ex){
            ex.printStackTrace();
        }

        SharedPreferences c_list = getSharedPreferences(
                "Sievert_Iridium",
                MODE_PRIVATE
        );
        SharedPreferences.Editor c_listEdit = c_list.edit();
        try{
            c_listEdit.putString(
                    "Cart_List",
                    String.valueOf(temp)
            );
        }catch(Exception ignore){
        }
        c_listEdit.commit();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Template drawer setup
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    // Menu creation on the top right
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.add("Exit")
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Kills everything to exit the program
                        finishAffinity();
                        return false;
                    }
                });
        return true;
    }

    // Template code for navigation between fragments
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // Adding selected item to cart, parsing into JSON and SharedPreferences
    public void addToCart(View view){
        // Individual Item attribute are stored as tag in the button
        String attribute = (String) view.getTag();
        JSONObject tempObj = null;
        try {
            tempObj = new JSONObject(attribute);
        }catch(JSONException ex){
            ex.printStackTrace();
        }

        SharedPreferences c_list = getSharedPreferences(
                "Sievert_Iridium",
                MODE_PRIVATE
        );

        JSONObject cart = null;
        try {
            cart = new JSONObject(c_list.getString("Cart_List", "{}"));
            JSONArray cartArr = cart.getJSONArray("cart");
            // If there's nothing in the cart JSON yet, it simply appends/adds it
            if(cartArr.length() == 0){
                cartArr.put(tempObj);
            }else{
                // If there's item in the list, it will search by id for the object
                Boolean flag = false;
                for(int i = 0; i < cartArr.length(); i++){
                    JSONObject item = cartArr.getJSONObject(i);
                    // If object exists, it will add "1" to the qty attribute
                    if(item.getString("id").equals(tempObj.getString("id"))){
                        int qty = item.getInt("qty");
                        item.put("qty",qty+1);
                        flag = true;
                        break;
                    }
                }
                // Else if object doesn't exist, it will again, simply appends/adds it to the JSON
                if(!flag){
                    cartArr.put(tempObj);
                }
            }

            // Saving result to a shopping cart SharedPreferences
            SharedPreferences.Editor c_listEdit = c_list.edit();
            try{
                c_listEdit.putString(
                        "Cart_List",
                        String.valueOf(cart)
                );
            }catch(Exception ignore){
            }
            c_listEdit.commit();

        }catch(JSONException ex){
            ex.printStackTrace();
        }

        JToast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show();
    }

    //Removing all content from cart + reloading the page
    public void removeCart(View view){
        // Emptying the cart SharedPreferences
        SharedPreferences c_list = getSharedPreferences(
                "Sievert_Iridium",
                MODE_PRIVATE
        );
        SharedPreferences.Editor c_listEdit = c_list.edit();
        try{
            c_listEdit.putString(
                    "Cart_List",
                   "{\"cart\":[]}"
            );
        }catch(Exception ignore){
        }
        c_listEdit.commit();

        // Preliminary declaration to the changing value for listview and textview
        ListView lv = findViewById(R.id.cartList);
        TextView tx = findViewById(R.id.textSummary);

        // Since all value is emptyed, an empty arraylist can be called to create an empty listview
        cartAdapter adapter = new cartAdapter(this, new ArrayList<>());
        tx.setText("No records found");
        lv.setAdapter(adapter);

        JToast.makeText(this, "Shopping cart cleared", Toast.LENGTH_SHORT).show();
    }

    //Removing single listing from cart + reloading the page
    public void removeItem(View view){

        // Preliminary declaration to the changing value
        ListView lv = findViewById(R.id.cartList);
        TextView tv = findViewById(R.id.textSummary);
        ArrayList<cartModel> arrayList = new ArrayList<>();

        // Item attribute via ID is retrieved from the button
        String match = (String) view.getTag();

        Boolean flag = false;
        int pos = 0;

        SharedPreferences c_list = getSharedPreferences(
                "Sievert_Iridium",
                MODE_PRIVATE
        );
        String listing = c_list.getString("Cart_List","{}");
        JSONObject objList = null;
        try{
            objList = new JSONObject(listing);
            JSONArray arr = objList.getJSONArray("cart");

            // If the cart is empty, then change the sum to following text for textview
            if(arr.length() <= 0) {
                tv.setText("No records found");
            }

            // Search for item id matching in the cart list JSON
            for(int i = 0; i < arr.length(); i++){
                JSONObject item = arr.getJSONObject(i);
                // Matching id to tag id
                if(item.getString("id").equals(match)){
                    int qty = item.getInt("qty");
                    // If item have more than one quanity, simply deduct "1" from qty
                    if(qty > 1) {
                        item.put("qty", qty - 1);
                    }else if(qty == 1){
                        // Else if item is equal to "1" in qty, simply remove from JSONArray
                        pos = i;
                        // Noted here, since array.remove() have API restrictions >= 18 level
                        // A code snippet from Stack Overflow is used to compensate the restriction
                        // Otherwise, the principle is to remove the item (via position) from the JSON
                        JSONArray_remove(pos,arr);
                    }
                    break;
                }
            }

            // Re-adding the cart JSON to listview, in terms, reloading it on-spot
            if(arr.length() <= 0){
                tv.setText("No records found");
                flag = true;
            }else {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject itemObj = arr.getJSONObject(i);
                    String id = itemObj.getString("id");
                    String name = itemObj.getString("name");
                    double price = itemObj.getDouble("price");
                    int qty = itemObj.getInt("qty");

                    // Appending the attribute to a model entity
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Preferences Update for cart list
        SharedPreferences.Editor c_listEdit = c_list.edit();
        try{
            c_listEdit.putString(
                    "Cart_List",
                    String.valueOf(objList)
            );
        }catch(Exception ignore){
        }
        c_listEdit.commit();

        // The adapter will setup a new listview from the arraylist
        cartAdapter adapter = new cartAdapter(this, arrayList);
        // If the list is not empty, then will calculate for the total credits needed
        if(!flag) {
            tv.setText("Total: " + adapter.CalculateTotal() + " CR");
        }
        lv.setAdapter(adapter);

        JToast.makeText(this, "Cart item removed", Toast.LENGTH_SHORT).show();
    }

    // Saving the cart list
    public void saveList(View view){
        SharedPreferences pref = getSharedPreferences(
                "Sievert_Iridium",
                MODE_PRIVATE
        );
        String cart = pref.getString("Cart_List","{}");

        // Appending the cart list to a save list in SharedPreferences
        SharedPreferences.Editor prefEdit = pref.edit();
        try{
            prefEdit.putString(
                    "Save_List",
                    cart
            );
        }catch(Exception ignore){
        }
        prefEdit.commit();

        JToast.makeText(this, "Cart list saved", Toast.LENGTH_SHORT).show();

    }

    //Loading the saved list
    public void loadList(View view){
        SharedPreferences pref = getSharedPreferences(
                "Sievert_Iridium",
                MODE_PRIVATE
        );
        // Loading the save list to the cart list
        String save = pref.getString("Save_List", "{}");
        SharedPreferences.Editor prefEdit = pref.edit();
        try{
            prefEdit.putString(
                    "Cart_List",
                    save
            );
        }catch(Exception ignore){
        }
        prefEdit.commit();

        // Reloading shopping cart with the new cart list from load
        String listing = pref.getString("Cart_List", "{}");

        // Preliminary declaration
        ListView listView;
        ArrayList<cartModel> arrayList;
        TextView textView;

        arrayList = new ArrayList<>();
        listView = findViewById(R.id.cartList);
        textView = findViewById(R.id.textSummary);

        Boolean flag = false;
        JSONObject objList = null;
        try{
            objList = new JSONObject(listing);
            JSONArray arr = objList.getJSONArray("cart");
            // If cart is emtpty then no need for appending attribute to model entity
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

                    // Appending cart item attribute to model entity
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

        // Adapter setup for listview
        cartAdapter adapter = new cartAdapter(this, arrayList);
        // If the list is not empty, then will calculate for the total credits needed
        if(!flag) {
            textView.setText("Total: " + adapter.CalculateTotal() + " CR");
        }
        listView.setAdapter(adapter);

        JToast.makeText(this, "Previous save list loaded", Toast.LENGTH_SHORT).show();
    }

    //JSON Parse - reading from assets folder
    public String readJSON() {
        String json = null;
        try {
            // Opening data.json file
            InputStream inputStream = getAssets().open("list.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            // Read values in the byte array
            inputStream.read(buffer);
            inputStream.close();
            // Convert byte to string
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return json;
        }
        return json;
    }

    //JSON item Removal - Imported from StackOverflow to counteract API restrictions
    public void JSONArray_remove(int index, JSONArray JSONArrayObject) throws Exception{
        if(index < 0)
            return;
        Field valuesField=JSONArray.class.getDeclaredField("values");
        valuesField.setAccessible(true);
        List<Object> values=(List<Object>)valuesField.get(JSONArrayObject);
        if(index >= values.size())
            return;
        values.remove(index);
    }
}


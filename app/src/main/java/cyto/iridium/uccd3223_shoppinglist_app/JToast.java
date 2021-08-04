package cyto.iridium.uccd3223_shoppinglist_app;

import android.content.Context;
import android.widget.Toast;

// To avoid overlapping of toast in queue
public class JToast {
    private static Toast toast;
    private static int LENGTH_LONG= Toast.LENGTH_LONG;
    private static int LENGTH_SHORT=Toast.LENGTH_SHORT;

    // Basically checks if there's  already a toast exists, then will overwrite and replace with a new one
    public static Toast makeText(Context context, String text, int duration) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, duration);
        return toast;
    }
    public void show(){
        toast.show();
    }
}

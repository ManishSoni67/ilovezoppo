package co.manishsoni.ilovezappos.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

/**
 * Created by manis on 2/8/2017.
 */

public class LocalPrefecrences {
    Context con;
    SharedPreferences sharedPreferences;

    public LocalPrefecrences(Context _con) {
        con = _con;
        sharedPreferences = con.getSharedPreferences("pref_key", Context.MODE_PRIVATE);
    }

    public boolean addProductToCart(String productId) {
        try {
            if (cartHasAlreadyAdded(productId)) {
                return false;
            }
            sharedPreferences.edit().putString(productId, productId).apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean cartHasAlreadyAdded(String productId) {
        try {


            return sharedPreferences.contains(productId);
        } catch (Exception e) {
            return false;
        }

    }

    public int getAddedProductCount() {

        return sharedPreferences.getAll().size();

    }


}

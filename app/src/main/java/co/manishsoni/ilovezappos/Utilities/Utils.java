package co.manishsoni.ilovezappos.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by manis on 1/29/2017.
 */

public class Utils {


    public static String getResponse(InputStream in) throws IllegalStateException, IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                str.append(line);
                str.append("\n");
            }
            in.close();
            return str.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isValidString(String strData) {

        try {
            return (strData != null && strData != "");
        } catch (Exception e) {
            return false;

        }

    }
}

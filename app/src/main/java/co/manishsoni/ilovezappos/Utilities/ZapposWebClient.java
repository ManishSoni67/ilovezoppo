package co.manishsoni.ilovezappos.Utilities;

import android.content.Context;
import android.util.Log;

import co.manishsoni.ilovezappos.Models.Products;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by manis on 2/2/2017.
 */

public class ZapposWebClient implements Callback<Products> {

    IResponseView con;
    String strURL;
    boolean isLoading;
    Call<Products> call;

    public ZapposWebClient(IResponseView _con, String baseURL) {
        this.con = _con;
        strURL = baseURL;
    }

    public void getSearchProducts(String term) {
        try {

            if (!Utils.isNetworkAvailable((Context) con)) {
                ResponseModel res = new ResponseModel();
                res.isSuceess = false;
                res.strMessage = "No Internet Available, please try again";
                con.onWebTaskException(res);
                return;
            }
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.zappos.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // prepare call in Retrofit 2.0
            ZapposProductsAPI productsAPI = retrofit.create(ZapposProductsAPI.class);
            cancelPreviousCall();
            call = productsAPI.loadProducts(term);
            call.enqueue(this);
        } catch (Exception e) {

        }
    }

    @Override
    public void onResponse(Call<Products> call, Response<Products> response) {
        try {
            ResponseModel res = new ResponseModel();
            res.isSuceess = true;
            res.products = response.body();
            callResponseViewForResuls(res);
        } catch (Exception e) {
        }


    }

    @Override
    public void onFailure(Call<Products> call, Throwable t) {
        if (call.isCanceled()) {
            Log.d(this.getClass().getName(), "Retrofit call cancelled: ");
            return;
        }

        ResponseModel res = new ResponseModel();
        res.isSuceess = false;
        res.strMessage = t.getLocalizedMessage();
        callResponseViewForExcpetions(res);
    }

    void callResponseViewForResuls(ResponseModel res) {
        if (con == null) {
            return;
        }

        con.onWebTaskCompleted(res);


    }

    void callResponseViewForExcpetions(ResponseModel res) {
        if (con == null) {
            return;
        }

        con.onWebTaskException(res);
    }

    void cancelPreviousCall() {
        if (call == null) {
            return;
        }
        call.cancel();

    }

}

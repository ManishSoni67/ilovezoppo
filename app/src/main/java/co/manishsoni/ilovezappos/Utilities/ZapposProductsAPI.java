package co.manishsoni.ilovezappos.Utilities;

/**
 * Created by manis on 2/2/2017.
 */

import co.manishsoni.ilovezappos.Models.Products;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ZapposProductsAPI {
    @GET("/Search?key=b743e26728e16b81da139182bb2094357c31d331")
    Call<Products> loadProducts(@Query("term") String term);

}

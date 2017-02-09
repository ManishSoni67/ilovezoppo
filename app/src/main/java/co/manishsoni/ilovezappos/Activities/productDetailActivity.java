package co.manishsoni.ilovezappos.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import java.io.InputStream;
import java.net.URL;

import co.manishsoni.ilovezappos.Adapters.ProductItemAdapter;
import co.manishsoni.ilovezappos.AsyncTasks.ProductImageDownLoadTask;
import co.manishsoni.ilovezappos.Models.Results;
import co.manishsoni.ilovezappos.R;
import co.manishsoni.ilovezappos.Utilities.IResponseView;
import co.manishsoni.ilovezappos.Utilities.LocalPrefecrences;
import co.manishsoni.ilovezappos.Utilities.ResponseModel;
import co.manishsoni.ilovezappos.Utilities.Utils;
import co.manishsoni.ilovezappos.Utilities.ZapposWebClient;

public class productDetailActivity extends AppCompatActivity implements IResponseView, View.OnClickListener {

    String productid;
    final Results product = new Results();
    ProgressBar _prgBar;
    boolean isBusy;
    String strQuery;
    ZapposWebClient client;
    RelativeLayout noNetworkLayout;

    RelativeLayout layout_no_data;
    public ImageView img_product;
    public TextView txt_brand_name;
    public TextView txt_product_name;
    public TextView txt_original_price;
    public TextView txt_price;
    public TextView txt_discount;
    public TextView txt_sale;
    ViewDataBinding binding;


    private Boolean isFabOpen = false;
    private FloatingActionButton fab_show, fab_share, fab_addToCart;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward, bounce_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_product_detail);
        try {

            binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
            //product = new Results();
            binding.setVariable(BR.product, product);
            noNetworkLayout = (RelativeLayout) findViewById(R.id.layout_network);
            layout_no_data = (RelativeLayout) findViewById(R.id.layout_no_data);
            _prgBar = (ProgressBar) findViewById(R.id.loading_spinner);
            img_product = (ImageView) this.findViewById(R.id.img_product);
            txt_brand_name = (TextView) this.findViewById(R.id.txt_brand_name);
            txt_product_name = (TextView) this.findViewById(R.id.txt_name);
            txt_original_price = (TextView) this.findViewById(R.id.txt_original_price);
            txt_price = (TextView) this.findViewById(R.id.txt_price);
            txt_discount = (TextView) this.findViewById(R.id.txt_discount);
            txt_sale = (TextView) this.findViewById(R.id.txt_sale);

            // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            // setSupportActionBar(toolbar);
            fab_show = (FloatingActionButton) findViewById(R.id.fab_show);
            fab_share = (FloatingActionButton) findViewById(R.id.fab_share);
            fab_addToCart = (FloatingActionButton) findViewById(R.id.fab_cart);
            fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fb_open);
            fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fb_close);
            rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
            rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
            bounce_fab = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_bounce);

            fab_show.setOnClickListener(this);
            fab_share.setOnClickListener(this);
            fab_addToCart.setOnClickListener(this);

            getIntentDataandgetProductInfo(getIntent());
        } catch (Exception e) {

            Log.d(this.getClass().getName(), "onCreate: Exception: " + e.getMessage());
        }
    }


    protected void getIntentDataandgetProductInfo(Intent intent) {
        try {
            String action = intent.getAction();
            String data = intent.getDataString();
            if (Intent.ACTION_VIEW.equals(action) && data != null) {
                Uri uri = Uri.parse(data);
                productid = uri.getQueryParameter("productId");
                strQuery = uri.getQueryParameter("term");
            } else {
                productid = getIntent().getExtras().getString("productId");
                strQuery = getIntent().getExtras().getString("term");
            }
        } catch (Exception e) {
            Log.d(this.getClass().getName(), "getIntentDataandgetProductInfo: e: " + e.getMessage());
        }
        intitateWebTask();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_detail, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            invokeShareIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void invokeShareIntent() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String sAux = "\nHey buddy check out this product on Zoppo\n\n";
            sAux = sAux + "manishsoni.co/ilovezappos?productId=" + productid + "&term=" + (strQuery == null ? "" : strQuery) + "\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
        }

    }


    @Override
    public void onWebTaskCompleted(ResponseModel response) {
        // Toast.makeText(MainActivity.this, "Products Loaded: " + response.products.results.size(), Toast.LENGTH_SHORT).show();
        try {

            boolean productFound = false;
            if (true) {
                for (Results res : response.products.results) {
                    if (res.getProductId().trim().equals(productid.trim())) {
                        product.setProperties(res.getIsBusy(), res.getStyleId(), res.getPrice(), res.getOriginalPrice(), res.getProductUrl(), res.getProductId(), res.getProductName(),
                                res.getBrandName(), res.getThumbnailImageUrl(), res.getPercentOff());
                        productFound = true;
                        break;
                    }
                }
            }
            if (productFound) {
                if (product.getIfPriceChanged()) {
                    this.txt_price.setTextColor(ContextCompat.getColor(this, R.color.discountedPriceColor));
                    this.txt_original_price.setPaintFlags(this.txt_original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    this.txt_original_price.setPaintFlags(0);
                    this.txt_price.setTextColor(ContextCompat.getColor(this, R.color.priceColor));
                }
                ProductImageDownLoadTask task = new ProductImageDownLoadTask(product, img_product);
                task.execute();
            } else {
                layout_no_data.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {

            layout_no_data.setVisibility(View.VISIBLE);
        }
        hideProgressBar();
    }

    @Override
    public void onWebTaskException(ResponseModel strError) {
        try {

            hideProgressBar();
            Snackbar.make(this.findViewById(R.id.mainCoordinatorLayout), strError.strMessage, Snackbar.LENGTH_LONG)
                    .setAction("retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            intitateWebTask();
                        }
                    }).show();
        } catch (Exception e) {
            Log.d(this.getClass().getName(), "onWebTaskException: Exception: " + e.getMessage());
        }
    }

    @Override
    public void showProgessBar() {

        if (_prgBar == null)
            return;
        isBusy = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _prgBar.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    public void hideProgressBar() {
        if (_prgBar == null)
            return;
        isBusy = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _prgBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean getIfBusy() {
        return isBusy;
    }

    @Override
    public void intitateWebTask() {

        try {
            //WebGetTask task= new WebGetTask(this,"https://api.zappos.com/Search?term="+strQuery+"&key=b743e26728e16b81da139182bb2094357c31d331");
            //task.execute();
            layout_no_data.setVisibility(View.GONE);
            if (!Utils.isNetworkAvailable(this)) {
                noNetworkLayout.setVisibility(View.VISIBLE);
                return;
            }
            noNetworkLayout.setVisibility(View.GONE);
            showProgessBar();
            if (client == null)
                client = new ZapposWebClient(this, "https://api.zappos.com");

            client.getSearchProducts(strQuery);


        } catch (Exception e) {
            Log.d(this.getClass().getName(), "intitateWebTask: Exception: " + e.getMessage());
        }

    }

    public void onRetryClicked(View v) {

        intitateWebTask();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_show:
                animateFAB_show();
                break;
            case R.id.fab_share:
                invokeShareIntent();
                break;
            case R.id.fab_cart:
                addto_cart();
                break;
        }
    }

    private void animateFAB_show() {

        if (isFabOpen) {
            isFabOpen = false;
            fab_show.startAnimation(rotate_backward);
            fab_share.startAnimation(fab_close);
            fab_addToCart.startAnimation(fab_close);
            fab_share.setClickable(false);
            fab_addToCart.setClickable(false);
        } else {
            isFabOpen = true;
            fab_show.startAnimation(rotate_forward);
            fab_share.startAnimation(fab_open);
            fab_addToCart.startAnimation(fab_open);
            fab_share.setClickable(true);
            fab_addToCart.setClickable(true);
        }
    }

    private void addto_cart() {

        String msg = "";
        try {
            fab_addToCart.startAnimation(bounce_fab);

            LocalPrefecrences localPrefecrences = new LocalPrefecrences(this);
            if (localPrefecrences.cartHasAlreadyAdded(productid)) {
                msg = getResources().getString(R.string.err_product_id_already_added);
            } else {
                if (localPrefecrences.addProductToCart(productid)) {
                    msg = getResources().getString(R.string.product_added);
                } else {
                    msg = getResources().getString(R.string.err_def_product_add_operation);
                }
            }

        } catch (Exception e) {
            msg = getResources().getString(R.string.err_def_product_add_operation);

        }

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();


    }
}

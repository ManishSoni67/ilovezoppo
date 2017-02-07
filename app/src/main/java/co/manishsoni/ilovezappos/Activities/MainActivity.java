package co.manishsoni.ilovezappos.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import co.manishsoni.ilovezappos.Adapters.ProductItemAdapter;
import co.manishsoni.ilovezappos.R;
import co.manishsoni.ilovezappos.Utilities.IResponseView;
import co.manishsoni.ilovezappos.Utilities.ResponseModel;
import co.manishsoni.ilovezappos.Utilities.Utils;
import co.manishsoni.ilovezappos.Utilities.ZapposWebClient;

public class MainActivity extends AppCompatActivity implements IResponseView {

    ProgressBar _prgBar;
    boolean isBusy;
    String strQuery;
    ZapposWebClient client;
    RecyclerView myRecyclerView;
    RelativeLayout noNetworkLayout;
    RelativeLayout layout_no_data;
    RecyclerView.Adapter<ProductItemAdapter.ProductListViewHolder> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        noNetworkLayout = (RelativeLayout) findViewById(R.id.layout_network);
        myRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layout_no_data = (RelativeLayout) findViewById(R.id.layout_no_data);
        myRecyclerView.setHasFixedSize(false);


        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            myRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            myRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        _prgBar = (ProgressBar) findViewById(R.id.loading_spinner);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        intitateWebTask();

        Intent in = new Intent(this, productDetailActivity.class);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        try {
            MenuItem item = menu.findItem(R.id.search_menu);
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d(this.getClass().getName(), "onQueryTextSubmit: " + query);
                    strQuery = query;
                    intitateWebTask();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d(this.getClass().getName(), "onQueryTextChange: " + newText);
                    strQuery = newText;
                    return false;
                }
            });
        } catch (Exception e) {
            Log.d(this.getClass().getName(), "onCreateOptionsMenu: Exception: " + e.getMessage());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onWebTaskCompleted(ResponseModel response) {
        // Toast.makeText(MainActivity.this, "Products Loaded: " + response.products.results.size(), Toast.LENGTH_SHORT).show();
        try {
            if (response.products.results == null || response.products.results.size() == 0) {
                layout_no_data.setVisibility(View.VISIBLE);
                ((ProductItemAdapter) myAdapter).clear();
            } else {
                if (myAdapter == null) {
                    myAdapter = new ProductItemAdapter(this, response.products.results, strQuery);
                    myRecyclerView.setAdapter(myAdapter);
                } else {
                    ((ProductItemAdapter) myAdapter).resetDataset(response.products.results, strQuery);
                    myAdapter.notifyDataSetChanged();
                }
            }

        } catch (Exception e) {


            layout_no_data.setVisibility(View.VISIBLE);
            ((ProductItemAdapter) myAdapter).clear();


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
                _prgBar.setVisibility(View.GONE);
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

}

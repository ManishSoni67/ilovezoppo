package co.manishsoni.ilovezappos.Adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.InputStream;
import java.net.URL;
import java.util.List;

import co.manishsoni.ilovezappos.Activities.productDetailActivity;
import co.manishsoni.ilovezappos.AsyncTasks.ProductImageDownLoadTask;
import co.manishsoni.ilovezappos.Models.Products;
import co.manishsoni.ilovezappos.Models.Results;
import co.manishsoni.ilovezappos.R;

import com.android.databinding.library.baseAdapters.BR;


/**
 * Created by manis on 2/2/2017.
 */

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ProductListViewHolder> {
    Context con;
    List<Results> products;
    String srch_term;

    public ProductItemAdapter(Context _con, List<Results> dataSet, String _srch_term) {
        con = _con;
        products = dataSet;
        srch_term = _srch_term;
    }


    @Override
    public ProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(con).inflate(R.layout.product_list_item_view, parent, false);
        ProductListViewHolder view = new ProductListViewHolder(v);
        return view;


    }

    @Override
    public void onBindViewHolder(final ProductListViewHolder holder, final int position) {
        Results data = products.get(position);
        if (data == null) {
            return;
        }
        holder.getBinding().setVariable(BR.data, data);
        holder.getBinding().executePendingBindings();
        if (data.getIfPriceChanged()) {
            holder.txt_price.setTextColor(ContextCompat.getColor(con, R.color.discountedPriceColor));
            holder.txt_original_price.setPaintFlags(holder.txt_original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.txt_price.setTextColor(ContextCompat.getColor(con, R.color.priceColor));
            holder.txt_original_price.setPaintFlags(0);
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                try {
                    Intent in = new Intent(con, productDetailActivity.class);
                    String productid = products.get(position).getProductId();
                    in.putExtra("productId", productid);
                    in.putExtra("term", srch_term);
                    con.startActivity(in);

                } catch (Exception e) {
                }

            }
        });


        ProductImageDownLoadTask task = new ProductImageDownLoadTask(data, holder.img_product);
        task.execute();


    }

    @Override
    public int getItemCount() {
        return (products == null ? 0 : products.size());
    }


    public void clear() {
        int size = getItemCount();
        if (size == 0) {
            return;
        }
        products.clear();
        notifyItemRangeChanged(0, size);

    }

    public void resetDataset(List<Results> dataSet, String _srch_term) {
        clear();
        srch_term = _srch_term;
        products.addAll(dataSet);
    }

    public static class ProductListViewHolder extends RecyclerView.ViewHolder {


        public View view;
        public ImageView img_product;
        public TextView txt_brand_name;
        public TextView txt_product_name;
        public TextView txt_original_price;
        public TextView txt_price;
        public TextView txt_discount;
        public TextView txt_sale;
        ViewDataBinding binding;

        public ProductListViewHolder(View v) {
            super(v);
            view = v;
            binding = DataBindingUtil.bind(view);
            img_product = (ImageView) v.findViewById(R.id.img_product);
            txt_brand_name = (TextView) v.findViewById(R.id.txt_brand_name);
            txt_product_name = (TextView) v.findViewById(R.id.txt_name);
            txt_original_price = (TextView) v.findViewById(R.id.txt_original_price);
            txt_price = (TextView) v.findViewById(R.id.txt_price);
            txt_discount = (TextView) v.findViewById(R.id.txt_discount);
            txt_sale = (TextView) v.findViewById(R.id.txt_sale);
        }


        public ViewDataBinding getBinding() {
            return binding;
        }


    }
}





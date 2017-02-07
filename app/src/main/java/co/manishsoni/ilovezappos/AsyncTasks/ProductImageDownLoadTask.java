package co.manishsoni.ilovezappos.AsyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.speech.tts.Voice;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

import co.manishsoni.ilovezappos.Adapters.ProductItemAdapter;
import co.manishsoni.ilovezappos.Models.Results;
import co.manishsoni.ilovezappos.Utilities.ResponseModel;

/**
 * Created by manis on 2/3/2017.
 */

public class ProductImageDownLoadTask extends AsyncTask<Void, Void, Bitmap> {
    Results product;
    ImageView img_view;


    public ProductImageDownLoadTask(Results _res, ImageView _img_view) {
        product = _res;
        img_view = _img_view;
    }

    @Override
    protected void onPostExecute(Bitmap bmp) {
        if (bmp == null) {
            return;
        }
        try {
            img_view.setImageBitmap(bmp);
        } catch (Exception e) {
        }


        super.onPostExecute(bmp);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {

            InputStream in = new URL(product.getThumbnailImageUrl()).openStream();
            return BitmapFactory.decodeStream(in);

        } catch (Exception e) {
        }

        return null;
    }
}

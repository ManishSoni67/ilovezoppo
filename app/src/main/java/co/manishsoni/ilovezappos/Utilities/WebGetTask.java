package co.manishsoni.ilovezappos.Utilities;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by manis on 1/29/2017.
 */

public class WebGetTask extends AsyncTask<String, String, ResponseModel> {

    IResponseView con;
    String strURL;
    boolean isLoading;

    public WebGetTask(IResponseView _con, String _strURL) {
        con = _con;
        strURL = _strURL;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ResponseModel res) {
        isLoading = false;
        if (con != null) {
            if (res.isSuceess) {
                con.onWebTaskCompleted(res);
            } else {
                con.onWebTaskException(res);
            }
        }
        super.onPostExecute(res);
    }

    @Override
    protected ResponseModel doInBackground(String... params) {
        ResponseModel res = new ResponseModel();
        try {
            if (!Utils.isNetworkAvailable((Context) con)) {
                res.isSuceess = false;
                res.strMessage = "No Internet Available, please try again";
                con.onWebTaskException(res);
                return res;
            }
            isLoading = true;
            HttpRequest req = new HttpRequest();
            res.strData = req.getData(strURL);
            res.isSuceess = Utils.isValidString(res.strData);

        } catch (Exception e) {
            res.isSuceess = false;
            res.strMessage = e.getMessage();
            res.strData="";
            e.printStackTrace();
        }
        return res;
    }


}

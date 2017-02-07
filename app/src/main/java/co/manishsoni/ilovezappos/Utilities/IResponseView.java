package co.manishsoni.ilovezappos.Utilities;

/**
 * Created by manis on 2/1/2017.
 */

public interface IResponseView {

    void onWebTaskCompleted(ResponseModel response);

    void onWebTaskException(ResponseModel strError);

    void showProgessBar();

    void hideProgressBar();

    boolean getIfBusy();

    void intitateWebTask();

}


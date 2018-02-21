package com.masterclass.pigakura.commoners;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.masterclass.pigakura.activities.MainActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tirgei on 7/10/17.
 */

public class NetworkChecker extends AsyncTask<Void, Void, Boolean>{
    private Context context;

    public interface NetworkResponse{
        void networkCheckComplete(Boolean result);
    }

    public NetworkResponse networkResponse = null;

    public NetworkChecker(Context context, NetworkResponse response){
        this.networkResponse = response;
        this.context = context;
    }

    private static boolean networkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()){
            if(activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
            if(activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
            }
        //return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return false;

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(networkConnection(context)){
            try{
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204")).openConnection();
                urlc.setRequestProperty("User-agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(10000);
                urlc.connect();

                return (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);

            } catch (IOException e){
                Log.d("Network error: ", "Error checking internet!");
            }
        } else {
            Log.d("Network error: ", "No network available!");
        }

        return false;
    }


    @Override
    protected void onPostExecute(Boolean result) {
        networkResponse.networkCheckComplete(result);
    }

}

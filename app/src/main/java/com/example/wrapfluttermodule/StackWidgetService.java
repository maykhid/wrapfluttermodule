package com.example.wrapfluttermodule;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import androidx.annotation.Nullable;

import com.example.wrapfluttermodule.Crypto.CryptoHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private int mCount;

    private static List<CryptoHelper> cryptoItems = new ArrayList<CryptoHelper>();
    private Context mContext;
    private int mAppWidgetId;
    private final static String TAG = "StackWidgetService";

//    private VolleyService mVolleyService;

    StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);


    }
    public void onCreate() {
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.



//        // We sleep for 3 seconds here to show how the empty view appears in the interim.
//        // The empty view is set in the StackWidgetProvider and should be a sibling of the
//        // collection view.
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Log.d(TAG, "OnCreate just ran");

    }
    public void onDestroy() {
        // In onDestroy() you should tear down anything that was setup for your data source,
        // eg. cursors, connections, etc.

        cryptoItems.clear();
        Log.d(TAG, "OnDestroy just ran");
    }
    public int getCount() {
        Log.d(TAG, "getCount just ran");
        return cryptoItems.size();
    }
    public RemoteViews getViewAt(int position) {
        // position will always range from 0 to getCount() - 1.
        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.


        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.listview_item);
        rv.setTextViewText(R.id.tvname, cryptoItems.get(position).name);
        rv.setTextViewText(R.id.tvprice, cryptoItems.get(position).price);
        // Next, we set a fill-intent which will be used to fill-in the pending intent template
        // which is set on the collection view in StackWidgetProvider.

        Bundle extras = new Bundle();
        extras.putInt(StackWidgetProvider.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);


        // setOnClickFillInIntent used to set click intent
        rv.setOnClickFillInIntent(R.id.relativelay, fillInIntent);

        // You can do heaving lifting in here, synchronously. For example, if you need to
        // process an image, fetch something from the network, etc., it is ok to do it here,
        // synchronously. A loading view will show up in lieu of the actual contents in the
        // interim.
        try {
            System.out.println("Loading view " + position);
//            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getViewAt just ran");
        // Return the remote views object.
        return rv;
    }
    public RemoteViews getLoadingView() {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        Log.d(TAG, "getLoadingView just ran");
        return null;
    }
    public int getViewTypeCount() {
        Log.d(TAG, "getViewTypeCount just ran");
        return 1;
    }
    public long getItemId(int position) {
        Log.d(TAG, "getItemId just ran");
        Log.d(TAG, "getItemId position data is: " + position);
        return position;
    }
    public boolean hasStableIds() {
        Log.d(TAG, "hasStableIds just ran");
        return true;
    }
    public void onDataSetChanged() {
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.

        VolleyService mVolleyService;
//
        mVolleyService = new VolleyService(mContext);
        mVolleyService.getDataVolley();
        Log.d(TAG, "onDataSetChanged just ran");
    }

    public static void setCrypto(@Nullable JSONObject crypto) {

        /*
        * This commented code was supposed to use Gson to convert JsonArray to
        * a list, this worked but I couldn't get price because deep in the JsonArray
        * there's a nested JsonObject to tap into to get price. I wasn't able to
        * directly tap into the JsonObject.
        *
        * I hope to find a way to this with Gson. So i'll leave the code below commented out.
        * Also, i'll leave the Gson library in the app too.
        * */

//        if (crypto == null) {
//            return;
//        }
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<CryptoHelper>>(){}.getType();
//        List<CryptoHelper> cryptoHelperList = gson.fromJson(String.valueOf(crypto), type);
//        int i = 0;
//        for (CryptoHelper l : cryptoHelperList) {
//            Log.i(TAG, l.id +" - "+l.name +" - "+l.price);
//        }
//        cryptoItems = cryptoHelperList;
//        Log.d(TAG, "This is setCrypto() data" + crypto);


        List<CryptoHelper> cryptoHelperList = new ArrayList<CryptoHelper>();
        if(crypto != null) {
            try {
                JSONArray data = crypto.getJSONArray("data");
                for (int i = 0; i < 50; i++) {
                    JSONObject coins = data.getJSONObject(i);
                    String id = coins.getString("id");
                    String name = coins.getString("name");
                    JSONObject quote = coins.getJSONObject("quote");
                    JSONObject USD = quote.getJSONObject("USD");
                    String  price = USD.getString("price");
                    cryptoHelperList.add(new CryptoHelper(Integer.parseInt(id), name, price));

                    Log.i(TAG, id +" - "+name +" - "+price);
                }
                cryptoItems = cryptoHelperList;
            } catch (final JSONException e) {
                Log.e("TAG", "JSON data parsing error :" + e.getMessage());
            }
        } else {
            Log.e("TAG", "Couldn't get json from server.");
        }
    }

}
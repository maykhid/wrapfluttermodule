package com.example.wrapfluttermodule;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.volley.VolleyError;
import com.example.wrapfluttermodule.Crypto.CryptoHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<String> language = Arrays.asList("C","C++","Java",".Net","Kotlin","Ruby","Rails","Python","Java Script","Php","Ajax","Perl","Hadoop", "henry");

    private int mCount;

    private List<WidgetItem> mWidgetItems = new ArrayList<WidgetItem>();
    private Context mContext;
    private int mAppWidgetId;

    IResult  mResultCallback = null;
    VolleyService mVolleyService;
    DatabaseHandler mDb;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        mDb = new DatabaseHandler(context);
        mCount = mDb.getAllCurrencies().size();
//        code below was used to call getcrypto function
//        mDataVoyage.getCrypto(context);
//        Log.d("TAG", "StackRemoteViewsFactory just ran");
    }
    public void onCreate() {
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.

//        String json = mDataVoyage.getCrypto(mContext);
//        Log.d("TAG", "onCreate of StackRemoteViewsFactory just ran => Result of jSon: "+mDataVoyage.getCrypto(mContext));
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, mContext);
        mVolleyService.getDataVolley();
        List<CryptoHelper> cryptos = mDb.getAllCurrencies();
//        for (int i = 0; i < mCount; i++) {
//
//            // this here loops through the word  in the loop
//            mWidgetItems.add(new WidgetItem(language.get(1) + "!"));
//
//        }
        for(CryptoHelper ch: cryptos) {
            mWidgetItems.add(new WidgetItem(ch.getCryptoName(), ch.getPriceValue()));
        }
        // We sleep for 3 seconds here to show how the empty view appears in the interim.
        // The empty view is set in the StackWidgetProvider and should be a sibling of the
        // collection view.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void onDestroy() {
        // In onDestroy() you should tear down anything that was setup for your data source,
        // eg. cursors, connections, etc.
        mWidgetItems.clear();
    }
    public int getCount() {
        return mCount;
    }
    public RemoteViews getViewAt(int position) {
        // position will always range from 0 to getCount() - 1.
        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.listview_item);
        rv.setTextViewText(R.id.tvname, mWidgetItems.get(position).coin_name);
        rv.setTextViewText(R.id.tvprice, mWidgetItems.get(position).price_value);
        // Next, we set a fill-intent which will be used to fill-in the pending intent template
        // which is set on the collection view in StackWidgetProvider.

        Bundle extras = new Bundle();
        extras.putInt(StackWidgetProvider.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setRemoteAdapter(position, R.id.listviewshow, fillInIntent);

        // setOnClickFillInIntent used to set click intent
//        rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

        // You can do heaving lifting in here, synchronously. For example, if you need to
        // process an image, fetch something from the network, etc., it is ok to do it here,
        // synchronously. A loading view will show up in lieu of the actual contents in the
        // interim.
        try {
            System.out.println("Loading view " + position);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Return the remote views object.
        return rv;
    }
    public RemoteViews getLoadingView() {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null;
    }
    public int getViewTypeCount() {
        return 1;
    }
    public long getItemId(int position) {
        return position;
    }
    public boolean hasStableIds() {
        return true;
    }
    public void onDataSetChanged() {
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
    }



    void initVolleyCallback(){
        mResultCallback = new IResult() {
            @Override
            public JSONObject notifySuccess(JSONObject response) {
//                Log.d(TAG, "Volley requester " + requestType);
                Log.d("TAG", "Volley JSON post" + response);
                return response;
            }

            @Override
            public void notifyError(VolleyError error) {
//                Log.d(TAG, "Volley requester " + requestType);
                Log.d("TAG", "Volley JSON post" + "That didn't work!");
            }
        };

    }}


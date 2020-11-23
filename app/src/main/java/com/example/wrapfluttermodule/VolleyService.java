package com.example.wrapfluttermodule;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wrapfluttermodule.Crypto.CryptoHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class VolleyService {

    private Context mContext;
    private DatabaseHandler mDb;

    VolleyService(Context context) {
        mContext = context;
        mDb = new DatabaseHandler(context);
    }
    // retrieveJsonData() takes in jsonObj, deserializes it and stores specific values
    // like name of coin and price etc in SQlite.
    void retrieveJsonData(JSONObject jsonObj) {
        if(jsonObj != null) {
            try {
                JSONArray data = jsonObj.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject coins = data.getJSONObject(i);
                    String id = coins.getString("id");
                    String name = coins.getString("name");
                    JSONObject quote = coins.getJSONObject("quote");
                    JSONObject USD = quote.getJSONObject("USD");
                    String  price = USD.getString("price");
                    mDb.insertJSON(new CryptoHelper(name, price));
                }
            } catch (final JSONException e) {
                Log.e("TAG", "JSON data parsing error :" + e.getMessage());
            }
        } else {
            Log.e("TAG", "Couldn't get json from server.");
        }
    }


    public void getDataVolley () {

        final String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue queue = Volley.newRequestQueue(mContext);

        // Creates JsonObject and assigns the parameters
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("start", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObject.put("limit", "50");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObject.put("convert", "USD");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, jsonObject, response -> {
                retrieveJsonData(response);
                Log.d("The JSON response", response.toString());
        }, error -> {
            //TODO: Handle error

        }) {
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("X-CMC_PRO_API_KEY", "32259241-51c1-4e3a-8929-05358d0f8e7c");
                return params;
            }
            //Pass Your Parameters here
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("start", "1");
//                params.put("limit", "50");
//                params.put("convert", "USD");
//                return params;
//            }
        };
        queue.add(getRequest);
    }

}

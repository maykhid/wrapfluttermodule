package com.example.wrapfluttermodule;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends AppCompatActivity {

    final String ENGINE_ID = "1";
//    private String CHANNEL = "samples.flutter.dev/battery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FlutterEngine here is initialized to  make use of Method channel
        // Why I took this approach was due to the fact that flutter was added in this app as a module
        // unlike my previous app code.

        // The MainActivity doesn't inherit the FlutterActivity in this case so tapping into it required some other
        // approach

        Log.d("Main Activity", "OnCreate just ran");

        FlutterEngine flutterEngine = new FlutterEngine(this);
        flutterEngine.getDartExecutor().executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault());

        FlutterEngineCache.getInstance().put(ENGINE_ID, flutterEngine);

        MethodChannel channel = new MethodChannel(flutterEngine.getDartExecutor(),
                "samples.flutter.dev/battery");

        channel.setMethodCallHandler(
//                ((call, result) -> {
//                    // Note: this method is invoked on the main thread.
////                            List<String> locale = call.arguments();
//                    if (call.method.equals("getBatteryLevel")) {
//                        int batteryLevel = getBatteryLevel();
//
//                        if (batteryLevel != -1) {
//                            result.success(batteryLevel);
//                            Log.d("TAG", "The value should be below");
////                                    Log.d("TAG", "Thus is passed from  flutter "+locale.get(1));
//                        } else {
//                            result.error("UNAVAILABLE", "Battery level not available.", null);
//                        }
//                    } else {
//                        result.notImplemented();
//                    }
//                })
                new MethodChannel.MethodCallHandler() {
                    @Override
                    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
                        List<String> locale = call.arguments();

                        if (call.method.equals("getBatteryLevel")) {
                            int batteryLevel = getBatteryLevel();

                            if (batteryLevel != -1) {
                                result.success(batteryLevel);
                                Log.d("TAG", "The value should be below");
                                    Log.d("TAG", "Thus is passed from  flutter "+locale.get(1));
                            } else {
                                result.error("UNAVAILABLE", "Battery level not available.", null);
                            }
                        } else {
                            result.notImplemented();
                        }
                        }
                    }
        );

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                // Start Flutter Activity

//                startActivity(
//                        FlutterActivity.createDefaultIntent(MainActivity.this)
//                );


            }
        });

//        startActivity(FlutterActivity.createDefaultIntent(MainActivity.this));
        startActivity(FlutterActivity.withCachedEngine(ENGINE_ID).build(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.stackwidgetinfo.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int getBatteryLevel() {
        int batteryLevel = -1;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        } else {
            Intent intent = new ContextWrapper(getApplicationContext()).
                    registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
                    intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        }

        return batteryLevel;
    }

//    public void getCrypto() {
//        final String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("start", "1");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            jsonObject.put("limit", "50");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            jsonObject.put("convert", "USD");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("TAG", "Response: " + response.toString());
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //TODO: Handle error
//                Log.d("ERROR","error => "+error.toString());
//            }
//        }) {
//            //This is for Headers If You Needed
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json");
//                params.put("X-CMC_PRO_API_KEY", "32259241-51c1-4e3a-8929-05358d0f8e7c");
//                return params;
//            }
//            //Pass Your Parameters here
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("start", "1");
//                params.put("limit", "50");
//                params.put("convert", "USD");
//                return params;
//            }
//
//
//    };
//        queue.add(getRequest);
//}
}

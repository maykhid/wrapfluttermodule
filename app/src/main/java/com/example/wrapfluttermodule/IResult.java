package com.example.wrapfluttermodule;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface IResult {
    public JSONObject notifySuccess (JSONObject response);
    public void notifyError (VolleyError error);
}

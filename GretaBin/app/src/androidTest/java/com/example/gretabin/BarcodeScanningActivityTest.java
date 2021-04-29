package com.example.gretabin;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class BarcodeScanningActivityTest {
    RequestQueue requestQueue;
    Context barcodeContext;
    HashMap<String, Object> responseMap;
    @Before
    public void setUp() throws Exception {
        barcodeContext = InstrumentationRegistry.getInstrumentation().getContext();
    }

    @Test
    public void onCreate() {

        requestQueue = Volley.newRequestQueue(barcodeContext);
        responseMap = lookupBarcode("00296717");
        //responseMap = new Gson().fromJson(responseJson.toString(), HashMap.class);
    }

    private HashMap<String, Object> lookupBarcode(String rawValue){

//        // Instantiate the cache
//        Cache cache = new DiskBasedCache(1024 * 1024); // 1MB cap
//
//        // Set up the network to use HttpURLConnection as the HTTP client.
//        Network network = new BasicNetwork(new HurlStack());
//
//        // Instantiate the RequestQueue with the cache and network.
//        requestQueue = new RequestQueue(cache, network);
//        // Start the queue
//        requestQueue.start();

        String url = "https://world.openfoodfacts.org/api/v0/product/" + rawValue;
        JSONObject responseObject = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responseMap=new Gson().fromJson(response.toString(), HashMap.class);
                    }
                },
        (Response.ErrorListener) error -> {
                    //TODO: Handle Error
                    error.printStackTrace();
                }
        );
        requestQueue.add(jsonObjectRequest);
        return responseMap;
    }
}
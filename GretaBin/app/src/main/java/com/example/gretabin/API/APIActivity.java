package com.example.gretabin.API;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.gretabin.BarcodeScanningActivity;
import com.example.gretabin.R;

import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class APIActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private ApiResult apiResult;
    private String url = "https://world.openfoodfacts.org/api/v0/product/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_search);
        requestQueue = Volley.newRequestQueue(this);
        lookupBarcode(getIntent().getStringExtra("Barcode + Image"));
    }

    public interface ApiResultResponseListener {
        void onApiResultResponse(ApiResult response);
    }
    private void lookupBarcode(String rawValue) {
        ApiResultRequest apiResultRequest = new ApiResultRequest(
                Request.Method.GET
                , url + rawValue
                , response -> startActivity(new Intent(this, BarcodeScanningActivity.class).putExtra("Retrieved Info", response)),
                error -> {
                    //TODO:Handle Error
                }
        );

        requestQueue.add(apiResultRequest);


    }



}

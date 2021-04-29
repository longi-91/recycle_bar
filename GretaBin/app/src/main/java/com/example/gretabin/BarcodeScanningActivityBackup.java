//
//package com.example.gretabin;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.gson.Gson;
//
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//public class BarcodeScanningActivityBackup extends AppCompatActivity {
//    private RequestQueue requestQueue;
//    private RecycledResult recycledResult;
//    private String url = "https://world.openfoodfacts.org/api/v0/product/";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.scanned_barcode);
//
//        recycledResult = new RecycledResult.RecycledResultBuilder()
//                .product(findViewById(R.id.product))
//                .recycledOutcome(findViewById(R.id.Recyclabe))
//                .outcomeGif(findViewById(R.id.imageView))
//                .productMaterial(findViewById(R.id.packaging_material_input))
//                .productType(findViewById(R.id.product_type_entry))
//                .productScore(findViewById(R.id.material_score_input))
//                .scanNew(findViewById(R.id.button))
//                .build();
//
//        requestQueue = Volley.newRequestQueue(this);
//
//        lookupBarcode(getIntent().getStringExtra("Barcode + Image"));
//
//        recycledResult.scanNew.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
//
//    }
//
//    private void lookupBarcode(String rawValue){
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET,
//                url+rawValue,
//                null,
//                (Response.Listener<JSONObject>) response -> {
//                    HashMap<String, Object> responseMap=new Gson().fromJson(response.toString(), HashMap.class);
//                    Map<String, Object> flattenedResponseMap = flattenResponse(null,responseMap);
//                    populatePage(flattenedResponseMap,recycledResult);
//                },
//                (Response.ErrorListener) error -> {
//                    //TODO: Handle Error
//                    error.printStackTrace();
//                }
//        );
//        requestQueue.add(jsonObjectRequest);
//
//    }
//
//        @RequiresApi(api = Build.VERSION_CODES.N)
//        private void populatePage (Map< String, Object > flattenedResponseMap, RecycledResult
//        recycledResult){
//            recycledResult.product.setText((CharSequence) flattenedResponseMap.getOrDefault("product.product_name", "Not Found"));
//
//            double productScore = (Double) flattenedResponseMap.getOrDefault("product.ecoscore_data.adjustments.packaging.score", 0.0);
//            if (productScore > 0) {
//                recycledResult.recycledOutcome.setText("Recyclable");
//                recycledResult.recycledOutcome.setTextColor(Color.parseColor("#42f56c"));
//                recycledResult.outcomeGif.setImageResource(R.drawable.recycle);
//            } else {
//                recycledResult.recycledOutcome.setText("Not Recyclable");
//                recycledResult.recycledOutcome.setTextColor(Color.parseColor("#eb3434"));
//                recycledResult.outcomeGif.setImageResource(R.drawable.trash);
//            }
//
//            recycledResult.productType.setText((CharSequence) flattenedResponseMap.getOrDefault("product.ecoscore_data.adjustments.packaging.packagings.0.shape", ""));
//            recycledResult.productMaterial.setText((CharSequence) flattenedResponseMap.getOrDefault("product.packaging", ""));
//            recycledResult.productScore.setText(String.valueOf(productScore));
//        }
//
//    public static Map<String, Object> flattenResponse(String parentKey, Map<String, Object> responseMap)
//    {
//        Map<String, Object> result = new HashMap<>();
//        String prefixKey = parentKey != null ? parentKey + "." : "";
//        for (Map.Entry<String, Object> entry : responseMap.entrySet()) {
//            if (entry.getValue() instanceof String) {
//                result.put(prefixKey + entry.getKey(), (String)entry.getValue());
//            }
//            if (entry.getValue() instanceof Number) {
//                result.put(prefixKey + entry.getKey(), (Number)entry.getValue());
//            }
//            if(entry.getValue() instanceof Boolean){
//                result.put(prefixKey + entry.getKey(),(Boolean)entry.getValue());
//            }
//            if (entry.getValue() instanceof List) {
//                List list = (List) entry.getValue();
//                for (int i = 0; i < list.size(); i++) {
//                    String key = prefixKey + entry.getKey() + "." + i;
//                    Object value = list.get(i);
//                    if (value instanceof Map) {
//                        result.putAll(flattenResponse(key, (Map<String, Object>) value));
//                    }
//                    else {
//                        result.put(key, value);
//                    }
//                }
//            }
//            if (entry.getValue() instanceof Map) {
//                result.putAll(flattenResponse(prefixKey + entry.getKey(), (Map<String, Object>)entry.getValue()));
//            }
//        }
//        return result;
//    }
//
//    }
//

package com.example.gretabin.API;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ApiResultRequest extends Request<ApiResult> {
    APIActivity.ApiResultResponseListener apiResultResponseListener;

    public ApiResultRequest(int method, String url, APIActivity.ApiResultResponseListener apiResultResponseListener, Response.ErrorListener errorListener){
        super(method, url, errorListener);
        this.apiResultResponseListener = apiResultResponseListener;
    }

    @Override
    protected Response<ApiResult> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
                    HashMap<String, Object> responseMap = new Gson().fromJson(json, HashMap.class);
                    Map<String,Object> flattenedResponse = flattenResponse(null, responseMap);
                    return Response.success(buildRecycledInfo(flattenedResponse), HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    //Response comes in here
    @Override
    protected void deliverResponse(ApiResult response) {
        apiResultResponseListener.onApiResultResponse(response);
    }

    public static Map<String, Object> flattenResponse(String parentKey, Map<String, Object> responseMap)
    {
        Map<String, Object> result = new HashMap<>();
        String prefixKey = parentKey != null ? parentKey + "." : "";
        for (Map.Entry<String, Object> entry : responseMap.entrySet()) {
            if (entry.getValue() instanceof String) {
                result.put(prefixKey + entry.getKey(), (String)entry.getValue());
            }
            if (entry.getValue() instanceof Number) {
                result.put(prefixKey + entry.getKey(), (Number)entry.getValue());
            }
            if(entry.getValue() instanceof Boolean){
                result.put(prefixKey + entry.getKey(),(Boolean)entry.getValue());
            }
            if (entry.getValue() instanceof List) {
                List list = (List) entry.getValue();
                for (int i = 0; i < list.size(); i++) {
                    String key = prefixKey + entry.getKey() + "." + i;
                    Object value = list.get(i);
                    if (value instanceof Map) {
                        result.putAll(flattenResponse(key, (Map<String, Object>) value));
                    }
                    else {
                        result.put(key, value);
                    }
                }
            }
            if (entry.getValue() instanceof Map) {
                result.putAll(flattenResponse(prefixKey + entry.getKey(), (Map<String, Object>)entry.getValue()));
            }
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ApiResult buildRecycledInfo (Map< String, Object > flattenedResponseMap){
        return ApiResult.builder()
                .product((String) flattenedResponseMap.getOrDefault("product.product_name", "Not Found"))
                .productScore((Double) flattenedResponseMap.getOrDefault("product.ecoscore_data.adjustments.packaging.score", 0.0))
                .productMaterial((String) flattenedResponseMap.getOrDefault("product.ecoscore_data.adjustments.packaging.packagings.0.shape", ""))
                .productType((String) flattenedResponseMap.getOrDefault("product.packaging", ""))
                .build();
//        apiResult.setProduct((String) flattenedResponseMap.getOrDefault("product.product_name", "Not Found"));
//        apiResult.setProductScore((Double) flattenedResponseMap.getOrDefault("product.ecoscore_data.adjustments.packaging.score", 0.0));
//        apiResult.setProductMaterial((String) flattenedResponseMap.getOrDefault("product.ecoscore_data.adjustments.packaging.packagings.0.shape", ""));
//        apiResult.setProductType((String) flattenedResponseMap.getOrDefault("product.packaging", ""));
    }

}

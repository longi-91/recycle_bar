package com.example.gretabin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gretabin.API.ApiResult;


public class BarcodeScanningActivity extends AppCompatActivity {
    private RecycledResult recycledResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scanned_barcode);

        recycledResult = new RecycledResult.RecycledResultBuilder()
                .product(findViewById(R.id.product))
                .recycledOutcome(findViewById(R.id.Recyclabe))
                .outcomeGif(findViewById(R.id.imageView))
                .productMaterial(findViewById(R.id.packaging_material_input))
                .productType(findViewById(R.id.product_type_entry))
                .productScore(findViewById(R.id.material_score_input))
                .scanNew(findViewById(R.id.button))
                .build();

        populatePage(getIntent().getParcelableExtra("Retrieved Info"));

        recycledResult.scanNew.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));

    }


        @RequiresApi(api = Build.VERSION_CODES.N)
        private void populatePage (ApiResult apiResult){
            recycledResult.product.setText((CharSequence) apiResult.getProduct());

            double productScore = apiResult.getProductScore();
            if (productScore > 0) {
                recycledResult.recycledOutcome.setText("Recyclable");
                recycledResult.recycledOutcome.setTextColor(Color.parseColor("#42f56c"));
                recycledResult.outcomeGif.setImageResource(R.drawable.recycle);
            } else {
                recycledResult.recycledOutcome.setText("Not Recyclable");
                recycledResult.recycledOutcome.setTextColor(Color.parseColor("#eb3434"));
                recycledResult.outcomeGif.setImageResource(R.drawable.trash);
            }

            recycledResult.productType.setText((CharSequence) apiResult.getProductType());
            recycledResult.productMaterial.setText((CharSequence) apiResult.getProductMaterial());
            recycledResult.productScore.setText(String.valueOf(productScore));
        }

    }

package com.example.gretabin;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecycledResult {
    TextView product;
    TextView recycledOutcome;
    TextView productType;
    TextView productMaterial;
    TextView productScore;
    ImageView outcomeGif;
    Button scanNew;

}

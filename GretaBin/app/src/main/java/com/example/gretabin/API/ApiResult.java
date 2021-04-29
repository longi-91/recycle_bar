package com.example.gretabin.API;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResult implements Parcelable {
    String product;
    double productScore;
    String productType;
    String productMaterial;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.product);
        dest.writeDouble(this.productScore);
        dest.writeString(this.productType);
        dest.writeString(this.productMaterial);
    }

    public void readFromParcel(Parcel source) {
        this.product = source.readString();
        this.productScore = source.readDouble();
        this.productType = source.readString();
        this.productMaterial = source.readString();
    }

    protected ApiResult(Parcel in) {
        this.product = in.readString();
        this.productScore = in.readDouble();
        this.productType = in.readString();
        this.productMaterial = in.readString();
    }

    public static final Creator<ApiResult> CREATOR = new Creator<ApiResult>() {
        @Override
        public ApiResult createFromParcel(Parcel source) {
            return new ApiResult(source);
        }

        @Override
        public ApiResult[] newArray(int size) {
            return new ApiResult[size];
        }
    };
}

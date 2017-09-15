package com.ideofuzion.btm.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by khali on 6/7/2017.
 */

public class QRModel {
    public String getPublicBitcoinId() {
        return publicBitcoinId;
    }

    public void setPublicBitcoinId(String publicBitcoinId) {
        this.publicBitcoinId = publicBitcoinId;
    }

    @SerializedName("bitcoin")
    String publicBitcoinId;


}

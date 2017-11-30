package com.ideofuzion.btm.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by khali on 6/7/2017.
 */

public class QRModel {
    public String getBitcoin() {
        return bitcoin;
    }

    public void setBitcoin(String bitcoin) {
        this.bitcoin = bitcoin;
    }

    @SerializedName("bitcoin")
    String bitcoin;


}

package com.ideofuzion.btm.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ideofuzion on 6/7/2017.
 *
 * this class act as model for data extracted from qr code and
 * the data resides in the object of this class
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

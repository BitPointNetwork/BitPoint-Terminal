package com.ideofuzion.btm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Zohaib Khaliq on 4/11/2017.
 */

public class BitPointUser implements Parcelable {


    @SerializedName("_id")
    private String userId;
    @SerializedName("userName")
    private String ethUserName;
    @SerializedName("userAddress")
    private String addresss;
    @SerializedName("userOccupation")
    private String occupation;
    @SerializedName("userContactNumber")
    private String mobileNum;
    @SerializedName("userProfilePictureURL")
    private String profilePicUrl;
    @SerializedName("userFullName")
    private String fullName;
    @SerializedName("channel")
    private String channel;
    @SerializedName("userEmail")
    private String emailAddress;
    @SerializedName("userProfileStatus")
    private int profileStatus;
    @SerializedName("ethereumUserApplicationToken")
    private String authToken;
    @SerializedName("ethereumUserLoginDetail")
    private ArrayList<String> bitPointUserLastLoginDetail;
    @SerializedName("ethereumUserPasscodeStatus")
    private int passcodeStatus;
    @SerializedName("ethereumUserNotificationStatus")
    private int bitPointNotificationStatus;
    @SerializedName("isUserMerchantMode")
    private int bitPointMerchantStatus;
    @SerializedName("ethereumUserDoubleAuthenticationMode")
    private int bitPointDoubleAuthenticationStatus;
    @SerializedName("userEthereumId")
    private String bitcoinPublicKey;
    @SerializedName("bitcoinCurrencyRate")
    private String bitcoinDollarRate;
    @SerializedName("lat")
    private String userLat;
    @SerializedName("lng")
    private String userLng;


    @SerializedName("tagLine")
    private String tagLine;

    @SerializedName("userRole")
    private String userRole;



    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }
    public String getBitcoinPublicKey() {
        return bitcoinPublicKey;
    }

    public void setBitcoinPublicKey(String bitcoinPublicKey) {
        this.bitcoinPublicKey = bitcoinPublicKey;
    }

    public String getBitcoinDollarRate() {
        return bitcoinDollarRate;
    }

    public void setBitcoinDollarRate(String bitcoinDollarRate) {
        this.bitcoinDollarRate = bitcoinDollarRate;
    }


    //@SerializedName("transactionsByDays")
    // private ArrayList<Entry> transactionGraphData;
    protected BitPointUser(Parcel in) {
        userId = in.readString();
        ethUserName = in.readString();
        addresss = in.readString();
        occupation = in.readString();
        mobileNum = in.readString();
        profilePicUrl = in.readString();
        fullName = in.readString();
        channel = in.readString();
        emailAddress = in.readString();
        profileStatus = in.readInt();
        authToken = in.readString();
        bitPointUserLastLoginDetail = in.createStringArrayList();
        passcodeStatus = in.readInt();
        bitPointNotificationStatus = in.readInt();
        bitPointMerchantStatus = in.readInt();
        bitPointDoubleAuthenticationStatus = in.readInt();
        bitcoinPublicKey = in.readString();
        bitcoinDollarRate = in.readString();
        userLat = in.readString();
        userLng = in.readString();
        userRole = in.readString();
        tagLine = in.readString();
    }

    public String getUserRole() {
        return userRole;
    }
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
    public int getBitPointMerchantStatus() {
        return bitPointMerchantStatus;
    }

    public void setBitPointMerchantStatus(int bitPointMerchantStatus) {
        this.bitPointMerchantStatus = bitPointMerchantStatus;
    }

    public static final Creator<BitPointUser> CREATOR = new Creator<BitPointUser>() {
        @Override
        public BitPointUser createFromParcel(Parcel in) {
            return new BitPointUser(in);
        }

        @Override
        public BitPointUser[] newArray(int size) {
            return new BitPointUser[size];
        }
    };
    public String getChannel() {
        return channel;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public BitPointUser() {
        ethUserName =
                addresss = this.occupation = this.mobileNum = this.profilePicUrl = this.fullName = this.emailAddress = authToken = "";
    }//end of constructor


    public int getBitPointNotificationStatus() {
        return bitPointNotificationStatus;
    }

    public void setBitPointNotificationStatus(int bitPointNotificationStatus) {
        this.bitPointNotificationStatus = bitPointNotificationStatus;
    }

    public int getBitPointDoubleAuthenticationStatus() {
        return bitPointDoubleAuthenticationStatus;
    }

    public void setBitPointDoubleAuthenticationStatus(int bitPointDoubleAuthenticationStatus) {
        this.bitPointDoubleAuthenticationStatus = bitPointDoubleAuthenticationStatus;
    }

    public int getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(int profileStatus) {
        this.profileStatus = profileStatus;
    }

    public String getBitPointUserName() {
        return ethUserName;
    }

    public void setEthUserName(String ethUserName) {
        this.ethUserName = ethUserName;
    }

    public String getAddresss() {
        return addresss;
    }

    public void setAddresss(String addresss) {
        this.addresss = addresss;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public ArrayList<String> getBitPointUserLastLoginDetail() {
        return bitPointUserLastLoginDetail;
    }


    public void setBitPointUserLastLoginDetail(ArrayList<String> bitPointUserLastLoginDetail) {
        this.bitPointUserLastLoginDetail = bitPointUserLastLoginDetail;
    }

    public int getPasscodeStatus() {
        return passcodeStatus;
    }

    public void setPasscodeStatus(int passcodeStatus) {
        this.passcodeStatus = passcodeStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(ethUserName);
        dest.writeString(addresss);
        dest.writeString(occupation);
        dest.writeString(mobileNum);
        dest.writeString(profilePicUrl);
        dest.writeString(fullName);
        dest.writeString(channel);
        dest.writeString(emailAddress);
        dest.writeInt(profileStatus);
        dest.writeString(authToken);
        dest.writeStringList(bitPointUserLastLoginDetail);
        dest.writeInt(passcodeStatus);
        dest.writeInt(bitPointNotificationStatus);
        dest.writeInt(bitPointMerchantStatus);
        dest.writeInt(bitPointDoubleAuthenticationStatus);
        dest.writeString(bitcoinPublicKey);
        dest.writeString(bitcoinDollarRate);
        dest.writeString(userLat);
        dest.writeString(userLng);
        dest.writeString(userRole);
        dest.writeString(tagLine);
    }

    /*public ArrayList<Entry> getTransactionGraphData() {
        return transactionGraphData;
    }

    public void setTransactionGraphData(ArrayList<Entry> transactionGraphData) {
        this.transactionGraphData = transactionGraphData;
    }*/

}

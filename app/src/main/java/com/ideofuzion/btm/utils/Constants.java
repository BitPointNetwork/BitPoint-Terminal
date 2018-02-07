package com.ideofuzion.btm.utils;

/**
 * Created by ideofuzuion on 4/8/2017.
 *
 * this class has all static members and these
 * incclude urls static message and other data
 * member that are used all around the application
 */

public class Constants {
    public static final String ERROR_EMPTY_EMAIL = "Please enter your email";
    public static final String ERROR_EMAIL_FORMAT = "Email is badly formatted";
    public static final String ERROR_EMPTY_PASSWORD = "Please enter your password";

    public static final String ERROR_PASSWORD_LENGTH = "Password should have at minimum 6 characters";
    public static final String ERROR_EMPTY_CONFIRMPASSWORD = "Please enter confirm password";
    public static final String ERROR_NO_PASSWORD_MATCH = "Password doesn't match";
    public static final String ROUTE_ADD_PASSCODE = "merchant/updateUserPin";
    public static final String ROUTE_VERIFY_PASSCODE = "merchant/verifyMerchantPin";
    public static final String ROUTE_UPDATE_MIN_MAX_BALANCE = "merchant/updateMinMaxBalance";
    public static final String ROUTE_UPDATE_KRAKEN_KEYS = "merchant/updateUserKrakenSetup";
    public static final String ROUTE_UPDATE_USE_KRAKEN = "merchant/updateUseKraken";
    public static final String ROUTE_CREATE_PROFIT_WALLET = "merchant/createMerchantProfitWallet";
    public static final String ROUTE_UPDATE_MERCHANT_PROFIT = "merchant/updateMerchantProfit";
    public static final String ROUTE_CREATE_BITPOINT_PROFIT_WALLET = "merchant/createBitPointProfitWallet";
    public static final String ROUTE_UPDATE_MERCHANT_PROFIT_THRESHOLD = "merchant/updateUserProfitThresholdSetup";
    public static final String ROUTE_UPDATE_HOT_WALLET_BENEFICIARY = "merchant/updateHotWalletBenificiaryKey";
    public static final String ROUTE_SAVE_SENDER_ADDRESS =  "merchant/postSaveSenderAddress";
    public static final String ROUTE_UPDATE_LAT_LNG = "merchant/postUpdateLatLong";


    public static final String BASE_SERVER_URL ="http://185.168.194.65:3000/";
    public static final String ROUTE_USER_SIGNUP = "merchant/createMerchant";
    public static final String ROUTE_USER_LOGIN = "merchant/adminLogin";
    public static final String BITCOIN_SYMBOL = "฿";
    public static final String POUND_SYMBOL = "£";
    public static final String ERROR_EMPTY_MOBILE_NUMBER = "Please enter phone number";


    public static String ERROR_EMPTY_NAME = "Please enter your name";
    public static String ERROR_NO_INTERNET = "No Internet Connection";

    public static String ERROR_CHECK_INTERNET = "Check Internet Connection";

    public static final String SEND_BALANCE = "merchant/sendBalance";
    public static String CURRENCY = " GBP";

    public interface ResultCode {
        public static int CODE_SUCCESS = 200;
    }


}

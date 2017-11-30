package com.ideofuzion.btm.utils;

/**
 * Created by Zohaib Khaliq on 4/8/2017.
 */

public class Constants {
    public static final String KEY_COUNTRY_POSITION_SELECTED = "KEY_COUNTRY_POSITION_SELECTED";
    public static final String ERROR_EMPTY_EMAIL = "Please enter your email";
    public static final String ERROR_EMAIL_FORMAT = "Email is badly formatted";
    public static final String ERROR_EMPTY_PASSWORD = "Please enter your password";
    public static final String ERROR_EMPTY_CONFIRM_PASSWORD = "Please enter your confirm password";

    public static final String ERROR_PASSWORD_LENGTH = "Password should have at minimum 6 characters";
    public static final String WEAK_PASSWORD = "WEAK PASSWORD";
    public static final String STRONG_PASSWORD = "STRONG PASSWORD";
    public static final String ERROR_PASSWORD_WEAK_STRENGTH = "Password strength is weak";
    public static final String ERROR_EMPTY_MOBILE_NUMBER = "Please enter your mobile number";
    public static final String CHECK_YOUR_EMAIL = "Please check your email";
    public static final String ERROR_EMPTY_OLDPASSWORD = "Please enter old password";
    public static final String ERROR_EMPTY_NEWPASSWORD = "Please enter new password";
    public static final String ERROR_EMPTY_CONFIRMPASSWORD = "Please enter confirm password";
    public static final String ERROR_NO_PASSWORD_MATCH = "Password doesn't match";
    public static final String ROUTE_CHANGE_PASSWORD = "ethereumUserChangePassword";
    public static final String ROUTE_MERCHANT_TOGGLE = "postEthereumUsersChangeMerchantMode";
    public static final String ROUTE_USER_BY_PUBLIC_KEY = "getUserByPublicKey?publicKey=";
    public static final String ROUTE_UPDATE_TAGLINE = "updateTagline";
    public static final String ROUTE_ADD_PASSCODE = "merchant/updateUserPin";
    public static final String ROUTE_UPDATE_PASSCODE = "ethereumUserChangePasscode";
    public static final String ROUTE_VERIFY_PASSCODE = "merchant/verifyMerchantPin";
    public static final String ROUTE_SEND_BITCOINS = "sendBitcoin";
    public static final String TRANSACTION_TYPE_MERCHANTS = "2";
    public static final String ROUTE_UPDATE_MIN_MAX_BALANCE = "merchant/updateMinMaxBalance";
    public static final String ROUTE_UPDATE_KRAKEN_KEYS = "merchant/updateUserKrakenSetup";
    public static final String ROUTE_UPDATE_USE_KRAKEN = "merchant/updateUseKraken";
    public static final String ROUTE_CREATE_PROFIT_WALLET = "merchant/createMerchantProfitWallet";
    public static final String ROUTE_UPDATE_MERCHANT_PROFIT = "merchant/updateMerchantProfit";
    public static final String ROUTE_CREATE_BITPOINT_PROFIT_WALLET = "merchant/createBitPointProfitWallet";
    public static final String ROUTE_UPDATE_MERCHANT_PROFIT_THRESHOLD = "merchant/updateUserProfitThresholdSetup";
    public static final String ROUTE_UPDATE_HOT_WALLET_BENEFICIARY = "merchant/updateHotWalletBenificiaryKey";
    public static final String ROUTE_RECEIVE_BALANCE = "merchant/receiveBalance";
    public static final String ROUTE_LISTENER = "merchant/attachListener";
    public static final String ROUTE_SAVE_SENDER_ADDRESS =  "merchant/postSaveSenderAddress";
    public static final String ROUTE_UPDATE_LAT_LNG = "merchant/postUpdateLatLong";

    public static String EXTRA_MOBILE_NUMBER = "EXTRA_MOBILE_NUMBER";
    public static int COUNTRY_SELECT_REQUEST = 1;

/*35.189.115.14:3000*/
    public static final String BASE_SERVER_URL = "http://35.198.174.113:3000/";
    public static final String ROUTE_USER_SIGNUP = "merchant/createMerchant";
    public static final String ROUTE_COMPLETE_USERR_PROFILE = "ethereumUserCompleteProfile";
    public static final String ROUTE_SOCIAL_MEDIA_SIGNUP = "postSocialMediaUser";
    public static final String ROUTE_GET_ALL_USERS = "getEthereumUsers";
    public static final String ROUTE_USER_LOGIN = "merchant/adminLogin";
    public static final String ROUTE_VERIFY_MOBILE_CODE = "ethereumUserMobileChange";
    public static final String ROUTE_ADD_MOBILE_NUMBER = "ethereumUserMobileCode";
    public static final String ROUTE_TERMS_CONDITION = "getTermsAndConditionsText";
    public static final String ROUTE_ABOUT_US = "getAboutText";
    public static final String ROUTE_DOUBLE_AUTHENTICATE_TOGGLE = "ethereumUsersChangeDoubleAuthentication";
    public static final String ROUTE_NOTIFICATION_TOGGLE = "ethereumUsersChangeNotificationStatus";
    public static final String ROUTE_PASSCODE_STATUS = "ethereumUsersChangePasscodeStatus";
    public static final String ROUTE_CONTACT_PROFILE = "getUserByContactNumber";
    public static final String ROUTE_FORGOT_PASSWORD = "forgotPassword";
    public static final String BITCOIN_SYMBOL = "฿";
    public static final String POUND_SYMBOL = "£";

    public static final String ARG_ACCOUNT_TYPE = "ARG_ACCOUNT_TYPE";
    public static final String ARG_AUTH_TYPE = "ARG_AUTH_TYPE";
    public static final String ARG_IS_ADDING_NEW_ACCOUNT = "ARG_IS_ADDING_NEW_ACCOUNT";
    public static final String ARG_IS_USER_FROM_SETTING = "ARG_IS_USER_FROM_SETTING";
    public static final String CHAT_SERVER_URL = "http://bitpointserver.azurewebsites.net/";

    public static String ERROR_EMPTY_NAME = "Please enter your name";
    public static String ERROR_NO_INTERNET = "No Internet Connection";

    public static String ERROR_CHECK_INTERNET = "Check Internet Connection";
    public static String ERROR_EMPTY_EMAIL_SOCIAL = "Email address not accessible";
    public static String ALLOW_ACCESS = "ALLOW_ACCESS";
    public static String EXTRA_USER_ID = "data2";
    public static String EXTRA_USERNAME = "data1";
    public static String EXTRA_USER_NUMBER = "data4";
    public static String Error_EMPTY_MESSAGE = "Please enter some text in message";
    public static String ROUTE_NEAREST_MERCHANTS = "getNearestMerchants";
    public static String ROUTE_UPDATE_LOCATION = "updateUserLocation";
    public static String ROUTE_GET_CHART_DATA = "getBitcoinToUSDChartRate?mode=";
    public static String ROUTE_DAILY = "day";
    public static String ROUTE_WEEKLY = "weekly";
    public static String ROUTE_MONTHLY = "monthly";
    public static String ROUTE_YEARLY = "yearly";

    public static String ERROR_SAME_OLD_AND_NEW_PASSWORD = "Old password and new password shouldn't be same ";
    public static final String SEND_BALANCE = "merchant/sendBalance";
    public static String CURRENCY = " GBP";

    public interface ResultCode {
        public static int CODE_SUCCESS = 200;
        public static int CODE_VERIFICATION_MESSAGE_SUCCESS = 201;
        public static int CODE_PASSWORD_MISMATCH = 401;
        public static int CODE_NOT_FOUND = 400;
        public static int CODE_EMAIL_NOT_VERIFIED = 408;
        public static int CODE_EMAIL_ALREADY_EXISTS = 409;
        public static int CODE_USER_ALREADY_EXISTS = 500;
        public static int CODE_FAILURE = 300;
        public static int CODE_LINK_EXPIRED = 600;

        //TODO set this in code in node backend service and get response in CODE format
        public static final int MOBILE_NUMBER_VERIFIED = 2;
        public static final int MOBILE_NUMBER_NOT_VERIFIED = 1;
    }

    public interface SyncAdapterConst {
        String ACTION = "ethereumUserMobileNumberSync";

        interface Params {
            String PARAM_CONTACTS_DATA = "mobileNumberList";
            String PARAM_AUTH_TOKEN = "authToken";
            String PARAM_USERNAME = "userName";
            String PARAM_USER_ID = "userId";

            String PARAM_SYNC_STATE = "";
        }

        interface BroadcastActions {
            String ACTION = "SyncContactsResponse";
            String RESPONSE_CODE = "SyncContactsResponseCode";
            String RESPONSE_CODE_SUCCESS = "SyncContactsResponseSuccess";
            String RESPONSE_CODE_FAILURE = "SyncContactsResponseFailure";
        }
    }

    public interface LocalContactSyncServices {
        String ACTION = "updateContactsList";
    }

    public interface SettingActivityConst {
        public enum SwitchStatus {
            SWITCH_ON(200),
            SWITCH_OFF(300),
            SWITCH_OFF_NOT_SET(100);

            private final int value;

            SwitchStatus(final int newValue) {
                value = newValue;
            }//end of

            public int getValue() {
                return value;
            }
        }
    }//end of interface settings

}

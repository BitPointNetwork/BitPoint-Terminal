package com.ideofuzion.btm.network;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ideofuzion.btm.BTMApplication;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * this is class to handle all network calls
 * made from the android device
 *
 */
public class VolleyRequestHelper {

    /**
     * send request to url from this function
     *
     * @param url              url for server
     * @param requestParams    all request params to send through
     * @param callbackListners to send response to callback either error or getting response result
     */
    public static void sendPostRequestWithParam(String url, final Map<String, String> requestParams, Object callbackListners) {
        if (callbackListners instanceof Response.Listener && callbackListners instanceof Response.ErrorListener) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(requestParams),
                    (Response.Listener<JSONObject>) callbackListners, (Response.ErrorListener) callbackListners);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BTMApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
        }//end of if for sender have already implement listners
        else {
            throw new IllegalArgumentException("Implment callbacks for volley for JSONObject as a template class");
        }
    }//end of func

    public static void getRequestWithParams(String url, final Map<String, String> requestParams, Object callbackListners) {
        if (callbackListners instanceof Response.Listener && callbackListners instanceof Response.ErrorListener) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(requestParams),
                    (Response.Listener<JSONObject>) callbackListners, (Response.ErrorListener) callbackListners);

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            BTMApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
        }//end of if for sender have already implement listners
        else {
            throw new IllegalArgumentException("Implment callbacks for volley for JSONObject as a template class");
        }
    }

    /**
     * send request to url from this function
     *
     * @param url                      url for server
     * @param requestParams            all request params to send through
     * @param callbackResponseListners to send response to callback either error or getting response result
     */
    public static void sendPostRequestWithParamCallback(String url, final Map<String, String> requestParams,
                                                        Response.Listener<JSONObject> callbackResponseListners,
                                                        Response.ErrorListener callbackResponseErrorListners) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(requestParams),
                callbackResponseListners, callbackResponseErrorListners);

        BTMApplication.getInstance().getRequestQueue().add(jsonObjectRequest);

    }//end of class

    /*
         /**
          * send request to url from this function
          *
          * @param url                      url for server
          * @param requestParams            all request params to send through
          * @param callbackResponseListners to send response to callback either error or getting response result
          */
  /*  public static void sendImageRequestWithParamCallback(String url, final Map<String, String> requestParams,
                                                         Response.Listener<NetworkResponse> callbackResponseListners,
                                                         Response.ErrorListener callbackResponseErrorListners, final File profileImage) {
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, callbackResponseListners,
                callbackResponseErrorListners) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return requestParams;
            }//end of params

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                try {
                    if (profileImage != null) {
                        InputStream inputStream = new FileInputStream(profileImage);
                        params.put("file", new DataPart(profileImage.getName(), IOUtils.toByteArray(inputStream), "image/jpeg"));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return params;
            }
        };

        BitPointApplication.getInstance().getRequestQueue().add(multipartRequest);

    }//end of class
*/
    public static String getAuthTokenFromServer(String userName, String password, String accountType) {
        String authToken = "";

        return authToken;
    }
}//ennd of class

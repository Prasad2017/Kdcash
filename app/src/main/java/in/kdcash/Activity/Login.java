package in.kdcash.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import in.kdcash.Extra.Common;
import in.kdcash.Extra.DetectConnection;
import in.kdcash.Model.LoginResponse;
import in.kdcash.Model.OTPResponse;
import in.kdcash.R;
import in.kdcash.Retrofit.Api;
import in.kdcash.helper.AppSignatureHelper;
import in.kdcash.interfaces.OtpReceivedInterface;
import in.kdcash.receiver.SmsBroadcastReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        OtpReceivedInterface, GoogleApiClient.OnConnectionFailedListener {


    @BindViews({R.id.mobileNumber})
    List<FormEditText> formEditTexts;
    @BindViews({R.id.mobileLayout})
    List<CardView> cardViews;
    @BindView(R.id.signUp)
    TextView textView;
    @BindView(R.id.otpView)
    OtpView otpView;
    GoogleApiClient mGoogleApiClient;
    SmsBroadcastReceiver mSmsBroadcastReceiver;
    private String HASH_KEY, OTP, otpCode;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        formEditTexts.get(0).setSelection(formEditTexts.get(0).getText().toString().length());

        // init broadcast receiver
        mSmsBroadcastReceiver = new SmsBroadcastReceiver();

        //set google api client for hint request
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        mSmsBroadcastReceiver.setOnOtpListeners(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                try {
                    otpCode = otp;
                    int length = otpCode.trim().length();
                    if (length == 4) {

                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        View view = getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        File file = new File("data/data/in.kdcash/shared_prefs/user.xml");
        if (file.exists()) {
            Intent intent = new Intent(Login.this, MainPage.class);
            startActivity(intent);
            finish();
        }

    }

    @OnClick({R.id.signUp})
    public void onClick(View view){

        switch (view.getId()){

            case R.id.signUp:
                if (DetectConnection.checkInternetConnection(Login.this)) {

                    if (textView.getText().toString().equalsIgnoreCase("Send OTP")) {
                        if (formEditTexts.get(1).testValidity()) {
                            CheckMobile(formEditTexts.get(1).getText().toString().trim());
                        }
                    } else if (textView.getText().toString().equalsIgnoreCase("Verify")) {
                        if (otpCode.equalsIgnoreCase(OTP)) {

                            pref = getSharedPreferences("user", Context.MODE_PRIVATE);
                            editor = pref.edit();
                            editor.putString("UserLogin", "UserLoginSuccessful");
                            editor.commit();

                            Common.saveUserData(Login.this, "accessToken", "accessToken".replace("\"", ""));
                            Common.saveUserData(Login.this, "userId", "29");
                            Intent intent = new Intent(Login.this, MainPage.class);
                            startActivity(intent);
                            finishAffinity();

                        } else {
                            Toasty.error(Login.this, "Enter valid otp", Toasty.LENGTH_SHORT).show();
                            otpView.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Toasty.warning(Login.this, "No Internet Connection", Toasty.LENGTH_SHORT, true).show();
                }

                break;
        }


    }

    private void login(String toString, String toString1) {
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onOtpReceived(String otp) {

    }

    @Override
    public void onOtpTimeout() {

    }


    private void CheckMobile(String mobileNumber) {

        ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Mobile number is checking");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        Call<LoginResponse> call = Api.getClient().checkMobileNumber(mobileNumber);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.body().getSuccess().booleanValue()==true){
                    progressDialog.dismiss();
                    if (DetectConnection.checkInternetConnection(Login.this)) {
                        sendOTP(mobileNumber);
                    } else {
                        Toasty.warning(Login.this, "No Internet Connection", Toasty.LENGTH_SHORT, true).show();
                    }
                } else if (response.body().getSuccess().booleanValue()==false){
                    progressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("MobileNumberError", ""+t.getMessage());
            }
        });

    }

    private void sendOTP(String mobileNumber) {

        ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("OTP is sending");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        HASH_KEY = (String) new AppSignatureHelper(this).getAppSignatures().get(0);
        HASH_KEY = HASH_KEY.replace("+", "%252B");
        OTP= new DecimalFormat("000000").format(new Random().nextInt(999999));
        String message = "<#> Your Anypayments verification OTP code is "+ OTP +". Please DO NOT share this OTP with anyone.\n" + HASH_KEY;
        //Your authentication key
        String authkey = "YourAuthKey";
        //Multiple mobiles numbers separated by comma
        String mobiles = "9999999";
        //Sender ID,While using route4 sender id should be 6 characters long.
        String senderId = "DEMOOO";
        //define route
        String route="default";

        URLConnection myURLConnection=null;
        URL myURL=null;
        BufferedReader reader=null;
        //encoding message
        String encoded_message= URLEncoder.encode(message);
        //Send SMS API
        String mainUrl="http://198.15.103.106/API/pushsms.aspx?";
        //Prepare parameter string
        StringBuilder sbPostData= new StringBuilder(mainUrl);
        sbPostData.append("loginID=DEMOTEST");
        sbPostData.append("&password=Zplus@123");
        sbPostData.append("&mobile="+mobileNumber);
        sbPostData.append("&text="+encoded_message);
        sbPostData.append("&senderid="+senderId);
        sbPostData.append("&route_id=1");
        sbPostData.append("&Unicode=1");
        //final string
        mainUrl = sbPostData.toString();
        try
        {
            //prepare connection
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
            //reading response
            String response;
            while ((response = reader.readLine()) != null) {
                //print response
                Log.d("RESPONSE", "" + response);

                Gson gson = new Gson();
                OTPResponse otpResponse = gson.fromJson(response, OTPResponse.class);
                Log.d("otpResponse", "" + otpResponse.getMsgStatus());
                if (otpResponse.getLoginStatus().equalsIgnoreCase("Success")) {
                    progressDialog.dismiss();
                    if (otpResponse.getMsgStatus().equalsIgnoreCase("failed")) {

                        otpView.setVisibility(View.GONE);

                        cardViews.get(0).setVisibility(View.GONE);

                    } else if (otpResponse.getMsgStatus().equalsIgnoreCase("Sent")) {

                        otpView.setVisibility(View.VISIBLE);

                        cardViews.get(0).setVisibility(View.GONE);


                        textView.setText("Verify");

                    } else {

                        otpView.setVisibility(View.GONE);

                        cardViews.get(0).setVisibility(View.GONE);

                    }
                } else {
                    progressDialog.dismiss();

                    otpView.setVisibility(View.GONE);

                    cardViews.get(0).setVisibility(View.GONE);


                }

            }

            //finally close connection
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }



    }


    private void requestPermission() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(Login.this, "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();

    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermission();
    }
}
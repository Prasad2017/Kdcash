package in.kdcash.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import in.kdcash.Activity.MainPage;
import in.kdcash.Extra.DetectConnection;
import in.kdcash.Model.PostResponse;
import in.kdcash.R;
import in.kdcash.Retrofit.Api;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class BankTransfer extends Fragment {

    View view;
    @BindViews({R.id.customerName, R.id.customerNumber, R.id.customerAddress, R.id.receiverName, R.id.receiverNumber,
                R.id.receiverAddress, R.id.amount, R.id.accountNumber, R.id.bankName, R.id.branchName, R.id.ifsc})
    List<FormEditText> formEditTexts;
    @BindViews({R.id.imageFirst, R.id.imageSecond, R.id.imageName})
    List<TextView> textViews;
    Intent galleryIntent;
    public String documentType, firstPath, secondPath, chequePath;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bank_transfer, container, false);
        ButterKnife.bind(this, view);

        return view;

    }

    @OnClick({R.id.imageFirstChoose, R.id.imageSecondChoose, R.id.imageSelect, R.id.submit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.imageFirstChoose:

                documentType = "firstImage";

                galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 0);

                break;

            case R.id.imageSecondChoose:

                documentType = "SecondImage";

                galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 0);

                break;

            case R.id.imageSelect:

                documentType = "chequeImage";

                galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 0);

                break;

            case R.id.submit:

                if (formEditTexts.get(0).testValidity() && formEditTexts.get(1).testValidity() && formEditTexts.get(2).testValidity()
                   && formEditTexts.get(3).testValidity() && formEditTexts.get(4).testValidity() && formEditTexts.get(5).testValidity()
                   && formEditTexts.get(6).testValidity() && formEditTexts.get(7).testValidity() && formEditTexts.get(8).testValidity()
                   && formEditTexts.get(9).testValidity() && formEditTexts.get(10).testValidity()) {

                  bankTransfer(formEditTexts.get(0).getText().toString(), formEditTexts.get(1).getText().toString(), formEditTexts.get(2).getText().toString(), formEditTexts.get(3).getText().toString(),
                          formEditTexts.get(4).getText().toString(), formEditTexts.get(5).getText().toString(), formEditTexts.get(6).getText().toString(),
                          formEditTexts.get(7).getText().toString(), formEditTexts.get(8).getText().toString(), formEditTexts.get(9).getText().toString(), formEditTexts.get(10).getText().toString());

                }

                break;

        }

    }

    private void bankTransfer(String fullName, String mobileNumber, String address, String receiverName, String receiverNumber, String receiverAddress, String amount, String accountNumber, String bankName, String branchName, String ifscCode) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Transfer money uploading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        //Headers Authorization Token
        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("Authorization", "bearer " + MainPage.accessToken.replace("\"", ""));
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(firstPath);
        File file1 = new File(secondPath);
        File file2 = new File(chequePath);


        // Parsing any Image type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("IdFrontSide", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        // Parsing any second Image type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("image/*"), file1);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("IdBackSide", file1.getName(), requestBody1);
        RequestBody filename1 = RequestBody.create(MediaType.parse("text/plain"), file1.getName());
        // Parsing any third Image type file
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("image/*"), file2);
        MultipartBody.Part fileToUpload2 = MultipartBody.Part.createFormData("CheckFrontPhoto", file2.getName(), requestBody2);
        RequestBody filename2 = RequestBody.create(MediaType.parse("text/plain"), file2.getName());


        RequestBody filefullName = RequestBody.create(MediaType.parse("multipart/form-data"), fullName);
        RequestBody filemobileNumber = RequestBody.create(MediaType.parse("multipart/form-data"), mobileNumber);
        RequestBody fileaddress = RequestBody.create(MediaType.parse("multipart/form-data"), address);
        RequestBody filereceiverName = RequestBody.create(MediaType.parse("multipart/form-data"), receiverName);
        RequestBody filereceiverNumber = RequestBody.create(MediaType.parse("multipart/form-data"), receiverNumber);
        RequestBody filereceiverAddress = RequestBody.create(MediaType.parse("multipart/form-data"), receiverAddress);
        RequestBody fileamount = RequestBody.create(MediaType.parse("multipart/form-data"), amount);
        RequestBody fileaccountNumber = RequestBody.create(MediaType.parse("multipart/form-data"), accountNumber);
        RequestBody filebankName = RequestBody.create(MediaType.parse("multipart/form-data"), bankName);
        RequestBody filebranchName = RequestBody.create(MediaType.parse("multipart/form-data"), branchName);
        RequestBody fileifscCode = RequestBody.create(MediaType.parse("multipart/form-data"), ifscCode);
        RequestBody fileuserId = RequestBody.create(MediaType.parse("multipart/form-data"), MainPage.userId);
        RequestBody fileCountry = RequestBody.create(MediaType.parse("multipart/form-data"), "Nepal");
        RequestBody filePaymentMode = RequestBody.create(MediaType.parse("multipart/form-data"), "cash");
        RequestBody filePaymentStatus = RequestBody.create(MediaType.parse("multipart/form-data"), "payment_received");


        Call<PostResponse> call = Api.getClient().transferMoney(stringStringMap, fileToUpload, filename, fileToUpload1, filename1, fileToUpload2, filename2, fileuserId, filefullName, filemobileNumber, fileaddress, filereceiverName, filereceiverNumber, fileamount, fileaccountNumber, filebankName, filebranchName, fileifscCode, fileCountry, filePaymentMode, filePaymentStatus);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

                if (response.isSuccessful()){

                    if (response.body().getSuccess().booleanValue() == true){
                        progressDialog.dismiss();
                        Toasty.normal(getActivity(), ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toasty.normal(getActivity(), ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.dismiss();
                    Toasty.normal(getActivity(), ""+response.body(), Toasty.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("transferError", ""+t.getLocalizedMessage());
            }
        });

    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            // When an Image is picked
            if (documentType.equalsIgnoreCase("firstImage")) {

                if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
                    // Get the Image from data
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    firstPath = cursor.getString(columnIndex);
                    textViews.get(0).setText(firstPath);

                    cursor.close();

                } else {
                    Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();
                }

            } else if (documentType.equalsIgnoreCase("secondImage")) {

                if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
                    // Get the Image from data
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    secondPath = cursor.getString(columnIndex);
                    textViews.get(1).setText(secondPath);

                    cursor.close();

                } else {
                    Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();
                }

            } else if (documentType.equalsIgnoreCase("chequeImage")) {

                if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
                    // Get the Image from data
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    chequePath = cursor.getString(columnIndex);
                    textViews.get(2).setText(chequePath);

                    cursor.close();

                } else {
                    Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }


    public void onStart() {
        super.onStart();
        Log.e("onStart", "called");
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            requestPermission();
        } else {
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

    private void requestPermission() {
        Dexter.withActivity(getActivity())
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
                        Toast.makeText(getActivity(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


}
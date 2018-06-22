package com.whaddyalove.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.soundcloud.android.crop.Crop;
import com.whaddyalove.R;
import com.whaddyalove.common.UrlConstants;
import com.whaddyalove.common.WhaddyaLoveConstants;
import com.whaddyalove.utils.DialogUtil;
import com.whaddyalove.utils.ImageLoadingUtils;
import com.whaddyalove.utils.SessionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity {
    private EditText firstName, lastName, phoneNo, email, gender;
    private ImageView userImage, btnSave;
    private TextView changeImage, resetPassword;
    private AQuery aq;
    private static final int CAMERA_PIC_REQUEST = 1;
    private String fileName = "whaddyaLove.jpg";
    private String mediaFileName;
    private String encodedImageString;
    private ImageLoadingUtils imageUtil;
    //Bitmap to get image from gallery
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        aq = new AQuery(this);
        imageUtil = new ImageLoadingUtils(this);
        inflateXmlData();
        getUserData();
    }

    private void inflateXmlData() {
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        phoneNo = (EditText) findViewById(R.id.et_phone);
        email = (EditText) findViewById(R.id.et_email);
        gender = (EditText) findViewById(R.id.et_gender);
        changeImage = (TextView) findViewById(R.id.change_img);
        resetPassword = (TextView) findViewById(R.id.reset_pass);
        userImage = (ImageView) findViewById(R.id.profile_img);
        btnSave = (ImageView) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                //Requesting storage permission
//                requestStoragePermission();
            }
        });

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                //Requesting storage permission
//                requestStoragePermission();
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPass();
            }
        });

    }

    private void resetPass() {
        final Dialog dialog = new Dialog(ProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_forgot_pass);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.8f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText et_email = (EditText) dialog.findViewById(R.id.et_email);
        final EditText oldPassword = (EditText) dialog.findViewById(R.id.et_oldPass);
        final EditText newPassword = (EditText) dialog.findViewById(R.id.et_newPass);
        Button saveBtn = (Button) dialog.findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String email = et_email.getText().toString();
                if ("".equals(email)) {
                    et_email.setError("Please enter your email id");
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    et_email.setError("Please enter a valid email id");
                } else if ("".equals(oldPassword.getText().toString().trim())) {
                    oldPassword.setError("Please enter old password");
                } else if ("".equals(newPassword.getText().toString().trim())) {
                    newPassword.setError("Please enter new password");
                } else if (DialogUtil.checkInternetConnection(ProfileActivity.this)) {
                    String url = UrlConstants.BASE_URL + UrlConstants.RESET_PASSWORD;
                    ProgressDialog progress = new ProgressDialog(ProfileActivity.this);
                    progress.setMessage("Loading...");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(true);
                    progress.setCancelable(false);
                    Map params = new HashMap();
                    params.put("email", et_email.getText().toString());
                    params.put("oldpassword", oldPassword.getText().toString());
                    params.put("password", newPassword.getText().toString());
                    aq.progress(progress).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus status) {
                            try {
                                if (json != null ) {
                                    if (json.getInt("responseCode")== 1){
                                        Toast.makeText(ProfileActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                    } else {
                                        Toast.makeText(ProfileActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    Toast.makeText(ProfileActivity.this,"Couldn't connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    DialogUtil.showNoConnectionDialog(ProfileActivity.this);
                }
            }
        });
        Button cancelBtn = (Button) dialog.findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getUserData() {
        if (DialogUtil.checkInternetConnection(ProfileActivity.this)) {
            String url = UrlConstants.BASE_URL + UrlConstants.GET_USER_INFO;
            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            Map params = new HashMap();
            params.put("user_id", SessionUtil.getUserId(this));
            aq.progress(progress).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        if (json != null) {
                            if (json.getInt("responseCode") == 1) {
                                JSONObject data = json.getJSONObject("data");
                                setProfileInfo(data);
                            } else {
                                Toast.makeText(ProfileActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(ProfileActivity.this, "Couldn't connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }
    }

    private void updateUserInfo() {
        if (DialogUtil.checkInternetConnection(ProfileActivity.this)) {
            String url = UrlConstants.BASE_URL + UrlConstants.UPDATE_PROFILE;
            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            Map params = new HashMap();
            params.put("firstname", firstName.getText().toString());
            params.put("lastname", lastName.getText().toString());
            params.put("phone", phoneNo.getText().toString());
            params.put("user_bio", gender.getText().toString());
            params.put("user_id", SessionUtil.getUserId(this));
            aq.progress(progress).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        if (json != null) {
                            JSONObject data = null;
                            if (json.getInt("responseCode") == 1) {
                                data = json.getJSONObject("data");
                                setProfileInfo(data);
                            /*} else {*/
                                SessionUtil.saveUserName(data.getString("firstname"), ProfileActivity.this);
                                Intent i = new Intent(ProfileActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(ProfileActivity.this, json.getInt("message"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(ProfileActivity.this, "Error : " + json.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }
    }

    private void updateUserProfilePic(String imagePath) {
        if (DialogUtil.checkInternetConnection(ProfileActivity.this)) {
            String url = UrlConstants.BASE_URL + UrlConstants.UPLOAD_USER_PIC;
            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            Map params = new HashMap();
            params.put("profile_pic", imagePath);
            params.put("user_id", SessionUtil.getUserId(this));
            aq.progress(progress).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        if (json != null) {
                            if (json.getInt("responseCode") == 1) {
                                getUserData();
                            } else {
                                Toast.makeText(ProfileActivity.this, json.getInt("message"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(ProfileActivity.this, "Couldn't connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }
    }

    private void setProfileInfo(JSONObject userInfo) {
        try {
            email.setText(userInfo.getString("email"));
            lastName.setText(userInfo.getString("lastname"));
            firstName.setText(userInfo.getString("firstname"));
            phoneNo.setText(userInfo.getString("phone"));
            gender.setText(userInfo.getString("user_bio"));
            if (!userInfo.isNull("profile_pic")) {
                aq.id(userImage).image(userInfo.getString("profile_pic"), true, true, 0, 0, null, AQuery.FADE_IN);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    if (checkCameraHardware(ProfileActivity.this)) {
                        int permissionStatus = ContextCompat.checkSelfPermission(ProfileActivity.this,
                                Manifest.permission.CAMERA);
                        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ProfileActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    WhaddyaLoveConstants.CAMERA_PERMISSION_REQUEST);
                        } else {
                            permissionStatus = ContextCompat.checkSelfPermission(ProfileActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(ProfileActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        WhaddyaLoveConstants.WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST);
                            } else {
                                openCamera();
                            }
                        }
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment.getExternalStorageDirectory(), fileName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WhaddyaLoveConstants.CAMERA_PERMISSION_REQUEST:
            case WhaddyaLoveConstants.WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (requestCode == WhaddyaLoveConstants.CAMERA_PERMISSION_REQUEST) {
                        int permissionStatus = ContextCompat.checkSelfPermission(ProfileActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ProfileActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    WhaddyaLoveConstants.WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST);
                        } else {
                            openCamera();
                        }
                    } else {
                        openCamera();
                    }
                } else {
                    if (requestCode == WhaddyaLoveConstants.CAMERA_PERMISSION_REQUEST) {
                        Toast.makeText(ProfileActivity.this, "Permission to access Camera is required to take a photo using Whaddya Love App.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Permission to access photos, media and files is required to take a photo using Whaddya Love App.", Toast.LENGTH_SHORT).show();
                    }
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                userImage.setImageBitmap(bitmap);
                uploadMultipart();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_PIC_REQUEST) {
                File f = new File(android.os.Environment.getExternalStorageDirectory(), fileName);
                Uri selectedImage = Uri.fromFile(f);
                Uri destination = Uri.fromFile(new File(getCacheDir(), "vd"));
                Crop.of(selectedImage, destination).asSquare().start(this);
            } else if (requestCode == 2) {
                Uri destination = Uri.fromFile(new File(getCacheDir(), "vd"));
                Crop.of(data.getData(), destination).asSquare().start(this);
            } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
                Uri selectedImage = Crop.getOutput(data);
                if (selectedImage.getAuthority() != null) {
                    String fileNameSegments[] = selectedImage.toString().split("/");
                    mediaFileName = fileNameSegments[fileNameSegments.length - 1];
                    if (mediaFileName.indexOf(".") == -1) {
                        mediaFileName = mediaFileName + sdf.format(date).toString() + ".jpg";
                    }
                    ParcelFileDescriptor parcelFileDescriptor = null;
                    try {
                        parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImage, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        bitmap = imageUtil.decodeBitmapFromFileDescriptor(fileDescriptor);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (parcelFileDescriptor != null)
                                parcelFileDescriptor.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    userImage.setImageBitmap(bitmap);
                    encodeImageToString();
                }
            }

        } else {
            Toast.makeText(ProfileActivity.this, "No Image Selected !!", Toast.LENGTH_SHORT).show();
        }

    }

    public void encodeImageToString() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                encodedImageString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                updateUserProfilePic(encodedImageString);
            }
        }.execute(null, null, null);
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
}

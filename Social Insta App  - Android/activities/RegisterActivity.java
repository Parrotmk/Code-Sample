package com.whaddyalove.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.whaddyalove.R;
import com.whaddyalove.common.UrlConstants;
import com.whaddyalove.utils.DialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    private EditText password, firstName, lastName, edtPhone, edtEmail;
    private Button signUp;
    private AQuery aQuery;
    private String phone = null, email = null, flag = "phone";
    private TextView titleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        aQuery = new AQuery(this);
        inflateXmlData();
    }

    private void inflateXmlData() {
        password = (EditText) findViewById(R.id.password);
        firstName = (EditText) findViewById(R.id.edtFirstName);
        lastName = (EditText) findViewById(R.id.edtLastName);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        signUp = (Button) findViewById(R.id.btnLogin);


        String styledText = "Already have an account? <font color=\"#02bcd4\">Sign In</font>";
        ((TextView) findViewById(R.id.txtSignUp)).setText(Html.fromHtml(styledText));


        findViewById(R.id.txtSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidation()) {
                    registration();
                }
            }
        });
    }

    private void registration() {
        if (DialogUtil.checkInternetConnection(this)) {
            String url = UrlConstants.BASE_URL + UrlConstants.SIGN_UP;
            ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
            progress.setMessage("Loading !! Please wait...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            Map data = new HashMap();
            data.put("phone", edtPhone.getText().toString());
            data.put("email", edtEmail.getText().toString());
            data.put("firstname", firstName.getText().toString());
            data.put("lastname", lastName.getText().toString());
            data.put("password", password.getText().toString());


            aQuery.progress(progress).ajax(url, data, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        if (json != null) {
                            if (json.getInt("responseCode") == 1) {
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                userAlreadyExist(json.getString("message"));
                            }
                        } else {
                            Toast.makeText(aQuery.getContext(), "Couldn't connect to server. Please try again later.", Toast.LENGTH_LONG).show();
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

    private void checkUserExist() {
        if (DialogUtil.checkInternetConnection(this)) {
            String url = UrlConstants.BASE_URL + UrlConstants.EMAIL_ALREADY_EXISTS;
            ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
            progress.setMessage("Loading !! Please wait...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            Map data = new HashMap();
            data.put("email", edtEmail.getText().toString());
            aQuery.progress(progress).ajax(url, data, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    if (json != null) {
                        if (isValidation())
                            registration();
                    } else {
                        Toast.makeText(aQuery.getContext(), "Couldn't connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                    }
                    if (isValidation())
                        registration();
                }
            });
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }
    }

    private boolean isValidation() {
        boolean isValid = true;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        String email = edtEmail.getText().toString();
        if ("".equals(email)) {
            edtEmail.setError("Please enter your email id");
            isValid = false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Please enter a valid email id");
            isValid = false;
        }

        if ("".equals(firstName.getText().toString())) {
            firstName.setError("Please enter a first name");
            isValid = false;
        }
        if ("".equals(lastName.getText().toString())) {
            lastName.setError("Please enter a last name");
            isValid = false;
        }
        if ("".equals(password.getText().toString())) {
            password.setError("Please enter a password");
            isValid = false;
        }

        return isValid;
    }

    private void userAlreadyExist(String message) {
        final Dialog dialog = new Dialog(RegisterActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_already_exist);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.8f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button cancelBtn = (Button) dialog.findViewById(R.id.btn_save);
        ((TextView) dialog.findViewById(R.id.message)).setText(message);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

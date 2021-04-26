package com.example.bookaddict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookaddict.utils.ErrorUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText edit_text_email_forgot_password, edit_text_new_password_forgot_password;
    private Button button_forgot_password;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edit_text_email_forgot_password = findViewById(R.id.edit_text_email_forgot_password);
        edit_text_new_password_forgot_password = findViewById(R.id.edit_text_new_password_forgot_password);
        button_forgot_password = findViewById(R.id.button_forgot_password);

        button_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        edit_text_new_password_forgot_password.addTextChangedListener(passwordTextWatcher);
    }

    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pass = edit_text_new_password_forgot_password.getText().toString().trim();
            if (!PASSWORD_PATTERN.matcher(pass).matches()){
                edit_text_new_password_forgot_password.setError("Please use one uppercase, lowercase, digit, special character and must be 6 character long");
                button_forgot_password.setEnabled(false);
            }
            else {
                edit_text_new_password_forgot_password.setError(null);
                button_forgot_password.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void resetPassword(){
        final String email, password;
        boolean error = false;
        String url = "http://192.168.1.65:81/bookaddict/reset_password.php";
        email = edit_text_email_forgot_password.getText().toString().trim();
        password = edit_text_new_password_forgot_password.getText().toString().trim();

        if (email.isEmpty()){
            edit_text_email_forgot_password.setError("Please fill this field");
            error = true;
        }
        if (password.isEmpty()){
            edit_text_new_password_forgot_password.setError("Please fill this field");
            error = true;
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            edit_text_new_password_forgot_password.setError("Please use one uppercase, lowercase, digit, special character and must be 6 character long");
            error = true;
        }

        if (!error) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(ForgotPasswordActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.trim().equals("success")) {
                        Toast.makeText(ForgotPasswordActivity.this, "Password Reset Successful", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);

                    } else if (response.trim().equals("dbError")) {
                        Toast.makeText(getApplicationContext(), "Error while inserting", Toast.LENGTH_SHORT).show();
                    } else if (response.trim().equals("email_not_found")) {
                        Toast.makeText(getApplicationContext(), "Email not found", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(ForgotPasswordActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("new_password", password);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }
}
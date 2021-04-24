package com.example.bookaddict;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class SignUpActivity extends AppCompatActivity {
    private EditText edit_text_full_name, edit_text_phone, edit_text_email, edit_text_password;
    private Button button_sign_up;
    private TextView text_view_go_to_login;
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

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9]{10,13}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edit_text_full_name = findViewById(R.id.edit_text_full_name);
        edit_text_phone = findViewById(R.id.edit_text_phone);
        edit_text_email = findViewById(R.id.edit_text_email);
        edit_text_password = findViewById(R.id.edit_text_password);
        button_sign_up = findViewById(R.id.button_sign_up);
        text_view_go_to_login = findViewById(R.id.text_view_go_to_login);

        text_view_go_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

        edit_text_full_name.addTextChangedListener(nameTextWatcher);
        edit_text_phone.addTextChangedListener(phoneTextWatcher);
        edit_text_email.addTextChangedListener(emailTextWatcher);
        edit_text_password.addTextChangedListener(passwordTextWatcher);

        button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String full_name = edit_text_full_name.getText().toString().trim();
            if (!full_name.matches("[a-zA-Z\\s]+")){
                edit_text_full_name.setError("Please enter your valid name without digits and special characters");
                button_sign_up.setEnabled(false);
            }
            else {
                edit_text_full_name.setError(null);
                button_sign_up.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String email = edit_text_email.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edit_text_email.setError("Please enter a valid email address");
                button_sign_up.setEnabled(false);
            }
            else {
                edit_text_email.setError(null);
                button_sign_up.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pass = edit_text_password.getText().toString().trim();
            if (!PASSWORD_PATTERN.matcher(pass).matches()){
                edit_text_password.setError("Please use one uppercase, lowercase, digit, special character and must be 6 character long");
                button_sign_up.setEnabled(false);
            }
            else {
                edit_text_password.setError(null);
                button_sign_up.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String phone = edit_text_phone.getText().toString().trim();
            if (!PHONE_PATTERN.matcher(phone).matches()){
                edit_text_phone.setError("Please enter valid 10 digit number");
                button_sign_up.setEnabled(false);
            }
            else {
                edit_text_phone.setError(null);
                button_sign_up.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



    private void openLoginActivity() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void signUp(){
        final String full_name, email, password, phone;
        boolean error = false;
        String url = "http://192.168.1.65:81/bookaddict/register.php";
        full_name = edit_text_full_name.getText().toString().trim();
        email = edit_text_email.getText().toString().trim();
        password = edit_text_password.getText().toString().trim();
        phone = edit_text_phone.getText().toString().trim();

        if (full_name.isEmpty()){
            edit_text_full_name.setError("Please fill this field");
            error = true;
        }
        if (!full_name.matches("[a-zA-Z\\s]+")){
            edit_text_full_name.setError("Please enter your valid name");
            error = true;
        }
        if (email.isEmpty()){
            edit_text_email.setError("Please fill this field");
            error = true;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edit_text_email.setError("Please enter a valid email address");
            error = true;
        }
        if (password.isEmpty()){
            edit_text_password.setError("Please fill this field");
            error = true;
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            edit_text_password.setError("Password too weak. Must be 6 character long");
            error = true;
        }
        if (phone.isEmpty()){
            edit_text_phone.setError("Please fill this field");
            error = true;
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            edit_text_phone.setError("Please enter valid phone number");
            error = true;
        }

        if (!error) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.trim().equals("success")) {
                        Toast.makeText(SignUpActivity.this, "Successful! Please login to continue", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    } else if (response.trim().equals("dbError")) {
                        Toast.makeText(getApplicationContext(), "Error while inserting", Toast.LENGTH_SHORT).show();
                    } else if (response.trim().equals("dbError2")) {
                        Toast.makeText(getApplicationContext(), "Error while fetching", Toast.LENGTH_SHORT).show();
                    } else if (response.trim().equals("email_taken")) {
                        Toast.makeText(getApplicationContext(), "Sorry! This email is already registered", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(SignUpActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("full_name", full_name);
                    params.put("email", email);
                    params.put("password", password);
                    params.put("phone", phone);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setMessage("Exit the app?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
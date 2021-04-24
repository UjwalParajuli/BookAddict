package com.example.bookaddict;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.bookaddict.models.User;
import com.example.bookaddict.utils.ErrorUtils;
import com.example.bookaddict.utils.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText edit_text_email_login, edit_text_password_login;
    private TextView text_view_forgot_password, text_view_go_to_sign_up;
    private Button button_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_text_email_login = findViewById(R.id.edit_text_email_login);
        edit_text_password_login = findViewById(R.id.edit_text_password_login);
        text_view_forgot_password = findViewById(R.id.text_view_forgot_password);
        text_view_go_to_sign_up = findViewById(R.id.text_view_go_to_sign_up);
        button_login = findViewById(R.id.button_login);

        text_view_go_to_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });

        text_view_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgotPasswordActivity();
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void openSignUpActivity() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void openForgotPasswordActivity(){

    }

    private void login(){
        final String email, password;
        boolean error = false;
        String url = "http://192.168.1.65:81/bookaddict/login.php";
        email = edit_text_email_login.getText().toString().trim();
        password = edit_text_password_login.getText().toString().trim();

        if (email.isEmpty()) {
            edit_text_email_login.setError("Please fill this field");
            error = true;
        }

        if (password.isEmpty()) {
            edit_text_password_login.setError("Please fill this field");
            error = true;
        }

        if (!error){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.trim().equals("first_db_error")) {
                        Toast.makeText(getApplicationContext(), "Invalid User", Toast.LENGTH_SHORT).show();

                    } else if (response.trim().equals("second_db_error")) {
                        Toast.makeText(getApplicationContext(), "Error while fetching", Toast.LENGTH_SHORT).show();
                    } else if (response.trim().equals("credential_error")) {
                        Toast.makeText(getApplicationContext(), "Email/password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            int id = jsonResponse.getInt("user_id");
                            String name = jsonResponse.getString("full_name");
                            String fetched_email = jsonResponse.getString("email");
                            String phone = jsonResponse.getString("phone");

                            User user = new User(id, name, fetched_email, phone, password);
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                            Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();

                            finish();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(LoginActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
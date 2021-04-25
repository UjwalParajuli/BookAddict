package com.example.bookaddict;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutUsActivity extends AppCompatActivity {
    private TextView text_view_gmail, text_view_facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        this.setTitle("About Us");

        text_view_gmail = findViewById(R.id.text_view_gmail);
        text_view_facebook = findViewById(R.id.text_view_facebook);

        text_view_gmail.setMovementMethod(LinkMovementMethod.getInstance());
        text_view_facebook.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
package com.example.bookaddict.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bookaddict.AboutUsActivity;
import com.example.bookaddict.ChangePasswordActivity;
import com.example.bookaddict.EditProfileActivity;
import com.example.bookaddict.LoginActivity;
import com.example.bookaddict.MyDonationsActivity;
import com.example.bookaddict.R;
import com.example.bookaddict.models.User;
import com.example.bookaddict.utils.SharedPrefManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    private Button button_logout;
    private TextView text_view_my_donations, text_view_edit_profile, text_view_change_password, text_view_about_us, text_view_full_name_profile;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = SharedPrefManager.getInstance(getContext()).getUser();

        text_view_my_donations = view.findViewById(R.id.text_view_my_donations);
        text_view_edit_profile = view.findViewById(R.id.text_view_edit_profile);
        text_view_change_password = view.findViewById(R.id.text_view_change_password);
        text_view_about_us = view.findViewById(R.id.text_view_about_us);
        text_view_full_name_profile = view.findViewById(R.id.text_view_full_name_profile);
        button_logout = view.findViewById(R.id.button_logout);

        text_view_full_name_profile.setText(user.getFull_name());

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        text_view_my_donations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMyDonations();
            }
        });

        text_view_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfile();
            }
        });

        text_view_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangePassword();
            }
        });

        text_view_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAboutUs();
            }
        });

        return view;
    }

    private void openMyDonations(){
        Intent intent = new Intent(getContext(), MyDonationsActivity.class);
        startActivity(intent);
    }

    private void openChangePassword(){
        Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
        startActivity(intent);
    }

    private void openEditProfile(){
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void openAboutUs(){
        Intent intent = new Intent(getContext(), AboutUsActivity.class);
        startActivity(intent);
    }

    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Logout from the app?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
                SharedPrefManager.getInstance(getContext()).logout();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);

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

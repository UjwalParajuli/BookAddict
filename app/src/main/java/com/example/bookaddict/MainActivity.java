package com.example.bookaddict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.bookaddict.fragments.BooksFragment;
import com.example.bookaddict.fragments.DonateFragment;
import com.example.bookaddict.fragments.DownloadsFragment;
import com.example.bookaddict.fragments.ProfileFragment;
import com.example.bookaddict.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottom_navigation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottom_navigation_view = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        bottom_navigation_view.setOnNavigationItemSelectedListener(nav_listener);



        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BooksFragment()).addToBackStack(null).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener nav_listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
                    Fragment selected_fragment = null;

                    getSupportFragmentManager().addOnBackStackChangedListener(
                            new FragmentManager.OnBackStackChangedListener() {
                                public void onBackStackChanged() {
                                    Fragment current = getCurrentFragment();
                                    if (current instanceof BooksFragment) {
                                        bottom_navigation_view.getMenu().findItem(R.id.nav_home).setChecked(true);
                                    }
                                    else if(current instanceof SearchFragment) {
                                        bottom_navigation_view.getMenu().findItem(R.id.nav_search).setChecked(true);
                                    }
                                    else if (current instanceof DownloadsFragment){
                                        bottom_navigation_view.getMenu().findItem(R.id.nav_downloads).setChecked(true);
                                    }
                                    else if (current instanceof DonateFragment){
                                        bottom_navigation_view.getMenu().findItem(R.id.nav_donate).setChecked(true);
                                    }
                                    else if (current instanceof ProfileFragment){
                                        bottom_navigation_view.getMenu().findItem(R.id.nav_profile).setChecked(true);
                                    }
                                }
                            });

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            selected_fragment = new BooksFragment();
                            break;
                        case R.id.nav_search:
                            selected_fragment = new SearchFragment();
                            break;
                        case R.id.nav_downloads:
                            selected_fragment = new DownloadsFragment();
                            break;
                        case R.id.nav_donate:
                            selected_fragment = new DonateFragment();
                            break;
                        case R.id.nav_profile:
                            selected_fragment = new ProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).addToBackStack(null).commit();
                    return true;

                }
            };

    public Fragment getCurrentFragment() {
        return this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment current = getCurrentFragment();
        if (manager.getBackStackEntryCount() >= 1 && !(current instanceof BooksFragment)) {
            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.

            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // clear backstack first
            FragmentTransaction transaction = manager.beginTransaction();
            if (current instanceof BooksFragment) {
                bottom_navigation_view.getMenu().findItem(R.id.nav_home).setChecked(true);
            }
            else if(current instanceof SearchFragment) {
                bottom_navigation_view.getMenu().findItem(R.id.nav_home).setChecked(true);
            }
            else if (current instanceof DownloadsFragment){
                bottom_navigation_view.getMenu().findItem(R.id.nav_home).setChecked(true);
            }
            else if (current instanceof DonateFragment){
                bottom_navigation_view.getMenu().findItem(R.id.nav_home).setChecked(true);
            }
            else if (current instanceof ProfileFragment){
                bottom_navigation_view.getMenu().findItem(R.id.nav_home).setChecked(true);
            }
            transaction.replace(R.id.fragment_container, new BooksFragment());
            transaction.commit();




        } else {
            //super.onBackPressed();
            // Otherwise, ask user if he wants to leave :)
            new AlertDialog.Builder(this)
                    .setMessage("Exit the app?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    }).create().show();
        }

    }
}
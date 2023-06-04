package com.example.studentnotifyapp.Admin;

import static com.example.studentnotifyapp.R.id.admin_home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.studentnotifyapp.Login;
import com.example.studentnotifyapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class AdminPage extends AppCompatActivity {


    BottomNavigationView bnView;
    private String ROOT_FRAGMENT_TAG;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);




        bnView = findViewById(R.id.bnView);

        bnView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id== R.id.admin_home)
                {
                    loadFrag(new HomeAdminFragment(),true);

                }
                else if(id==R.id.admin_Action)
                {
                    loadFrag(new Actions(),false);
                }
                else{
                    AlertDialog.Builder logoutDialog = new AlertDialog.Builder(AdminPage.this);
                    logoutDialog.setTitle("Logout");
                    logoutDialog.setMessage("Do you really want to logout?");
                    logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putString("isLogin","no");
                            editor.commit();
                            Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                            startActivity(loginIntent);
                        }

                    });

                    logoutDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);

                            if (currentFragment != null) {
                                if (currentFragment instanceof HomeAdminFragment) {
                                    bnView.getMenu().getItem(0).setChecked(true);

                                }
                                if (currentFragment instanceof Actions) {
                                    bnView.getMenu().getItem(1).setChecked(true);
                                }

                            }

                        }
                    });
                    logoutDialog.show();
                }

                return true;
            }
        });

        // to highlight bottom navigation item when back button pressed and back stack load

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                // get the currently displayed fragment

                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);

                if (currentFragment != null) {
                    if (currentFragment instanceof HomeAdminFragment) {
                       bnView.getMenu().getItem(0).setChecked(true);

                    }
                    if (currentFragment instanceof Actions) {
                        bnView.getMenu().getItem(1).setChecked(true);
                    }

                }
            }
        });

        // set initial fragment
        bnView.setSelectedItemId(admin_home);

    }

    // method to load fragment
    public void loadFrag(Fragment fragment, boolean flag)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(flag){
            ft.add(R.id.container,fragment);
            fm.popBackStack(ROOT_FRAGMENT_TAG,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.addToBackStack(ROOT_FRAGMENT_TAG);
        }
        else{
            ft.replace(R.id.container,fragment);
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if(count==1)
        {
            AlertDialog.Builder exitDialog = new AlertDialog.Builder(AdminPage.this);
            exitDialog.setTitle("Exit");
            exitDialog.setMessage("Do you really want to Exit?");
            exitDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
//                    Toast.makeText(AdminPage.this, "yes", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                }
            });
            exitDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            exitDialog.show();
        }
        else
        {
            super.onBackPressed();
        }
    }
}
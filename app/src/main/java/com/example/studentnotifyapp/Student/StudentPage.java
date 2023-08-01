package com.example.studentnotifyapp.Student;


import static com.example.studentnotifyapp.R.id.admin_home;
import static com.example.studentnotifyapp.R.id.student_home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.studentnotifyapp.Admin.ActionsFragment;
import com.example.studentnotifyapp.Admin.AdminPage;
import com.example.studentnotifyapp.Admin.HomeAdminFragment;
import com.example.studentnotifyapp.Login;
import com.example.studentnotifyapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class StudentPage extends AppCompatActivity {

    BottomNavigationView bnView;

    private String ROOT_FRAGMENT_TAG;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);



        bnView = findViewById(R.id.bnViewStu);
        bnView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id==R.id.student_home)
                {
                    loadFrag(new HomeStudentFragment(),true);
                }
                else if(id==R.id.Student_viewPdf)
                {

                    loadFrag(new ViewPdfFragment(),false);
                }
                else if(id==R.id.Student_Discuss)
                {

                    loadFrag(new DiscussFragment(),false);
                }
                else if(id==R.id.Student_Profile)
                {
                    loadFrag(new ProfileFragment(),false);
                }
                else
                {
                    AlertDialog.Builder logoutDialog = new AlertDialog.Builder(StudentPage.this);
                    logoutDialog.setCancelable(false);
                    logoutDialog.setTitle("Logout");
                    logoutDialog.setMessage("Do you really want to logout?");


                    logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            FirebaseMessaging.getInstance().unsubscribeFromTopic("studentnotifyapp");

                            sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putString("isLogin","no");
                            editor.putString("username","");
                            editor.commit();
                            finishAffinity();
                            Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                            startActivity(loginIntent);
                        }
                    });

                    logoutDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            Fragment currentFragment = fragmentManager.findFragmentById(R.id.containerStu);

                            if (currentFragment != null) {
                                if (currentFragment instanceof HomeStudentFragment) {
                                    bnView.getMenu().getItem(0).setChecked(true);

                                }
                                if (currentFragment instanceof ViewPdfFragment) {
                                    bnView.getMenu().getItem(1).setChecked(true);
                                }
                                if (currentFragment instanceof DiscussFragment) {
                                    bnView.getMenu().getItem(2).setChecked(true);
                                }
                                if(currentFragment instanceof ProfileFragment){
                                    bnView.getMenu().getItem(3).setChecked(true);
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
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.containerStu);

                if (currentFragment != null) {
                    if (currentFragment instanceof HomeStudentFragment) {
                        bnView.getMenu().getItem(0).setChecked(true);

                    }
                    if (currentFragment instanceof ViewPdfFragment) {
                        bnView.getMenu().getItem(1).setChecked(true);
                    }
                    if (currentFragment instanceof DiscussFragment) {
                        bnView.getMenu().getItem(2).setChecked(true);
                    }
                    if(currentFragment instanceof ProfileFragment){
                        bnView.getMenu().getItem(3).setChecked(true);
                    }

                }
            }
        });

        // set initial fragment
        bnView.setSelectedItemId(student_home);
    }
    public void loadFrag(Fragment fragment, boolean flag)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(flag){
            ft.add(R.id.containerStu,fragment);
            fm.popBackStack(ROOT_FRAGMENT_TAG,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.addToBackStack(ROOT_FRAGMENT_TAG);
        }
        else{
            ft.replace(R.id.containerStu,fragment);
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if(count==1)
        {
            AlertDialog.Builder exitDialog = new AlertDialog.Builder(StudentPage.this);
            exitDialog.setTitle("Exit");
            exitDialog.setMessage("Do you really want to Exit?");
            exitDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
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
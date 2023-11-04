package com.example.studentnotifyapp.Student;


import static com.example.studentnotifyapp.R.id.admin_home;
import static com.example.studentnotifyapp.R.id.student_home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentnotifyapp.Admin.ActionsFragment;
import com.example.studentnotifyapp.Admin.AdminPage;
import com.example.studentnotifyapp.Admin.HomeAdminFragment;
import com.example.studentnotifyapp.Login;
import com.example.studentnotifyapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

public class StudentPage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private String ROOT_FRAGMENT_TAG;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);



        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer,R.string.closeDrawer);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        String username = getApplicationContext().getSharedPreferences("login",MODE_PRIVATE).getString("username","");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot userSnapshot = snapshot.child(username);
                TextView  name = findViewById(R.id.fullname);
                name.setText(userSnapshot.child("fullname").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // set initial fragment
        loadFrag(new HomeStudentFragment(),true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Menu menu = navigationView.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    menu.getItem(i).setChecked(false);
                }

                if(id== student_home)
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
                else if(id==R.id.Student_Reminder)
                {

                    loadFrag(new ReminderFragment(),false);
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
                                    navigationView.getMenu().getItem(0).setChecked(true);

                                }
                                if (currentFragment instanceof ViewPdfFragment) {
                                    navigationView.getMenu().getItem(1).setChecked(true);
                                }
                                if (currentFragment instanceof DiscussFragment) {
                                    navigationView.getMenu().getItem(2).setChecked(true);
                                }
                                if(currentFragment instanceof ReminderFragment){
                                    navigationView.getMenu().getItem(3).setChecked(true);
                                }
                                if(currentFragment instanceof ProfileFragment){
                                    navigationView.getMenu().getItem(4).setChecked(true);
                                }

                            }
                        }
                    });
                    logoutDialog.show();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // to highlight  navigation item when back button pressed and back stack load

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                Menu menu = navigationView.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    menu.getItem(i).setChecked(false);
                }

                // get the currently displayed fragment

                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.containerStu);

                if (currentFragment != null) {
                    if (currentFragment instanceof HomeStudentFragment) {
                        navigationView.getMenu().getItem(0).setChecked(true);

                    }
                    if (currentFragment instanceof ViewPdfFragment) {
                        navigationView.getMenu().getItem(1).setChecked(true);
                    }
                    if (currentFragment instanceof DiscussFragment) {
                        navigationView.getMenu().getItem(2).setChecked(true);
                    }

                    if(currentFragment instanceof ReminderFragment){
                        navigationView.getMenu().getItem(3).setChecked(true);
                    }
                    if(currentFragment instanceof ProfileFragment){
                        navigationView.getMenu().getItem(4).setChecked(true);
                    }

                }
            }
        });


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

        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {

            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 1) {
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
            } else {
                super.onBackPressed();

            }
        }
    }
}
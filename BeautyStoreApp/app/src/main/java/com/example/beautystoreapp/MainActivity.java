package com.example.beautystoreapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeMessageToolBar;
    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;
    private String userEmail;
    private String userFName;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        welcomeMessageToolBar = findViewById(R.id.welcome_message_tool_bar);

        // Get the user's first name from the intent
        userFName = getIntent().getStringExtra("userFName");

        // Set welcome message
        welcomeMessageToolBar.setText("Welcome " + userFName);


        fragmentManager = getSupportFragmentManager();

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        // Set listener for bottom navigation items
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.home_item) {
                    selectedFragment = new HomeFragment();
                } else if (item.getItemId() == R.id.profile_item) {
                    switchToProfileFragment(userEmail);
                } else if (item.getItemId() == R.id.tips_item) {
                    selectedFragment = new BeautyTipsFragment();
                }else if (item.getItemId() == R.id.comment_item) {
                    selectedFragment = new CommentsFragment();
                }else if (item.getItemId() == R.id.logout_item) {
                    auth.signOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }

                // Replace the fragment if a valid selection was made
                if (selectedFragment != null) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_container, selectedFragment);
                    transaction.commit();
                    return true;
                }

                return false; // Return false if no valid selection was made
            }

        });
        // Load default fragment
        replaceFragment(new HomeFragment());
    }

    // Method to load fragments into the fragment container
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    // Method to switch to ProfileFragment and pass the email
    public void switchToProfileFragment(String userEmail) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("userEmail", userEmail);
        args.putString("userFName", userFName);
        profileFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, profileFragment)
                .addToBackStack(null)
                .commit();


    }
}
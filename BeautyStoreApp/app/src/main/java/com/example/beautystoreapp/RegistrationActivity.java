package com.example.beautystoreapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.beautystoreapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText registerFNameEditText;
    private EditText registerLNameEditText;
    private EditText registerAddressEditText;
    private EditText registerEmailEditText;
    private EditText registerPasswordEditText;
    private Button createAccountButton;
    private FirebaseAuth auth;
    private DatabaseReference databaseRef;

    public static String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();


        registerFNameEditText = (EditText) findViewById(R.id.register_fname_edit_text);
        registerLNameEditText = (EditText) findViewById(R.id.register_lname_edit_text);
        registerAddressEditText = (EditText) findViewById(R.id.register_address_edit_text);
        registerEmailEditText = (EditText) findViewById(R.id.register_email_edit_text);
        registerPasswordEditText = (EditText) findViewById(R.id.register_password_edit_text);
        createAccountButton = (Button) findViewById(R.id.create_account_button);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fName = registerFNameEditText.getText().toString().trim();
                String lName = registerLNameEditText.getText().toString().trim();
                String address = registerAddressEditText.getText().toString().trim();
                String email = registerEmailEditText.getText().toString().trim();
                String password = registerPasswordEditText.getText().toString().trim();

                if (fName.isEmpty() || lName.isEmpty() || address.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Register user with Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegistrationActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Get user ID
                                FirebaseUser user = auth.getCurrentUser();
                                assert user != null;
                                String userId = user.getUid();

                                // Create User object
                                User newUser = new User(userId, fName, lName, address, email, password);

                                // Insert user into Firebase Realtime Database
                                databaseRef.child("Users").child(userId).setValue(newUser).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        // Show registration confirmation dialog
                                        showRegistrationConfirmationDialog();
                                    } else {
                                        Toast.makeText(RegistrationActivity.this, "Failed to save user info", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void showRegistrationConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registration Successful")
                .setMessage("Your account has been successfully registered.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Navigate to LoginActivity or any desired activity
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setCancelable(false) // Prevent dialog from being dismissed on outside touch or back press
                .show();
    }

    public void onStart(){
        super.onStart();
        Log.d(TAG,"onStart() called");
    }

    public void onPause(){
        super.onPause();
        Log.d(TAG,"onPause() called");
    }

    //to restart the app
    public void onResume(){
        super.onResume();
        Log.d(TAG,"onResume() called");
    }

    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"onDestroy() called");
    }
}
package com.example.beautystoreapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.beautystoreapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText clientEmailEditText;
    private EditText clientPasswordEditText;
    private Button loginButton;
    private Button registrationButton;
    public static String TAG;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //Instantiate and reference Object FirebaseAuth
        auth = FirebaseAuth.getInstance(); //Get the instance of FireBaseAuth

        clientEmailEditText = (EditText) findViewById(R.id.client_email_edit_text);
        clientPasswordEditText = (EditText) findViewById(R.id.client_password_edit_text);
        loginButton = (Button) findViewById(R.id.login_button);
        registrationButton = (Button) findViewById(R.id.registration_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trim to remove any leading or trailing whitespace
                String email = clientEmailEditText.getText().toString().trim();
                String password = clientPasswordEditText.getText().toString().trim();

                // Validate email and password fields
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                login(email, password);
            }

        });

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

    } //onCreate

    private void login(String email, String password){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Login successful
                FirebaseUser user = auth.getCurrentUser();
                assert user != null;
                String userId = user.getUid();

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            String userFName = user.getUser_FName();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userFName", userFName);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "Database error", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
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
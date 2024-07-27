package com.example.beautystoreapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class UpdateProfileFragment extends Fragment {

    private EditText updateUserFName;
    private EditText updateUserLName;
    private EditText updateUserAddress;
    private EditText updateUserEmail;
    private EditText updateUserPassword;
    private Button saveChangesButton;
    private DatabaseReference databaseRef;
    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Database
        databaseRef = FirebaseDatabase.getInstance().getReference("Users");

        // Retrieve userId from arguments
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update_profile, container, false);

        updateUserFName = (EditText) v.findViewById(R.id.update_fname_edit_text);
        updateUserLName = (EditText) v.findViewById(R.id.update_lname_edit_text);
        updateUserAddress = (EditText) v.findViewById(R.id.update_address_edit_text);
        updateUserEmail = (EditText) v.findViewById(R.id.update_email_edit_text);
        updateUserPassword = (EditText) v.findViewById(R.id.update_password_edit_text);
        saveChangesButton = (Button) v.findViewById(R.id.save_update_button);

        // Load user data into the fields
        fetchUserData();

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateUserProfile();
            }
        });

        return v;
    }

    private void fetchUserData() {
        if (userId != null) {
            databaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String fName = dataSnapshot.child("user_FName").getValue(String.class);
                        String lName = dataSnapshot.child("user_LName").getValue(String.class);
                        String address = dataSnapshot.child("address").getValue(String.class);
                        String email = dataSnapshot.child("userEmail").getValue(String.class);

                        updateUserFName.setText(fName);
                        updateUserLName.setText(lName);
                        updateUserAddress.setText(address);
                        updateUserEmail.setText(email);
                        //To hide or encrypt the password field
                        updateUserPassword.setText("********"); // Hide the password initially
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }
    }

    private void updateUserProfile() {
        if (userId != null) {
            String fName = updateUserFName.getText().toString().trim();
            String lName = updateUserLName.getText().toString().trim();
            String address = updateUserAddress.getText().toString().trim();
            String email = updateUserEmail.getText().toString().trim();
            String password = updateUserPassword.getText().toString().trim();

            if (fName.isEmpty() || lName.isEmpty() || address.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update user information in Firebase
            Map<String, Object> updates = new HashMap<>();
            updates.put("user_FName", fName);
            updates.put("user_LName", lName);
            updates.put("address", address);
            updates.put("userEmail", email);
            updates.put("userPassword", password);

            databaseRef.child(userId).updateChildren(updates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Update Successful")
                            .setMessage("Your profile has been updated successfully")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Return to ProfileFragment
                                    getFragmentManager().popBackStack();
                                }
                            }).show();
                } else {
                    Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
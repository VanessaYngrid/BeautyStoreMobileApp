package com.example.beautystoreapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    private TextView userFNameTextView;
    private TextView userLNameTextView;
    private TextView userAddressTextView;
    private ImageView updateProfileButton;
    private ImageView contactUsButton;
    private ImageView logOutButton;

    private FirebaseAuth auth;
    private DatabaseReference databaseRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa Firebase Auth y Database
        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        userFNameTextView = (TextView) v.findViewById(R.id.user_fname_text_view);
        userLNameTextView = (TextView) v.findViewById(R.id.user_lname_text_view);
        userAddressTextView = (TextView) v.findViewById(R.id.user_address_text_view);
        updateProfileButton = (ImageView) v.findViewById(R.id.update_profile_button);
        contactUsButton = (ImageView) v.findViewById(R.id.contact_us_button);
        logOutButton = (ImageView) v.findViewById(R.id.logout_button);

        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create an instance of UpdateProfileFragment
                UpdateProfileFragment updateProfileFragment = new UpdateProfileFragment();

                // Pass the userId to UpdateProfileFragment
                Bundle args = new Bundle();
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    args.putString("userId", userId);
                    updateProfileFragment.setArguments(args);

                    // Replace current fragment with UpdateProfileFragment
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, updateProfileFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        contactUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create an instance of UpdateProfileFragment
                ContactUsFragment contactUsFragment = new ContactUsFragment();

                // Replace current fragment with UpdateProfileFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, contactUsFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create an Intent to start LoginActivity
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                // Start LoginActivity
                startActivity(intent);

                //getActivity() -> method that you use within a Fragment to get a
                //reference to the Activity that the Fragment is currently associated with
            }
        });

        // Fetch and display user data
        fetchUserData();

        return v;
    }

    private void fetchUserData() {

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseRef.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String fName = dataSnapshot.child("user_FName").getValue(String.class);
                        String lName = dataSnapshot.child("user_LName").getValue(String.class);
                        String address = dataSnapshot.child("address").getValue(String.class);

                        userFNameTextView.setText(fName);
                        userLNameTextView.setText(lName);
                        userAddressTextView.setText(address);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });

        }
    }
}
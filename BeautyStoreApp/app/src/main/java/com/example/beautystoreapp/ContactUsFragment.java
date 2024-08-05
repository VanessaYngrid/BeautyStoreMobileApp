package com.example.beautystoreapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ContactUsFragment extends Fragment {

    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView addressTextView;
    private Button openGoogleMapsButton;
    private DatabaseReference databaseRef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseRef = FirebaseDatabase.getInstance().getReference("StoreInfo");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_contact_us, container, false);

        emailTextView = v.findViewById(R.id.email_text_view);
        phoneTextView = v.findViewById(R.id.phone_number_text_view);
        addressTextView = v.findViewById(R.id.address_text_view);
        openGoogleMapsButton = v.findViewById(R.id.see_maps_button);

        fetchDataFromFirebase();

        openGoogleMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    private void fetchDataFromFirebase() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Verify if the data is on the firebase
                if (dataSnapshot.exists()) {
                    //Get the values from the firebase
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);

                    //Set the values in the textviews
                    emailTextView.setText(email);
                    phoneTextView.setText(phone);
                    addressTextView.setText(address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar posibles errores
                Log.w("ContactUsFragment", "Failed to read value.", databaseError.toException());
            }
        });
    }
}
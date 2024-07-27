package com.example.beautystoreapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beautystoreapp.model.BeautyTip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AllTipsFragment extends Fragment {

    private Button podcastButton;
    private LinearLayout tipsLinearLayout;
    private DatabaseReference databaseRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseRef = FirebaseDatabase.getInstance().getReference("BeautyTips");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_all_tips, container, false);

        podcastButton = (Button) v.findViewById(R.id.podcast_button);
        tipsLinearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);

        fetchAndDisplayTips();

        podcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://theglossarymagazine.com/beauty-wellness/best-beauty-podcasts/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);

            }
        });

        return v;

    }

    private void fetchAndDisplayTips() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tipsLinearLayout.removeAllViews();  // Clear previous views
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BeautyTip tip = snapshot.getValue(BeautyTip.class);
                    if (tip != null) {
                        addTipToView(tip);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load tips", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTipToView(BeautyTip tip) {
        TextView tipTextView = new TextView(getContext());
        tipTextView.setText(String.format("%s: %s", tip.getTopic(), tip.getTip()));
        tipTextView.setTextSize(16);
        tipTextView.setTextColor(Color.BLACK);
        tipTextView.setBackgroundResource(R.drawable.tip_card_background);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 16, 5, 16);
        tipTextView.setLayoutParams(layoutParams);

        tipsLinearLayout.addView(tipTextView);
    }
}

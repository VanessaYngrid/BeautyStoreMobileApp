package com.example.beautystoreapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beautystoreapp.model.BeautyTip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class BeautyTipsFragment extends Fragment {

    private EditText tipTopicEditText;
    private EditText tipCommentEditText;
    private TextView tipCommentsTextView;
    private Button addTipButton;
    private Button seeAllTipsButton;
    private DatabaseReference databaseRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseRef = FirebaseDatabase.getInstance().getReference("BeautyTips");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_beauty_tips, container, false);

        tipTopicEditText = v.findViewById(R.id.tip_topic_edit_text);
        tipCommentEditText = v.findViewById(R.id.tip_comment_edit_text);
        tipCommentsTextView = v.findViewById(R.id.tip_comments_text_view);
        addTipButton = v.findViewById(R.id.add_tip_button);
        seeAllTipsButton = v.findViewById(R.id.see_all_tips_button);

        addTipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String topic = tipTopicEditText.getText().toString().trim();
                String tip = tipCommentEditText.getText().toString().trim();

                if (topic.isEmpty() || tip.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!topic.isEmpty() && !tip.isEmpty()) {
                    String tipId = databaseRef.push().getKey();
                    BeautyTip newTip = new BeautyTip(topic, tip);
                    if (tipId != null) {
                        databaseRef.child(tipId).setValue(newTip).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Update Successful")
                                        .setMessage("Your tip has been added successfully")
                                        .setPositiveButton("OK", (dialog, which) -> {
                                            tipCommentEditText.setText("");
                                            tipTopicEditText.setText("");
                                            updateTipsDisplay();
                                        }).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to add tip", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

        seeAllTipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create an instance of UpdateProfileFragment
                AllTipsFragment allTipsFragment = new AllTipsFragment();

                // Replace current fragment with UpdateProfileFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, allTipsFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });



        return v;
    }

    private void updateTipsDisplay() {
        //To only show the tip added at that moment
        databaseRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder tipsBuilder = new StringBuilder();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BeautyTip tip = snapshot.getValue(BeautyTip.class);
                    if (tip != null) {
                        tipsBuilder.append(tip.getTopic())
                                .append(": ")
                                .append(tip.getTip())
                                .append("\n\n");
                    }
                }
                tipCommentsTextView.setText(tipsBuilder.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load tips", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
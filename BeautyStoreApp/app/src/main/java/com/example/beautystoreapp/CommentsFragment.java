package com.example.beautystoreapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beautystoreapp.model.Comment;
import com.example.beautystoreapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CommentsFragment extends Fragment {

    private EditText feedbackEditText;
    private Button addFeedbackButton;
    private LinearLayout reviewsContainer;
    private FirebaseDatabase database;
    private DatabaseReference commentsRef;
    private DatabaseReference userRef;
    private FirebaseAuth auth;
    private String userName;
    private int selectedRating = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        commentsRef = database.getReference("comments");
        auth = FirebaseAuth.getInstance();

        // Retrieve user information
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            userRef = database.getReference("Users").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        userName = user.getUser_FName(); // Get the first name of the user
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comments, container, false);

        feedbackEditText = v.findViewById(R.id.feedback_edit_text);
        addFeedbackButton = v.findViewById(R.id.add_feedback_button);
        reviewsContainer = v.findViewById(R.id.reviewsContainer);

        setupStarRating(v);

        addFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addFeedback();
            }
        });

        loadComments();

        return v;
    }

    private void setupStarRating(View v) {
        int[] starIds = {R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star5};
        for (int i = 0; i < starIds.length; i++) {
            ImageButton star = v.findViewById(starIds[i]);
            int rating = i + 1;
            star.setOnClickListener(view -> {
                selectedRating = rating;
                updateStarRating(v, rating);
            });
        }
    }

    private void updateStarRating(View v, int rating) {
        int[] starIds = {R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star5};
        for (int i = 0; i < starIds.length; i++) {
            ImageButton star = v.findViewById(starIds[i]);
            if (i < rating) {
                star.setImageResource(R.drawable.ic_star_filled);
            } else {
                star.setImageResource(R.drawable.ic_star_empty);
            }
        }
    }

    private void addFeedback() {
        String feedback = feedbackEditText.getText().toString().trim();
        if (feedback.isEmpty() || selectedRating == 0 || userName == null) {
            Toast.makeText(getContext(), "Please provide feedback and rating", Toast.LENGTH_SHORT).show();
            return;
        }

        Comment comment = new Comment(userName, feedback, selectedRating);
        commentsRef.push().setValue(comment)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        feedbackEditText.setText("");
                        selectedRating = 0;
                        updateStarRating(getView(), 0);
                        Toast.makeText(getContext(), "Feedback added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to add feedback", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadComments() {
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewsContainer.removeAllViews();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    if (comment != null) {
                        addCommentCard(comment);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCommentCard(Comment comment) {
        TextView commentTextView = new TextView(getContext());
        commentTextView.setText(String.format("User: %s\nRating: %d\n%s", comment.getUserName(), comment.getRating(), comment.getFeedback()));
        commentTextView.setTextSize(16);
        commentTextView.setTextColor(Color.BLACK);
        commentTextView.setBackgroundResource(R.drawable.comment_card_background);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 16, 10, 16);
        commentTextView.setLayoutParams(layoutParams);

        reviewsContainer.addView(commentTextView);
    }
}
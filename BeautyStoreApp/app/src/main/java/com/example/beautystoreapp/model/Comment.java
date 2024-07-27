package com.example.beautystoreapp.model;

public class Comment {

    private String userName;
    private String feedback;
    private int rating;

    public Comment() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Comment(String userName, String feedback, int rating) {
        this.userName = userName;
        this.feedback = feedback;
        this.rating = rating;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "userName='" + userName + '\'' +
                ", feedback='" + feedback + '\'' +
                ", rating=" + rating +
                '}';
    }
}

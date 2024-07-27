package com.example.beautystoreapp.model;

public class BeautyTip {

    private String topic;
    private String tip;

    public BeautyTip() {
        super();
        // TODO Auto-generated constructor stub
    }

    public BeautyTip(String topic, String tip) {
        this.topic = topic;
        this.tip = tip;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return "BeautyTip{" +
                "topic='" + topic + '\'' +
                ", tip='" + tip + '\'' +
                '}';
    }
}

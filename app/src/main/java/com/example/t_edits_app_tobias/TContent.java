package com.example.t_edits_app_tobias;

public class TContent {

    private String ContentCreatedOne;
    private String ContentCreatedTwo;
    private String ContentCreatedThree;
    private String ContentCreatedFour;
    private String ImageUri;

    public TContent(String contentCreatedOne, String contentCreatedTwo, String contentCreatedThree, String contentCreatedFour,String imageUri) {
        ContentCreatedOne = contentCreatedOne;
        ContentCreatedTwo = contentCreatedTwo;
        ContentCreatedThree = contentCreatedThree;
        ContentCreatedFour = contentCreatedFour;
        ImageUri = imageUri;
    }

    public String getContentCreatedOne() {
        return ContentCreatedOne;
    }

    public void setContentCreatedOne(String contentCreatedOne) {
        ContentCreatedOne = contentCreatedOne;
    }

    public String getContentCreatedTwo() {
        return ContentCreatedTwo;
    }

    public void setContentCreatedTwo(String contentCreatedTwo) {
        ContentCreatedTwo = contentCreatedTwo;
    }

    public String getContentCreatedThree() {
        return ContentCreatedThree;
    }

    public void setContentCreatedThree(String contentCreatedThree) {
        ContentCreatedThree = contentCreatedThree;
    }

    public String getContentCreatedFour() {
        return ContentCreatedFour;
    }

    public void setContentCreatedFour(String contentCreatedFour) {
        ContentCreatedFour = contentCreatedFour;
    }

    public TContent(){

    }
//    public String getContentCreated() {
//        return ContentCreated;
//    }
//
//    public void setContentCreated(String contentCreated) {
//        ContentCreated = contentCreated;
//    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }
}

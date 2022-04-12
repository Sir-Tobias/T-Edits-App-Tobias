package com.example.t_edits_app_tobias;

public class QuestionOne {

    private String AnswerOne;
    private String ImageUri;
    private String IconType;
    private String NameType;

    public QuestionOne(String answerOne, String imageUri, String iconType, String nameType) {
        AnswerOne = answerOne;
        ImageUri = imageUri;
        IconType = iconType;
        NameType = nameType;
    }

    public QuestionOne(){
    }

    public String getAnswerOne() {
        return AnswerOne;
    }

    public void setAnswerOne(String answerOne) {
        AnswerOne = answerOne;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }

    public String getIconType() {
        return IconType;
    }

    public void setIconType(String iconType) {
        IconType = iconType;
    }

    public String getNameType() {
        return NameType;
    }

    public void setNameType(String nameType) {
        NameType = nameType;
    }
}

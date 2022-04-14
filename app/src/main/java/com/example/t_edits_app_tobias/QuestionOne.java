package com.example.t_edits_app_tobias;

public class QuestionOne {

    private String AnswerOne;
    private String ImageUri;
    private String LogoType;

    public QuestionOne(String answerOne, String imageUri, String logoType) {
        AnswerOne = answerOne;
        ImageUri = imageUri;
        LogoType = logoType;
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

    public String getLogoType() {
        return LogoType;
    }
    public void setLogoType(String logoType) {
        LogoType = logoType;
    }
}

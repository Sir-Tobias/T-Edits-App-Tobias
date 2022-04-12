package com.example.t_edits_app_tobias;

public class QuestionThree {

    private String AnswerThree;
    private String Apparel;
    private String Media;
    private String Cosmetics;
    private String Tech;

    public QuestionThree(String answerThree, String apparel, String media, String cosmetics, String tech) {
        AnswerThree = answerThree;
        Apparel = apparel;
        Media = media;
        Cosmetics = cosmetics;
        Tech = tech;
    }

    public String getAnswerThree() {
        return AnswerThree;
    }

    public void setAnswerThree(String answerThree) {
        AnswerThree = answerThree;
    }

    public String getApparel() {
        return Apparel;
    }

    public void setApparel(String apparel) {
        Apparel = apparel;
    }

    public String getMedia() {
        return Media;
    }

    public void setMedia(String media) {
        Media = media;
    }

    public String getCosmetics() {
        return Cosmetics;
    }

    public void setCosmetics(String cosmetics) {
        Cosmetics = cosmetics;
    }

    public String getTech() {
        return Tech;
    }

    public void setTech(String tech) {
        Tech = tech;
    }
}

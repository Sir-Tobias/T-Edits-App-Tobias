package com.example.t_edits_app_tobias;

public class QuestionTwo {

    private String AnswerTwo;
    private String Male;
    private String Female;
    private String GenderNeutral;
    private String AgeYoung;
    private String AgeMedium;
    private String AgeOld;

    public QuestionTwo(String answerTwo, String male, String female, String genderNeutral, String ageYoung, String ageMedium, String ageOld) {
        AnswerTwo = answerTwo;
        Male = male;
        Female = female;
        GenderNeutral = genderNeutral;
        AgeYoung = ageYoung;
        AgeMedium = ageMedium;
        AgeOld = ageOld;
    }

    public String getAnswerTwo() {
        return AnswerTwo;
    }

    public void setAnswerTwo(String answerTwo) {
        AnswerTwo = answerTwo;
    }

    public String getMale() {
        return Male;
    }

    public void setMale(String male) {
        Male = male;
    }

    public String getFemale() {
        return Female;
    }

    public void setFemale(String female) {
        Female = female;
    }

    public String getGenderNeutral() {
        return GenderNeutral;
    }

    public void setGenderNeutral(String genderNeutral) {
        GenderNeutral = genderNeutral;
    }

    public String getAgeYoung() {
        return AgeYoung;
    }

    public void setAgeYoung(String ageYoung) {
        AgeYoung = ageYoung;
    }

    public String getAgeMedium() {
        return AgeMedium;
    }

    public void setAgeMedium(String ageMedium) {
        AgeMedium = ageMedium;
    }

    public String getAgeOld() {
        return AgeOld;
    }

    public void setAgeOld(String ageOld) {
        AgeOld = ageOld;
    }
}

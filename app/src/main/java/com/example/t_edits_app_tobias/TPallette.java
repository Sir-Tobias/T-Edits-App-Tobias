package com.example.t_edits_app_tobias;

public class TPallette {

    private String PalletteValue;
    private String ImageUri;

    public TPallette(String palletteValue, String imageUri) {
        PalletteValue = palletteValue;
        ImageUri = imageUri;
    }

    public TPallette() {

    }

    public String getPalletteValue() {
        return PalletteValue;
    }

    public void setPalletteValue(String palletteValue) {
        PalletteValue = palletteValue;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }
}

package com.example.t_edits_app_tobias;

public class TElement {

    private String ElementCreatedOne;
    private String ImageUri;

    public TElement(String elementCreatedOne, String imageUri) {
        ElementCreatedOne = elementCreatedOne;
        ImageUri = imageUri;
    }
    public TElement(){

    }
    public String getElementCreatedOne() {
        return ElementCreatedOne;
    }

    public void setElementCreated(String elementCreatedOne) {
        ElementCreatedOne = elementCreatedOne;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }
}

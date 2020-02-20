package com.example.miniv1;

public class disease_info_model {

    String disease, disease_img, disease_info;

    public disease_info_model(){}

    public disease_info_model(String disease, String disease_img, String disease_info) {
        this.disease = disease;
        this.disease_img = disease_img;
        this.disease_info = disease_info;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getDisease_img() {
        return disease_img;
    }

    public void setDisease_img(String disease_img) {
        this.disease_img = disease_img;
    }

    public String getDisease_info() {
        return disease_info;
    }

    public void setDisease_info(String disease_info) {
        this.disease_info = disease_info;
    }
}

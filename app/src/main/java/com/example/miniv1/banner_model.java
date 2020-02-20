package com.example.miniv1;

public class banner_model {
    String img_url, web_url;

    public banner_model() {}

    public banner_model(String img_url, String web_url) {
        this.img_url = img_url;
        this.web_url = web_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }
}

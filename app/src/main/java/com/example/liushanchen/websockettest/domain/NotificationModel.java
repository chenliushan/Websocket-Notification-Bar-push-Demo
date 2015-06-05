package com.example.liushanchen.websockettest.domain;

/**
 * Created by liushanchen on 15/6/5.
 */
public class NotificationModel {
    private String mabstract;
    private String tittle;
    private String context;

    public String getMabstract() {
        return mabstract;
    }

    public void setMabstract(String mabstract) {
        this.mabstract = mabstract;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

}

package com.official.nanorus.news.entity.data.ip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ip {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}

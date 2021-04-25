package com.example.bookaddict.models;

import java.io.Serializable;

public class DonationModel implements Serializable {
    private String donation_amount, donation_date;

    public DonationModel(String donation_amount, String donation_date) {
        this.donation_amount = donation_amount;
        this.donation_date = donation_date;
    }

    public String getDonation_amount() {
        return donation_amount;
    }

    public void setDonation_amount(String donation_amount) {
        this.donation_amount = donation_amount;
    }

    public String getDonation_date() {
        return donation_date;
    }

    public void setDonation_date(String donation_date) {
        this.donation_date = donation_date;
    }
}

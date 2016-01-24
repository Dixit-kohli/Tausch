package com.scu.tausch.DTO;

/**
 * Created by Pratyusha on 1/22/2016.
 */
public class OfferDTO {

    private String offerId;
    private String postedBy;
    private String categoryId;
    private String offerTitle;
    private String offerDescription;
    private String cityId;
    private int price;
    private boolean offerStatus;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(boolean offerStatus) {
        this.offerStatus = offerStatus;
    }

}

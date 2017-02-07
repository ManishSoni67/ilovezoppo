package co.manishsoni.ilovezappos.Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import co.manishsoni.ilovezappos.Utilities.Utils;

/**
 * Created by manis on 2/2/2017.
 */
public class Results extends BaseObservable {
    private String styleId;

    private String price;

    private String originalPrice;

    private String productUrl;

    private String colorId;

    private String productName;

    private String brandName;

    private String thumbnailImageUrl;

    private String percentOff;

    private String productId;

    private boolean isBusy;

    @Bindable
    public boolean getIsBusy() {
        return isBusy;
    }

    public void setIsBusy(boolean isBusy) {
        this.isBusy = isBusy;
        notifyPropertyChanged(BR.isBusy);
    }


    @Bindable
    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
        notifyPropertyChanged(BR.styleId);
    }

    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);

    }

    @Bindable
    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
        notifyPropertyChanged(BR.originalPrice);
        notifyPropertyChanged(BR.ifPriceChanged);
    }

    @Bindable
    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
        notifyPropertyChanged(BR.productUrl);
    }

    @Bindable
    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
        notifyPropertyChanged(BR.colorId);
    }

    @Bindable
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
        notifyPropertyChanged(BR.productName);
    }

    @Bindable
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
        notifyPropertyChanged(BR.brandName);
    }

    @Bindable
    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
        notifyPropertyChanged(BR.thumbnailImageUrl);
    }

    @Bindable
    public String getPercentOff() {
        return percentOff;

    }

    @Bindable
    public String getPercentOffSaleBanner() {
        return percentOff + " OFF!";
    }

    public void setPercentOff(String percentOff) {
        this.percentOff = percentOff;
        notifyPropertyChanged(BR.percentOff);
        notifyPropertyChanged(BR.percentOffSaleBanner);
    }

    @Bindable
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
        notifyPropertyChanged(BR.productId);
    }

    @Bindable
    public boolean getIfPriceChanged() {

        if (Utils.isValidString(percentOff)) {
            return !percentOff.trim().equals("0%");
        }
        return false;
    }

    @Override
    public String toString() {
        return " [styleId = " + styleId + ", price = " + price + ", originalPrice = " + originalPrice + ", productUrl = " + productUrl + ", colorId = " + colorId + ", productName = " + productName + ", brandName = " + brandName + ", thumbnailImageUrl = " + thumbnailImageUrl + ", percentOff = " + percentOff + ", productId = " + productId + "]";
    }

    public void notifyAllProperties() {
        notifyPropertyChanged(BR.isBusy);
        notifyPropertyChanged(BR.styleId);
        notifyPropertyChanged(BR.price);
        notifyPropertyChanged(BR.originalPrice);
        notifyPropertyChanged(BR.brandName);
        notifyPropertyChanged(BR.percentOff);
        notifyPropertyChanged(BR.percentOffSaleBanner);
        notifyPropertyChanged(BR.ifPriceChanged);
        notifyPropertyChanged(BR.productId);
        notifyPropertyChanged(BR.productUrl);
        notifyPropertyChanged(BR.thumbnailImageUrl);


    }

    public void setProperties(boolean isBusy, String styleId, String price, String originalPrice, String productUrl, String productId
            , String productName, String brandName, String thumbnailImageUrl, String percentOff) {

        setIsBusy(isBusy);
        setStyleId(styleId);
        setPrice(price);
        setOriginalPrice(originalPrice);
        setProductUrl(productUrl);
        setProductId(productId);
        setProductName(productName);
        setBrandName(brandName);
        setThumbnailImageUrl(thumbnailImageUrl);
        setPercentOff(percentOff);

    }

}


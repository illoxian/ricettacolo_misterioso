package com.pape.ricettacolomisterioso.models;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.pape.ricettacolomisterioso.R;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity(tableName = "products")
public class Product implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String product_name;
    private String imageUrl;
    private String brand;
    private String barcode;
    private String dataSource;

    private String category;
    private Date expirationDate;
    private Date purchaseDate;


    public Product(){

    }

    public Product(String product_name, String imageUrl, String brand, String barcode, String dataSource, String category, Date expirationDate, Date purchaseDate) {
        this.product_name = product_name;
        this.imageUrl = imageUrl;
        this.brand = brand;
        this.barcode = barcode;
        this.dataSource = dataSource;
        this.category = category;
        this.expirationDate = expirationDate;
        this.purchaseDate = purchaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getExpirationDateString(){
        if(expirationDate != null)
            return DateFormat.getDateInstance(DateFormat.SHORT).format(expirationDate);
        else return "";
    }

    public String getPurchaseDateString() {
        if (purchaseDate != null)
            return DateFormat.getDateInstance(DateFormat.SHORT).format(purchaseDate);
        else return "";
    }

    @Override
    public String toString() {
        return "Product{" +
                ", id='" + id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", brand='" + brand + '\'' +
                ", barcode='" + barcode + '\'' +
                ", dataSource='" + dataSource + '\'' +
                ", category='" + category + '\'' +
                ", expirationDate=" + getExpirationDateString() +
                ", purchaseDate=" + getPurchaseDateString() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.id);
        out.writeString(this.product_name);
        out.writeString(this.imageUrl);
        out.writeString(this.brand);
        out.writeString(this.barcode);
        out.writeString(this.dataSource);
        out.writeString(this.category);

        if(this.expirationDate==null) out.writeString(null);
        else out.writeLong(this.expirationDate.getTime());

        if(this.purchaseDate==null) out.writeString(null);
        else out.writeLong(this.purchaseDate.getTime());
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Product(Parcel in) {

        this.id = in.readInt();
        this.product_name = in.readString();
        this.imageUrl = in.readString();
        this.brand = in.readString();
        this.barcode = in.readString();
        this.dataSource = in.readString();
        this.category = in.readString();
        this.expirationDate = new Date(in.readLong());
        this.purchaseDate = new Date(in.readLong());
    }

    public int getCategoryIconId(Context context){
        Resources res = context.getResources();
        TypedArray icons = res.obtainTypedArray(R.array.categoriesIcon);
        List categories = Arrays.asList(res.getStringArray(R.array.categoriesString));

        int index = categories.indexOf(getCategory());
        int resourceId = icons.getResourceId(index, -1);
        icons.recycle();
        return resourceId;
    }

    public int getCategoryPreviewId(Context context){
        Resources res = context.getResources();
        TypedArray previews = res.obtainTypedArray(R.array.categoriesPreviews);
        List categories = Arrays.asList(res.getStringArray(R.array.categoriesString));

        int index = categories.indexOf(getCategory());
        int resourceId = previews.getResourceId(index, -1);
        previews.recycle();
        return resourceId;
    }
}

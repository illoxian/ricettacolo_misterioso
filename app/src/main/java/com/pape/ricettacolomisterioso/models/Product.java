package com.pape.ricettacolomisterioso.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Date;

@Entity(tableName = "products")
public class Product implements Parcelable {
    @PrimaryKey @NotNull
    private String product_name;

    private String category;
    private Date expirationDate;

    public Product() {}

    public Product(String product_name, String category, Date expirationDate) {
        this.product_name = product_name;
        this.category = category;
        this.expirationDate = expirationDate;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
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

    @Override
    public String toString() {
        String dateString = "";
        if(expirationDate != null)
            dateString = DateFormat.getDateInstance(DateFormat.SHORT).format(expirationDate);
        return "Product{" +
                "productName='" + product_name + '\'' +
                ", category='" + category + '\'' +
                ", expirationDate=" + dateString +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.product_name);
        out.writeString(this.category);
        if(this.expirationDate==null) out.writeString(null);
        else out.writeLong(this.expirationDate.getTime());
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
        this.product_name = in.readString();
        this.category = in.readString();
        this.expirationDate = new Date(in.readLong());
    }
}

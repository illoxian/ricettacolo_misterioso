package com.pape.ricettacolomisterioso.models;

public class OFFProductApiResponse {
    private OFFProductFromApi product;
    private String status_verbose;
    private int status;
    private String code;

    public OFFProductApiResponse(OFFProductFromApi product, String status_verbose, int status, String code) {
        this.product = product;
        this.status_verbose = status_verbose;
        this.status = status;
        this.code = code;
    }

    public Product getProduct() {
        Product p = new Product();
        p.setProduct_name(product.getProduct_name());
        p.setImageUrl(product.getImage_url());
        p.setBrand(product.getBrands());
        p.setBarcode(product.getCode());
        p.setDataSource("Open Food Facts");
        //Log.d("ProductApiResponse", "getProduct: "+product.toString());
        return p;
    }

    public void setProduct(OFFProductFromApi product) {
        this.product = product;
    }

    public String getStatus_verbose() {
        return status_verbose;
    }

    public void setStatus_verbose(String status_verbose) {
        this.status_verbose = status_verbose;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ProductApiResponse{" +
                "product=" + product +
                ", status_verbose='" + status_verbose + '\'' +
                ", status=" + status +
                ", code='" + code + '\'' +
                '}';
    }
}

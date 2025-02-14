package com.example.ecommerce;

public class CrtDtaFirbsModel {
    private String ProductImageUrl;
    private String NameOfProduct;
    private String PriceOfProduct;
  private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductImageUrl() {
        return ProductImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        ProductImageUrl = productImageUrl;
    }

    public String getNameOfProduct() {
        return NameOfProduct;
    }

    public void setNameOfProduct(String nameOfProduct) {
        NameOfProduct = nameOfProduct;
    }



    public String getPriceOfProduct() {
        return PriceOfProduct;
    }

    public void setPriceOfProduct(String priceOfProduct) {
        PriceOfProduct = priceOfProduct;
    }
}

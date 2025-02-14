package com.example.ecommerce;

import java.util.List;


public class ApiResponse {
   // private String status; // If you have a status in your API response
    private List<ecomproducts> recipes; // Change "data" to "recipes"

    // Getters and Setters
//    public String getStatus() {
//        return status;
//    }

//    public void setStatus(String status) {
//        this.status = status;
//    }

    public List<ecomproducts> getRecipes() { // Updated method to return recipes
        return recipes;
    }

    public void setRecipes(List<ecomproducts> recipes) { // Updated setter
        this.recipes = recipes;
    }
}


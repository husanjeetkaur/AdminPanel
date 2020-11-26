package com.example.firestoredataapp;

public class Model {

    String Image, Price, Description, Category, SubCategory, Id;

    public Model(String category, String subcategory, String name, String desc, String image, String id) {
        Id = id;
        Image = image;
        Price = name;
        Description = desc;
        Category = category;
        SubCategory = subcategory;
    }


    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}

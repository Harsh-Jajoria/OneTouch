package com.axepert.onetouch.responses;

import java.util.List;

public class ECommData {
    public List<ProductCategory> category;
    public List<TrendingProduct> trending_product;
    public List<PopularProduct> popular_product;

    public List<ProductCategory> getCategory() {
        return category;
    }

    public List<TrendingProduct> getTrending_product() {
        return trending_product;
    }

    public List<PopularProduct> getPopular_product() {
        return popular_product;
    }
}

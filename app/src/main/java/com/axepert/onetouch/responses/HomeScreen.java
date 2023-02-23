package com.axepert.onetouch.responses;

import java.util.List;

public class HomeScreen {
    public List<Banners> banner;
    public List<Services> services;
    public List<Category> category;
    public List<ServiceProviders> service_provider;
    public List<SellerInformation> seller_info;
    public List<Blog> blogs;

    public List<Banners> getBanner() {
        return banner;
    }

    public List<Services> getServices() {
        return services;
    }

    public List<Category> getCategory() {
        return category;
    }

    public List<ServiceProviders> getService_provider() {
        return service_provider;
    }

    public List<SellerInformation> getSeller_info() {
        return seller_info;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }
}

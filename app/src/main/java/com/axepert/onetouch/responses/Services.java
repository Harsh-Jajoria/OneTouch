package com.axepert.onetouch.responses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

public class Services {

    public String id;

    public String service_type;

    public String description;

    public String services_image;

    public String slug;

    public int provider_count;

    public Services(String id, String service_type, String description, String services_image, String slug, int provider_count) {
        this.id = id;
        this.service_type = service_type;
        this.description = description;
        this.services_image = services_image;
        this.slug = slug;
        this.provider_count = provider_count;
    }

    public String getId() {
        return id;
    }

    public String getService_type() {
        return service_type;
    }

    public String getDescription() {
        return description;
    }

    public String getServices_image() {
        return services_image;
    }

    public String getSlug() {
        return slug;
    }

    public int getProvider_count() {
        return provider_count;
    }


}

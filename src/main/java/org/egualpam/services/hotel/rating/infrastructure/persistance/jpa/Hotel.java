package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    private Long id;
    @Column(name = "global_identifier")
    private String globalIdentifier;
    private String name;
    private String description;
    private String location;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "image_url")
    private String imageURL;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGlobalIdentifier() {
        return globalIdentifier;
    }

    public void setGlobalIdentifier(String identifier) {
        this.globalIdentifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}

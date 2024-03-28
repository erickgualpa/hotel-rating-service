package org.egualpam.services.hotelmanagement.reviews.domain;

import org.egualpam.services.hotelmanagement.shared.domain.UniqueId;

import java.util.UUID;

// TODO: Avoid having UUID at this level
public record HotelId(UUID value) {
    public HotelId(String value) {
        this(new UniqueId(value).value());
    }
}
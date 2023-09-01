package org.egualpam.services.hotel.rating.infrastructure.persistance;

public record HotelDto(
        Long id,
        String name,
        String description,
        String location,
        Integer totalPrice,
        String imageURL) {
}
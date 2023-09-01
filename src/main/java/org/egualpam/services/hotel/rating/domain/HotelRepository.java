package org.egualpam.services.hotel.rating.domain;

import org.egualpam.services.hotel.rating.application.HotelQuery;

import java.util.List;
import java.util.UUID;

public abstract class HotelRepository {

    abstract public List<Hotel> findHotelsMatchingQuery(HotelQuery query);

    protected Hotel buildEntity(
            String identifier,
            String name,
            String description,
            String locationName,
            Integer totalPrice,
            String imageURL
    ) {
        return new Hotel(
                identifier,
                name,
                description,
                // TODO: Decide what how 'Location' will be managed (Entity or Value)
                new Location(UUID.randomUUID().toString(), locationName),
                totalPrice,
                imageURL
        );
    }
}
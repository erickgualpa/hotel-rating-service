package org.egualpam.services.hotel.rating.application.hotels;

import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.hotels.Location;
import org.egualpam.services.hotel.rating.domain.hotels.Price;

import java.util.List;
import java.util.Optional;

public final class FindHotelsByRatingAverage {

    private final HotelRepository hotelRepository;

    public FindHotelsByRatingAverage(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<HotelDto> execute(Filters filters) {
        Optional<Location> location = Optional.ofNullable(filters.location())
                .map(Location::new);

        Optional<Price> minPrice = Optional.ofNullable(filters.priceBegin())
                .map(Price::new);

        Optional<Price> maxPrice = Optional.ofNullable(filters.priceEnd())
                .map(Price::new);

        return hotelRepository.find(location, minPrice, maxPrice)
                .stream()
                .map(this::mapIntoHotelDto)
                .toList();
    }

    private HotelDto mapIntoHotelDto(Hotel hotel) {
        return new HotelDto(
                hotel.getIdentifier().value(),
                hotel.getName().value(),
                hotel.getDescription().value(),
                hotel.getLocation().value(),
                hotel.getTotalPrice().value(),
                hotel.getImageURL().value(),
                hotel.getAverageRating().value()
        );
    }
}
package org.egualpam.services.hotelmanagement.domain.hotels;

import org.egualpam.services.hotelmanagement.domain.shared.AggregateId;
import org.egualpam.services.hotelmanagement.domain.shared.Criteria;

import java.util.Optional;

public final class HotelCriteria implements Criteria {
    private final Optional<AggregateId> hotelId;
    private final Optional<Location> location;
    private final PriceRange priceRange;

    // TODO: Split this into two different Criteria classes to avoid Optional parameters
    public HotelCriteria(AggregateId hotelId) {
        this.hotelId = Optional.of(hotelId);
        this.location = Optional.empty();
        this.priceRange = new PriceRange(
                Optional.empty(),
                Optional.empty()
        );
    }

    public HotelCriteria(
            Optional<Location> location,
            PriceRange priceRange
    ) {
        this.location = location;
        this.priceRange = priceRange;
        this.hotelId = Optional.empty();
    }

    public Optional<Location> getLocation() {
        return location;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public Optional<AggregateId> getHotelId() {
        return hotelId;
    }
}
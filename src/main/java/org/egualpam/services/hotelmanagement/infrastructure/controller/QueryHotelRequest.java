package org.egualpam.services.hotelmanagement.infrastructure.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record QueryHotelRequest(String location, PriceRange priceRange) {
    record PriceRange(Integer begin, Integer end) {
    }
}

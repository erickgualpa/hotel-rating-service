package org.egualpam.services.hotel.rating.controller;

import java.time.LocalDate;

public final class HotelQuery {

    private String location;
    private LocalDate checkIn;
    private LocalDate checkOut;

    // Making this an object with a tuple of (begin, end) in order to ease the dev
    private PriceRange priceRange;

    public HotelQuery() {}

    public HotelQuery(
            String location, LocalDate checkIn, LocalDate checkOut, PriceRange priceRange) {
        this.location = location;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.priceRange = priceRange;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public static class PriceRange {

        private final Integer begin;
        private final Integer end;

        public PriceRange(Integer begin, Integer end) {
            this.begin = begin;
            this.end = end;
        }

        public Integer getBegin() {
            return begin;
        }

        public Integer getEnd() {
            return end;
        }
    }
}

package org.egualpam.services.hotel.rating.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class Hotel {

    private final String identifier;
    private final String name;
    private final String description;
    private final Location location;
    private final Integer totalPrice;
    private final String imageURL;

    private final List<Review> reviews = new ArrayList<>();

    public void addReviews(List<Review> reviews) {
        this.reviews.addAll(reviews);
    }

    public Double calculateRatingAverage() {
        return this.reviews.stream()
                .mapToDouble(Review::getRating)
                .filter(Objects::nonNull)
                .average()
                .orElse(0.0);
    }
}
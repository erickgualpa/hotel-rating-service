package org.egualpam.services.hotelmanagement.application.reviews;

import org.egualpam.services.hotelmanagement.application.shared.View;

import java.util.List;

public record ReviewsView(List<Review> reviews) implements View {
    public record Review(Integer rating, String comment) {
    }
}
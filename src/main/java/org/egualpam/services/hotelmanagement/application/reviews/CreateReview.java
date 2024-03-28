package org.egualpam.services.hotelmanagement.application.reviews;

import org.egualpam.services.hotelmanagement.application.shared.InternalCommand;
import org.egualpam.services.hotelmanagement.domain.reviews.Review;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateRepository;
import org.egualpam.services.hotelmanagement.domain.shared.DomainEventsBus;

public final class CreateReview implements InternalCommand {

    private final String reviewId;
    private final String hotelId;
    private final Integer rating;
    private final String comment;

    private final AggregateRepository<Review> reviewRepository;
    private final DomainEventsBus domainEventsBus;

    public CreateReview(
            String reviewId,
            String hotelId,
            Integer rating,
            String comment,
            AggregateRepository<Review> reviewRepository,
            DomainEventsBus domainEventsBus
    ) {
        this.reviewId = reviewId;
        this.hotelId = hotelId;
        this.rating = rating;
        this.comment = comment;
        this.reviewRepository = reviewRepository;
        this.domainEventsBus = domainEventsBus;
    }

    @Override
    public void execute() {
        Review review = Review.create(
                reviewRepository,
                reviewId,
                hotelId,
                rating,
                comment
        );
        reviewRepository.save(review);
        domainEventsBus.publish(review.pullDomainEvents());
    }
}

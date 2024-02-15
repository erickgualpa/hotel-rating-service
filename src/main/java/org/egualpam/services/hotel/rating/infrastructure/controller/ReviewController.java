package org.egualpam.services.hotel.rating.infrastructure.controller;


import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.application.reviews.ReviewDto;
import org.egualpam.services.hotel.rating.application.shared.Command;
import org.egualpam.services.hotel.rating.application.shared.Query;
import org.egualpam.services.hotel.rating.domain.shared.InvalidIdentifier;
import org.egualpam.services.hotel.rating.domain.shared.InvalidRating;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.CommandBus;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.CommandFactory;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.QueryBus;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.QueryFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final CommandFactory commandFactory;
    private final CommandBus commandBus;
    private final QueryFactory queryFactory;
    private final QueryBus queryBus;

    @GetMapping
    public ResponseEntity<GetReviewsResponse> findReviews(@RequestParam String hotelIdentifier) {
        Query<List<ReviewDto>> findHotelReviewsQuery = queryFactory
                .findHotelReviewsQuery(hotelIdentifier);
        List<GetReviewsResponse.Review> reviews =
                queryBus.publish(findHotelReviewsQuery)
                        .stream()
                        .map(
                                r -> new GetReviewsResponse.Review(
                                        r.rating(),
                                        r.comment()))
                        .toList();
        return ResponseEntity.ok(new GetReviewsResponse(reviews));
    }

    @PostMapping(path = "/{reviewIdentifier}")
    public ResponseEntity<Void> createReview(@PathVariable String reviewIdentifier,
                                             @RequestBody CreateReviewRequest createReviewRequest) {
        try {
            Command createReviewCommand =
                    commandFactory
                            .createReviewCommand(
                                    reviewIdentifier,
                                    createReviewRequest.hotelIdentifier(),
                                    createReviewRequest.rating(),
                                    createReviewRequest.comment());
            commandBus.publish(createReviewCommand);
        } catch (InvalidIdentifier | InvalidRating e) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

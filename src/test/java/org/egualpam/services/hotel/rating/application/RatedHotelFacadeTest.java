package org.egualpam.services.hotel.rating.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import org.egualpam.services.hotel.rating.domain.Location;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.egualpam.services.hotel.rating.domain.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// TODO: Clean up this test class
@ExtendWith(MockitoExtension.class)
class RatedHotelFacadeTest {

    private static final Location EXPECTED_LOCATION = new Location("BCN", "Barcelona");

    private static final String EXPECTED_HOTEL_IDENTIFIER = "AMZ_HOTEL";
    private static final String EXPECTED_NAME = "Amazing hotel";
    private static final String EXPECTED_DESCRIPTION = "Eloquent description";
    private static final String EXPECTED_IMAGE_URL = "amz-hotel-image.com";

    private static final Integer EXPECTED_TOTAL_PRICE = 200;
    private static final String EXPECTED_REVIEW_IDENTIFIER = "AMZ_REVIEW";
    private static final int EXPECTED_REVIEW_RATING = 5;
    private static final String EXPECTED_REVIEW_COMMENT = "Eloquent comment";

    private static final String EXPECTED_WORST_HOTEL_IDENTIFIER = "EXPECTED_WORST_HOTEL_IDENTIFIER";
    private static final String EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER =
            "EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER";
    private static final String EXPECTED_BEST_HOTEL_IDENTIFIER = "EXPECTED_BEST_HOTEL_IDENTIFIER";

    private static final HotelQuery DEFAULT_QUERY = HotelQuery.create().build();

    @Mock private RatedHotelRepository ratedHotelRepository;

    private RatedHotelFacade testee;

    @BeforeEach
    void setup() {
        testee = new RatedHotelFacade(ratedHotelRepository);
    }

    @Test
    void givenAnyQuery_hotelsMatchingQueryShouldBeReturned() {
        RatedHotel defaultHotel = buildHotelStub(EXPECTED_HOTEL_IDENTIFIER);

        when(ratedHotelRepository.findHotelsMatchingQuery(any(HotelQuery.class)))
                .thenReturn(List.of(defaultHotel));

        List<RatedHotel> result = testee.findHotelsMatchingQuery(DEFAULT_QUERY);

        assertThat(result).hasSize(1);
        RatedHotel actualHotel = result.get(0);
        assertThat(actualHotel.getIdentifier()).isEqualTo(EXPECTED_HOTEL_IDENTIFIER);
        assertThat(actualHotel.getName()).isEqualTo(EXPECTED_NAME);
        assertThat(actualHotel.getDescription()).isEqualTo(EXPECTED_DESCRIPTION);
        assertThat(actualHotel.getLocation()).isEqualTo(EXPECTED_LOCATION);
        assertThat(actualHotel.getTotalPrice()).isEqualTo(EXPECTED_TOTAL_PRICE);
        assertThat(actualHotel.getImageURL()).isEqualTo(EXPECTED_IMAGE_URL);
    }

    @Test
    void givenAnyQuery_hotelsMatchingQueryShouldBePopulatedWithReviewsAndReturned() {
        RatedHotel defaultHotel = buildHotelStub(EXPECTED_HOTEL_IDENTIFIER);
        Review defaultReview = buildReviewStub(EXPECTED_REVIEW_RATING);

        // TODO: Review if it is correct allowing this here
        defaultHotel.populateReviews(List.of(defaultReview));

        when(ratedHotelRepository.findHotelsMatchingQuery(any(HotelQuery.class)))
                .thenReturn(List.of(defaultHotel));

        List<RatedHotel> result = testee.findHotelsMatchingQuery(DEFAULT_QUERY);

        assertThat(result).hasSize(1);
        RatedHotel actualHotel = result.get(0);
        assertThat(actualHotel.getReviews()).hasSize(1);
        Review actualReview = actualHotel.getReviews().get(0);
        assertThat(actualReview.getIdentifier()).isEqualTo(EXPECTED_REVIEW_IDENTIFIER);
        assertThat(actualReview.getRating()).isEqualTo(EXPECTED_REVIEW_RATING);
        assertThat(actualReview.getComment()).isEqualTo(EXPECTED_REVIEW_COMMENT);
    }

    @Test
    void givenAnyQuery_hotelsMatchingQueryShouldBeReturnedOrderedByRatingAverage() {
        RatedHotel expectedWorstHotel = buildHotelStub(EXPECTED_WORST_HOTEL_IDENTIFIER);
        List<Review> expectedWorstHotelReviews =
                List.of(buildReviewStub(1), buildReviewStub(2));
        // TODO: Review if it is correct allowing this here
        expectedWorstHotel.populateReviews(expectedWorstHotelReviews);

        RatedHotel expectedIntermediateHotel =
                buildHotelStub(EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER);
        List<Review> expectedIntermediateHotelReviews =
                List.of(buildReviewStub(2), buildReviewStub(4));
        // TODO: Review if it is correct allowing this here
        expectedIntermediateHotel.populateReviews(expectedIntermediateHotelReviews);

        RatedHotel expectedBestHotel = buildHotelStub(EXPECTED_BEST_HOTEL_IDENTIFIER);
        List<Review> expectedBestHotelReviews =
                List.of(buildReviewStub(4), buildReviewStub(5));
        // TODO: Review if it is correct allowing this here
        expectedBestHotel.populateReviews(expectedBestHotelReviews);

        when(ratedHotelRepository.findHotelsMatchingQuery(any(HotelQuery.class)))
                .thenReturn(
                        List.of(expectedIntermediateHotel, expectedWorstHotel, expectedBestHotel));

        List<RatedHotel> result = testee.findHotelsMatchingQuery(DEFAULT_QUERY);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getIdentifier()).isEqualTo(EXPECTED_BEST_HOTEL_IDENTIFIER);
        assertThat(result.get(1).getIdentifier()).isEqualTo(EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER);
        assertThat(result.get(2).getIdentifier()).isEqualTo(EXPECTED_WORST_HOTEL_IDENTIFIER);
    }

    private RatedHotel buildHotelStub(String identifier) {
        return new RatedHotel(
                identifier,
                EXPECTED_NAME,
                EXPECTED_DESCRIPTION,
                EXPECTED_LOCATION,
                EXPECTED_TOTAL_PRICE,
                EXPECTED_IMAGE_URL);
    }

    private Review buildReviewStub(int rating) {
        return new Review(EXPECTED_REVIEW_IDENTIFIER, rating, EXPECTED_REVIEW_COMMENT);
    }
}

package org.egualpam.services.hotel.rating.application;

import org.egualpam.services.hotel.rating.application.hotels.FindHotelsByRatingAverage;
import org.egualpam.services.hotel.rating.application.hotels.HotelDto;
import org.egualpam.services.hotel.rating.application.hotels.HotelQuery;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindHotelsByRatingAverageTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ReviewRepository reviewRepository;

    private FindHotelsByRatingAverage testee;

    @BeforeEach
    void setup() {
        testee = new FindHotelsByRatingAverage(hotelRepository, reviewRepository);
    }

    @Test
    void hotelQueryIsProperlyBuilt() {
        String location = randomAlphabetic(5);
        HotelQuery result = HotelQuery.create()
                .withLocation(location)
                .withPriceRange(nextInt(50, 1000), nextInt(50, 100))
                .build();

        assertThat(result.getLocation()).isEqualTo(location);
        assertThat(result.getPriceRange())
                .isNotNull()
                .satisfies(
                        actualPriceRange -> {
                            Integer actualMinPrice = actualPriceRange.begin();
                            Integer actualMaxPrice = actualPriceRange.end();
                            assertNotNull(actualMinPrice);
                            assertNotNull(actualMaxPrice);
                            assertThat(actualMaxPrice).isGreaterThanOrEqualTo(actualMinPrice);
                        }
                );
    }

    @Test
    void hotelsMatchingQueryShouldBeReturnedSortedByRatingAverage() {

        HotelQuery hotelQuery = HotelQuery.create().build();

        String intermediateHotelIdentifier = randomUUID().toString();
        String worstHotelIdentifier = randomUUID().toString();
        String bestHotelIdentifier = randomUUID().toString();

        when(hotelRepository.findHotelsMatchingQuery(hotelQuery))
                .thenReturn(
                        List.of(
                                buildHotelStubWithIdentifier(intermediateHotelIdentifier),
                                buildHotelStubWithIdentifier(worstHotelIdentifier),
                                buildHotelStubWithIdentifier(bestHotelIdentifier)));

        when(reviewRepository.findByHotelIdentifier(worstHotelIdentifier))
                .thenReturn(
                        List.of(
                                buildReviewStubWithRating(1),
                                buildReviewStubWithRating(2)));

        when(reviewRepository.findByHotelIdentifier(intermediateHotelIdentifier))
                .thenReturn(
                        List.of(
                                buildReviewStubWithRating(3),
                                buildReviewStubWithRating(3)));

        when(reviewRepository.findByHotelIdentifier(bestHotelIdentifier))
                .thenReturn(
                        List.of(
                                buildReviewStubWithRating(4),
                                buildReviewStubWithRating(5)));

        List<HotelDto> result = testee.execute(hotelQuery);

        assertThat(result).hasSize(3)
                .extracting("identifier")
                .containsExactly(
                        bestHotelIdentifier,
                        intermediateHotelIdentifier,
                        worstHotelIdentifier
                );

        assertThat(result)
                .allSatisfy(
                        hotelDto -> assertThat(hotelDto.reviews()).isNotEmpty());
    }

    private Hotel buildHotelStubWithIdentifier(String identifier) {
        return new Hotel(
                identifier,
                randomAlphabetic(5),
                randomAlphabetic(10),
                randomAlphabetic(5),
                nextInt(50, 1000),
                randomUUID().toString());
    }

    private Review buildReviewStubWithRating(int rating) {
        return new Review(randomUUID().toString(), rating, randomAlphabetic(10));
    }
}
package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.domain.Review;
import org.egualpam.services.hotel.rating.domain.ReviewRepository;
import org.egualpam.services.hotel.rating.helpers.HotelTestRepository;
import org.egualpam.services.hotel.rating.helpers.ReviewTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext
public class PostgreSqlJpaReviewRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private HotelTestRepository hotelTestRepository;

    @Autowired
    private ReviewTestRepository reviewTestRepository;

    @Autowired
    private ReviewRepository testee;

    @Test
    void givenHotelIdentifier_allMatchingReviewShouldBeReturned() {

        UUID hotelIdentifier = UUID.randomUUID();

        hotelTestRepository.insertHotelWithIdentifier(hotelIdentifier);
        reviewTestRepository.insertReviewWithRatingAndCommentAndHotelIdentifier(
                5,
                "This is an amazing hotel!",
                hotelIdentifier
        );

        List<Review> result = testee.findByHotelIdentifier(hotelIdentifier.toString());

        assertThat(result)
                .hasSize(1)
                .allSatisfy(
                        review -> {
                            assertNotNull(review.getIdentifier());
                            assertThat(review.getRating()).isEqualTo(5);
                            assertThat(review.getComment())
                                    .isEqualTo("This is an amazing hotel!");
                        }
                );
    }
}

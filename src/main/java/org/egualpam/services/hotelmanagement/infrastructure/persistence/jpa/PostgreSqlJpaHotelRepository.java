package org.egualpam.services.hotelmanagement.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import org.egualpam.services.hotelmanagement.domain.hotels.AverageRating;
import org.egualpam.services.hotelmanagement.domain.hotels.Hotel;
import org.egualpam.services.hotelmanagement.domain.hotels.HotelCriteria;
import org.egualpam.services.hotelmanagement.domain.hotels.HotelDescription;
import org.egualpam.services.hotelmanagement.domain.hotels.HotelName;
import org.egualpam.services.hotelmanagement.domain.hotels.ImageURL;
import org.egualpam.services.hotelmanagement.domain.hotels.Location;
import org.egualpam.services.hotelmanagement.domain.hotels.Price;
import org.egualpam.services.hotelmanagement.domain.hotels.PriceRange;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateId;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateRepository;
import org.egualpam.services.hotelmanagement.domain.shared.Criteria;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class PostgreSqlJpaHotelRepository implements AggregateRepository<Hotel> {

    private final EntityManager entityManager;
    private final Function<PersistenceHotel, List<PersistenceReview>> findReviewsByHotel;

    public PostgreSqlJpaHotelRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.findReviewsByHotel = new FindReviewsByHotel(entityManager);
    }

    @Override
    public Optional<Hotel> find(AggregateId id) {
        PersistenceHotel persistenceHotel = entityManager.find(PersistenceHotel.class, id.value());
        return Optional.ofNullable(persistenceHotel)
                .map(this::mapResultIntoHotel);
    }

    @Override
    public List<Hotel> find(Criteria criteria) {
        PriceRange priceRange = ((HotelCriteria) criteria).getPriceRange();
        Optional<String> location = ((HotelCriteria) criteria).getLocation().map(Location::value);

        CriteriaQuery<PersistenceHotel> criteriaQuery =
                new HotelCriteriaQueryBuilder(entityManager)
                        .withLocation(location)
                        .withMinPrice(priceRange.minPrice().map(Price::value))
                        .withMaxPrice(priceRange.maxPrice().map(Price::value))
                        .build();

        return entityManager
                .createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .map(this::mapResultIntoHotel)
                .toList();
    }

    private Hotel mapResultIntoHotel(PersistenceHotel persistenceHotel) {
        String name = persistenceHotel.getName();
        String description = persistenceHotel.getDescription();
        String location = persistenceHotel.getLocation();
        Integer totalPrice = persistenceHotel.getTotalPrice();
        String imageURL = persistenceHotel.getImageURL();
        AverageRating averageRating = new AverageRating(
                findReviewsByHotel
                        .apply(persistenceHotel)
                        .stream()
                        .mapToDouble(PersistenceReview::getRating)
                        .filter(Objects::nonNull)
                        .average()
                        .orElse(0.0)
        );
        return new Hotel(
                new AggregateId(persistenceHotel.getId()),
                new HotelName(name),
                new HotelDescription(description),
                new Location(location),
                new Price(totalPrice),
                new ImageURL(imageURL),
                averageRating
        );
    }

    @Override
    public void save(Hotel aggregate) {
        throw new RuntimeException("NOT_IMPLEMENTED");
    }
}
package org.egualpam.services.hotelmanagement.infrastructure.cqrs.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.application.hotels.HotelView;
import org.egualpam.services.hotelmanagement.application.hotels.HotelsView;
import org.egualpam.services.hotelmanagement.application.reviews.ReviewsView;
import org.egualpam.services.hotelmanagement.application.shared.Query;
import org.egualpam.services.hotelmanagement.application.shared.QueryBus;
import org.egualpam.services.hotelmanagement.application.shared.View;
import org.egualpam.services.hotelmanagement.application.shared.ViewSupplier;
import org.egualpam.services.hotelmanagement.domain.hotels.HotelCriteria;
import org.egualpam.services.hotelmanagement.domain.reviews.ReviewCriteria;

import java.util.Map;

@FunctionalInterface
interface QueryHandler {
    View handle(Query query);
}

public final class SimpleQueryBus implements QueryBus {

    private final Map<Class<? extends Query>, QueryHandler> handlers;

    public SimpleQueryBus(
            ViewSupplier<HotelView> hotelViewSupplier,
            ViewSupplier<HotelsView> hotelsViewSupplier,
            ViewSupplier<ReviewsView> reviewsViewSupplier
    ) {
        handlers = Map.of(
                FindHotelReviewsQuery.class,
                new FindHotelReviewsQueryHandler(reviewsViewSupplier),
                FindHotelsQuery.class,
                new FindHotelsQueryHandler(hotelsViewSupplier),
                FindHotelQuery.class,
                new FindHotelQueryHandler(hotelViewSupplier)
        );
    }

    @Override
    public View publish(Query query) {
        QueryHandler queryHandler = handlers.get(query.getClass());
        if (queryHandler == null) {
            throw new QueryHandlerNotFound();
        }
        return queryHandler.handle(query);
    }

    @RequiredArgsConstructor
    static class FindHotelReviewsQueryHandler implements QueryHandler {

        private final ViewSupplier<ReviewsView> reviewsViewSupplier;

        @Override
        public View handle(Query query) {
            String hotelId = ((FindHotelReviewsQuery) query).getHotelIdentifier();
            return reviewsViewSupplier.get(
                    new ReviewCriteria(hotelId)
            );
        }
    }

    @RequiredArgsConstructor
    static class FindHotelsQueryHandler implements QueryHandler {

        private final ViewSupplier<HotelsView> hotelsViewSupplier;

        @Override
        public View handle(Query query) {
            return hotelsViewSupplier.get(
                    new HotelCriteria(
                            ((FindHotelsQuery) query).getLocation(),
                            ((FindHotelsQuery) query).getMinPrice(),
                            ((FindHotelsQuery) query).getMaxPrice()
                    )
            );
        }
    }

    @RequiredArgsConstructor
    static class FindHotelQueryHandler implements QueryHandler {

        private final ViewSupplier<HotelView> hotelViewSupplier;

        @Override
        public View handle(Query query) {
            String hotelId = ((FindHotelQuery) query).getHotelId();
            return hotelViewSupplier.get(
                    new HotelCriteria(hotelId)
            );
        }
    }
}

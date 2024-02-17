package org.egualpam.services.hotel.rating.infrastructure.cqrs.simple;

import org.egualpam.services.hotel.rating.application.reviews.CreateReview;
import org.egualpam.services.hotel.rating.application.reviews.UpdateReview;
import org.egualpam.services.hotel.rating.application.shared.Command;
import org.egualpam.services.hotel.rating.application.shared.CommandBus;
import org.egualpam.services.hotel.rating.application.shared.InternalCommand;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;

import java.util.Map;

@FunctionalInterface
interface CommandHandler {
    void handle(Command query);
}

public final class SimpleCommandBus implements CommandBus {

    private final Map<Class<? extends Command>, CommandHandler> handlers;

    public SimpleCommandBus(ReviewRepository reviewRepository) {
        handlers = Map.of(
                CreateReviewCommand.class, new CreateReviewCommandHandler(reviewRepository),
                UpdateReviewCommand.class, new UpdateReviewCommandHandler(reviewRepository)
        );
    }

    @Override
    public void publish(Command command) {
        CommandHandler commandHandler = handlers.get(command.getClass());
        if (commandHandler == null) {
            throw new CommandHandlerNotFound();
        }
        commandHandler.handle(command);
    }

    static class CreateReviewCommandHandler implements CommandHandler {

        private final ReviewRepository reviewRepository;

        public CreateReviewCommandHandler(ReviewRepository reviewRepository) {
            this.reviewRepository = reviewRepository;
        }

        @Override
        public void handle(Command query) {
            InternalCommand internalCommand =
                    new CreateReview(
                            ((CreateReviewCommand) query).getReviewIdentifier(),
                            ((CreateReviewCommand) query).getHotelIdentifier(),
                            ((CreateReviewCommand) query).getRating(),
                            ((CreateReviewCommand) query).getComment(),
                            reviewRepository
                    );
            internalCommand.execute();
        }
    }

    static class UpdateReviewCommandHandler implements CommandHandler {

        private final ReviewRepository reviewRepository;

        public UpdateReviewCommandHandler(ReviewRepository reviewRepository) {
            this.reviewRepository = reviewRepository;
        }

        @Override
        public void handle(Command query) {
            InternalCommand internalCommand =
                    new UpdateReview(
                            ((UpdateReviewCommand) query).getReviewIdentifier(),
                            ((UpdateReviewCommand) query).getComment(),
                            reviewRepository
                    );
            internalCommand.execute();
        }
    }
}

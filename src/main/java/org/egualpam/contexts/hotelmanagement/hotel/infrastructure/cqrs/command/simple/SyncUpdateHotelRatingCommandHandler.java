package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.command.simple;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.hotel.application.command.UpdateHotelRating;
import org.egualpam.contexts.hotelmanagement.hotel.application.command.UpdateHotelRatingCommand;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.CommandHandler;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
public class SyncUpdateHotelRatingCommandHandler implements CommandHandler {

  private final TransactionTemplate transactionTemplate;
  private final UpdateHotelRating updateHotelRating;

  private static UpdateHotelRatingCommand toApplicationCommand(SyncUpdateHotelRatingCommand cmd) {
    return new UpdateHotelRatingCommand(cmd.hotelId(), cmd.reviewId(), cmd.rating());
  }

  @Override
  public void handle(Command command) {
    transactionTemplate.executeWithoutResult(
        ts ->
            Optional.of(command)
                .filter(SyncUpdateHotelRatingCommand.class::isInstance)
                .map(SyncUpdateHotelRatingCommand.class::cast)
                .map(SyncUpdateHotelRatingCommandHandler::toApplicationCommand)
                .ifPresent(updateHotelRating::execute));
  }
}

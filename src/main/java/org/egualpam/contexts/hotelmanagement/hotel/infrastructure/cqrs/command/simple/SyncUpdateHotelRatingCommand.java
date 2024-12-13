package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.command.simple;

import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;

public record SyncUpdateHotelRatingCommand(String hotelId, Integer rating) implements Command {}

package org.egualpam.contexts.hotelmanagement.hotel.application.query;

import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;

public record FindHotelQuery(String hotelId) implements Query {}

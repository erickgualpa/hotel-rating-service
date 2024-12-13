package org.egualpam.contexts.hotelmanagement.review.domain;

// TODO: Move this to shared domain
public record Rating(Integer value) {
  public Rating {
    if (value < 1 || value > 5) {
      throw new InvalidRating();
    }
  }
}

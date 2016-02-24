package com.prasanna.vwapsimulator.domain;

public class Venue {
    private String marketVenue;

    public Venue(String marketVenue) {
        this.marketVenue = marketVenue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue venue = (Venue) o;
        return !(marketVenue != null ? !marketVenue.equals(venue.marketVenue) : venue.marketVenue != null);
    }

    @Override
    public int hashCode() {
        return marketVenue != null ? marketVenue.hashCode() : 0;
    }

    public static Venue from(String venue) {
        return new Venue(venue);
    }
}

package com.prasanna.vwapsimulator.domain;


import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable class to represent tick
 */
public class Tick implements Comparable {


    private Instrument instrument;
    private Venue venue;
    private TickDirection tickDirection;
    private Long size;
    private BigDecimal price;
    private LocalDateTime timeStamp;

    private Tick() {

    }

    public Tick(Instrument instrument, Venue venue, TickDirection tickDirection, Long size, BigDecimal price) {
        this.instrument = instrument;
        this.venue = venue;
        this.tickDirection = tickDirection;
        this.size = size;
        this.price = price;
        this.timeStamp = LocalDateTime.now();
    }

    public static final Tick tickFor(Instrument instrument, TickDirection tickDirection, Venue venue, Long size, BigDecimal price) {
        return new Tick(instrument, venue, tickDirection, size, price);
    }

    public static final Tick buyTickFor(Instrument instrument, Venue venue, Long size, BigDecimal price) {
        return tickFor(instrument, TickDirection.BUY, venue, size, price);
    }

    public static final Tick sellTickFor(Instrument instrument, Venue venue, Long size, BigDecimal price) {
        return tickFor(instrument, TickDirection.SELL, venue, size, price);
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public Venue getVenue() {
        return venue;
    }

    public TickDirection getTickDirection() {
        return tickDirection;
    }

    public Long getSize() {
        return size;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tick tick = (Tick) o;

        if (instrument != null ? !instrument.equals(tick.instrument) : tick.instrument != null) return false;
        if (venue != null ? !venue.equals(tick.venue) : tick.venue != null) return false;
        if (tickDirection != tick.tickDirection) return false;
        if (size != null ? !size.equals(tick.size) : tick.size != null) return false;
        return !(price != null ? !price.equals(tick.price) : tick.price != null);

    }

    @Override
    public int hashCode() {
        int result = instrument != null ? instrument.hashCode() : 0;
        result = 31 * result + (venue != null ? venue.hashCode() : 0);
        result = 31 * result + (tickDirection != null ? tickDirection.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tick{" +
                "instrument=" + instrument +
                ", venue=" + venue +
                ", tickDirection=" + tickDirection +
                ", size=" + size +
                ", price=" + price +
                ", timeStamp=" + timeStamp +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Tick tick = (Tick) o;
        return price.compareTo(tick.getPrice());
    }
}

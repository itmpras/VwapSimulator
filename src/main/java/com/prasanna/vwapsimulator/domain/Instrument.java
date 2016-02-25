package com.prasanna.vwapsimulator.domain;


/**
 * Immutable class to represent Instrument
 */
public class Instrument {
    final String symbol;

    public Instrument(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Instrument that = (Instrument) o;

        return !(symbol != null ? !symbol.equals(that.symbol) : that.symbol != null);

    }

    @Override
    public int hashCode() {
        return symbol != null ? symbol.hashCode() : 0;
    }

    public static Instrument from(String instrument) {
        return new Instrument(instrument);
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "symbol='" + symbol + '\'' +
                '}';
    }
}

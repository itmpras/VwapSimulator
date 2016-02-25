package com.prasanna.vwapsimulator.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * VWAP = Sum(number of shares * share price ) / total Shares bought
 */
public class RunningVwap {

    private BigDecimal orderValue;
    private BigDecimal totalShares;
    private BigDecimal vwap;

    // TODO to handle thread failures
    public RunningVwap(BigDecimal orderValue, BigDecimal totalShares) {
        this.orderValue = orderValue;
        this.orderValue.setScale(2, RoundingMode.HALF_UP);
        this.totalShares = totalShares;
        this.totalShares.setScale(4, RoundingMode.HALF_UP);
        if (totalShares.equals(BigDecimal.ZERO)) {
            this.vwap = BigDecimal.ZERO;
        } else {
            this.vwap = this.orderValue.divide(this.totalShares, 4, RoundingMode.HALF_UP);
        }

    }

    public static RunningVwap initialVWAPValue() {
        return new RunningVwap(BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public RunningVwap apply(Tick tick) {

        BigDecimal price = tick.getPrice();
        BigDecimal size = new BigDecimal(tick.getSize());

        BigDecimal newOrderValue = orderValue.add(price.multiply(size));
        BigDecimal newTotalSize = totalShares.add(size);
        return new RunningVwap(newOrderValue.setScale(4, RoundingMode.HALF_UP), newTotalSize.setScale(4, RoundingMode.HALF_UP));
    }

    @Override
    public String toString() {
        return "RunningVwap{" +
                "orderValue=" + orderValue +
                ", totalShares=" + totalShares +
                ", vwap=" + vwap +
                '}';
    }
}

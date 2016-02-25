package com.prasanna.vwapsimulator.orderbook.comparator;

import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.domain.TickDirection;

import java.util.Comparator;

/**
 *  Base class having boiler plate code
 */
public abstract class BaseTickComparator  implements Comparator<Tick> {
    protected void validate(Tick o1, Tick o2, TickDirection directionToCompare) {
        if (o1 == null || o2 == null) {
            BuySideTickComparator.logger.error("Illegal Argument {},{}", o1, o2);
            throw new IllegalArgumentException("Expecting valid ticks");
        }

        if (o1.getTickDirection() != o2.getTickDirection()) {
            BuySideTickComparator.logger.error("Illegal Argument {},{}", o1, o2);
            throw new IllegalArgumentException("Expecting ticks in same direction");
        }


        if (!isTick(o1.getTickDirection(), directionToCompare) ||
                !isTick(o2.getTickDirection(), directionToCompare)) {
            BuySideTickComparator.logger.error("Illegal Argument {},{}", o1, o2);
            throw new IllegalArgumentException("Expecting Buy ticks");
        }
    }

    private boolean isTick(TickDirection tickDirection, TickDirection directionToCompare) {
        return tickDirection == directionToCompare;
    }
}

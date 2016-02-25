package com.prasanna.vwapsimulator.orderbook.comparator;

import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.domain.TickDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by gopalp on 24/02/2016.
 */
public class SellSideTickComparator extends BaseTickComparator {
    public final static Logger logger = LoggerFactory.getLogger(SellSideTickComparator.class);

    @Override
    public int compare(Tick o1, Tick o2) {

        validate(o1, o2, TickDirection.SELL);

        BigDecimal price1 = o1.getPrice();
        BigDecimal price2 = o2.getPrice();

        return price1.compareTo(price2);
    }

}

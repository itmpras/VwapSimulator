package com.prasanna.vwapsimulator.task;

import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.orderbook.OrderBookMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by prasniths on 24/02/16.
 */
public class OrderBookMapUpdateTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBookMapUpdateTask.class);

    private OrderBookMap orderBookMap;
    private Tick tick;

    public OrderBookMapUpdateTask(OrderBookMap orderBookMap, Tick tick) {
        this.orderBookMap = orderBookMap;
        this.tick = tick;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("OrderBookMapUpdateTask");
        LOGGER.info("Updating tick {}", tick);
        orderBookMap.update(tick);
    }
}

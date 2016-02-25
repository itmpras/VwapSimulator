package com.prasanna.vwapsimulator.task;

import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.orderbook.OrderBookMap;
import com.prasanna.vwapsimulator.util.WorkerThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OrderBookMapUpdateTask
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
        WorkerThreadUtil.enrichWorkerThread("OrderBookMapUpdateTask");
        LOGGER.info("Updating tick {}", tick);
        orderBookMap.update(tick);
    }


}

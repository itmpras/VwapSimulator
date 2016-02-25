package com.prasanna.vwapsimulator.domain;


import com.prasanna.vwapsimulator.orderbook.InstrumentOrderBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum TickDirection {

    BUY {
        @Override
        public void updateOrderBook(InstrumentOrderBook orderBook, Tick tick) {
            RunningVwap apply = orderBook.getBuyorderVWAP().apply(tick);
            orderBook.setBuyorderVWAP(apply);
            orderBook.getBuyOrderBook().updateOrderBook(tick);
        }
    },
    SELL {
        @Override
        public void updateOrderBook(InstrumentOrderBook orderBook, Tick tick) {
            RunningVwap apply = orderBook.getSellOrderVWAP().apply(tick);
            orderBook.setSellOrderVWAP(apply);
            orderBook.getSellOrderBook().updateOrderBook(tick);
        }
    };

    public void updateOrderBook(InstrumentOrderBook orderBook, Tick tick) {
        // TODO empty implentation
    }
}

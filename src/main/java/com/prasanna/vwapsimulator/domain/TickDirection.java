package com.prasanna.vwapsimulator.domain;


import com.prasanna.vwapsimulator.orderbook.InstrumentOrderBook;

public enum TickDirection {

    BUY {
        @Override
        public void updateOrderBook(InstrumentOrderBook orderBook, Tick tick) {
            Vwap apply = orderBook.getBuyorderVWAP().apply(tick);
            orderBook.setBuyorderVWAP(apply);
            orderBook.getBuyOrderBook().updateOrderBook(tick);
        }
    },
    SELL {
        @Override
        public void updateOrderBook(InstrumentOrderBook orderBook, Tick tick) {
            Vwap apply = orderBook.getSellOrderVWAP().apply(tick);
            orderBook.setSellOrderVWAP(apply);
            orderBook.getSellOrderBook().updateOrderBook(tick);
        }
    };

    public void updateOrderBook(InstrumentOrderBook orderBook, Tick tick) {
        throw new UnsupportedOperationException("Invalid operation");
    }
}

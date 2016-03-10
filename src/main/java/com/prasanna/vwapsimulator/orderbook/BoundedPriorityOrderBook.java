package com.prasanna.vwapsimulator.orderbook;

import com.prasanna.vwapsimulator.domain.Tick;

import java.util.*;
import java.util.function.Consumer;

/**
 * BoundedPriorityOrderBook
 */
public class BoundedPriorityOrderBook implements OrderBook {
    private Comparator<Tick> comparator;
    private PriorityQueue<Tick> priorityQueue;
    private int orderBookDepth;

    public BoundedPriorityOrderBook(Comparator<Tick> comparator, int orderBookDepth) {
        this.comparator = comparator;
        this.orderBookDepth = orderBookDepth;
        this.priorityQueue = new PriorityQueue<>(orderBookDepth);
    }

    @Override
    public boolean updateOrderBook(Tick tick) {

        if (priorityQueue.size() == orderBookDepth) {
            Tick[] ticksSortedArray = getTicksSortedArray();
            priorityQueue.remove(ticksSortedArray[orderBookDepth - 1]);
        }
        return priorityQueue.add(tick);
    }

    @Override
    public Iterator<Tick> getTicks() {
        Tick[] ticks = getTicksSortedArray();
        return new InternalIterator(ticks);
    }

    private Tick[] getTicksSortedArray() {
        Tick[] ticks = priorityQueue.toArray(new Tick[0]);
        Arrays.<Tick>sort(ticks, comparator);
        return ticks;
    }

    private class InternalIterator implements Iterator<Tick> {

        private Tick[] ticks;
        int index = 0;

        public InternalIterator(Tick[] ticks) {
            this.ticks = ticks;
        }

        @Override
        public boolean hasNext() {
            return index < ticks.length - 1;
        }

        @Override
        public Tick next() {
            int length = ticks.length;
            if (index < length - 1) {
                return ticks[index++];

            } else {
                return ticks[length - 1];
            }

        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("UnSupported operation");
        }

        @Override
        public void forEachRemaining(Consumer<? super Tick> action) {
            throw new UnsupportedOperationException("UnSupported operation");
        }
    }

    @Override
    public String toString() {
        return "BoundedPriorityOrderBook{" +
                "priorityQueue=" + priorityQueue +
                '}';
    }
}

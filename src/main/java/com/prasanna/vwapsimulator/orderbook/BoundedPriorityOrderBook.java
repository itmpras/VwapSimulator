package com.prasanna.vwapsimulator.orderbook;

import com.prasanna.vwapsimulator.domain.Tick;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.function.Consumer;

/**
 * Created by prasniths on 24/02/16.
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
        return new InteranlIterator(ticks);
    }

    private Tick[] getTicksSortedArray() {
        Tick[] ticks = priorityQueue.toArray(new Tick[0]);
        Arrays.<Tick>sort(ticks, comparator);
        return ticks;
    }

    private class InteranlIterator implements Iterator<Tick> {

        private Tick[] ticks;
        int index = 0;

        public InteranlIterator(Tick[] ticks) {
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
}

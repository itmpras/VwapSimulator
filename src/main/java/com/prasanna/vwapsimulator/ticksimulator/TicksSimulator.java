package com.prasanna.vwapsimulator.ticksimulator;

import com.prasanna.vwapsimulator.Parser.Parser;
import com.prasanna.vwapsimulator.domain.Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;


public class TicksSimulator {
    private final Parser parser;
    private final Reader reader;
    private static final Logger LOGGER = LoggerFactory.getLogger(TicksSimulator.class);
    private final BlockingQueue<Tick> queue;


    public TicksSimulator(Reader reader, Parser parser, BlockingQueue<Tick> destinationQueue) {
        this.parser = parser;
        this.reader = reader;
        this.queue = destinationQueue;
    }


    public List<Tick> generateTicksFrom(Reader tickJsonString) {
        return parser.parse(tickJsonString, Tick.class);
    }

    public void generateRandomTicks() throws InterruptedException {
        List<Tick> ticks = generateTicksFrom(reader);
        Random random = new Random();
        int randomNum = random.nextInt((ticks.size() - 2) + 1) + 0;
        Tick tick = ticks.get(randomNum);
        LOGGER.info("Adding tick {} to destinationQueue {}", tick);
        queue.put(tick);
    }
}

package com.prasanna.vwapsimulator;

import com.prasanna.vwapsimulator.Parser.GsonParser;
import com.prasanna.vwapsimulator.Parser.Parser;
import com.prasanna.vwapsimulator.domain.Instrument;
import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.orderbook.ConcurrentInstrumentOrderBookMap;
import com.prasanna.vwapsimulator.orderbook.InstrumentOrderBookRepository;
import com.prasanna.vwapsimulator.ticksimulator.TicksSimulator;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

import static org.slf4j.LoggerFactory.getLogger;

public class ApplicationRunner {

    private static final Logger LOGGER = getLogger(ApplicationRunner.class);

    public static void main(String[] args) throws InterruptedException {
        LOGGER.info("Application Runner");
        Parser parser = new GsonParser();
        LinkedBlockingQueue<Tick> tickQueue = new LinkedBlockingQueue<>(50);


        startTicksSimulator(Executors.newScheduledThreadPool(2), tickQueue, parser.parse(getReader("Ticks.json"), Tick.class), 1000L);
        startTicksSimulator(Executors.newScheduledThreadPool(2), tickQueue, parser.parse(getReader("Venue1.json"), Tick.class), 500L);
        startTicksSimulator(Executors.newScheduledThreadPool(2), tickQueue, parser.parse(getReader("Venue2.json"), Tick.class), 1200L);

        ConcurrentInstrumentOrderBookMap concurrentInstrumentOrderBookMap = new ConcurrentInstrumentOrderBookMap();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        InstrumentOrderBookRepository instrumentOrderBookRepository = new InstrumentOrderBookRepository(tickQueue, concurrentInstrumentOrderBookMap, executorService);
        instrumentOrderBookRepository.start();

        Thread.sleep(2000);
        concurrentInstrumentOrderBookMap.addInstrument(Instrument.from("LAD.L"));
        Thread.sleep(1000);
        concurrentInstrumentOrderBookMap.addInstrument(Instrument.from("LYOD.L"));
        Thread.sleep(1000);
        concurrentInstrumentOrderBookMap.addInstrument(Instrument.from("VOD.L"));
        Thread.sleep(1000);
        concurrentInstrumentOrderBookMap.addInstrument(Instrument.from("RBS.L"));
    }

    private static void startTicksSimulator(ScheduledExecutorService scheduledExecutorService, LinkedBlockingQueue<Tick> tickQueue, List<Tick> ticks, Long scheduleFrequencyInMills) throws InterruptedException {
        TicksSimulator ticksSimulator = new TicksSimulator(ticks, tickQueue, scheduledExecutorService, scheduleFrequencyInMills);
        ticksSimulator.generateRandomTicks();
    }

    private static Reader getReader(String name) {
        InputStream resourceAsStream = ApplicationRunner.class.getClassLoader().getResourceAsStream(name);
        return new BufferedReader(new InputStreamReader(resourceAsStream));
    }
}

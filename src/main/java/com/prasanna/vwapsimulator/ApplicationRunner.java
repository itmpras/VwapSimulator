package com.prasanna.vwapsimulator;

import ch.qos.logback.classic.LoggerContext;
import com.prasanna.vwapsimulator.Parser.GsonParser;
import com.prasanna.vwapsimulator.Parser.Parser;
import com.prasanna.vwapsimulator.domain.Instrument;
import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.orderbook.ConcurrentInstrumentOrderBookMap;
import com.prasanna.vwapsimulator.orderbook.InstrumentOrderBookRepository;
import com.prasanna.vwapsimulator.ticksimulator.TicksSimulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        // For sake of simplicity wiring different components here.

        LOGGER.info("Starting Tick simulator");
        Parser parser = new GsonParser();
        // Tick queue capacity
        int tickQueueCapacity = 100;
        // OrderBook depth
        int instrumentOrderBookDepth = 30;
        // Per instrument queue capacity
        int instrumentTickQueueDepth = 100;
        // Blocking Queue where ticks from various sources will get updated
        LinkedBlockingQueue<Tick> tickQueue = new LinkedBlockingQueue<>(tickQueueCapacity);

        // Stating tick simulator populating ticks to queue
        final TicksSimulator ticksSimulator = startTicksSimulator(Executors.newScheduledThreadPool(2), tickQueue, parser.parse(getReader("Ticks.json"), Tick.class), 1000L);
        final TicksSimulator ticksSimulatorVenue1 = startTicksSimulator(Executors.newScheduledThreadPool(2), tickQueue, parser.parse(getReader("Venue1.json"), Tick.class), 500L);
        final TicksSimulator ticksSimulatorVenue2 = startTicksSimulator(Executors.newScheduledThreadPool(2), tickQueue, parser.parse(getReader("Venue2.json"), Tick.class), 1200L);

        // Creating InstrumentOrderBookMap
        final ConcurrentInstrumentOrderBookMap concurrentInstrumentOrderBookMap = new ConcurrentInstrumentOrderBookMap(instrumentOrderBookDepth, instrumentTickQueueDepth);

        // Creating InstrumentOrder book repository
        int orderBookUpdateThreadCount = 5;
        final ExecutorService instrumentOrderBookRepoExecutor = Executors.newFixedThreadPool(orderBookUpdateThreadCount);
        final InstrumentOrderBookRepository instrumentOrderBookRepository = new InstrumentOrderBookRepository(tickQueue, concurrentInstrumentOrderBookMap, instrumentOrderBookRepoExecutor);
        instrumentOrderBookRepository.start();

        // Adding Instruments , for which we need tick updates
        Thread.sleep(2000);
        concurrentInstrumentOrderBookMap.addInstrument(Instrument.from("LAD.L"));
        Thread.sleep(1000);
        concurrentInstrumentOrderBookMap.addInstrument(Instrument.from("LYOD.L"));
        Thread.sleep(1000);
        concurrentInstrumentOrderBookMap.addInstrument(Instrument.from("VOD.L"));
        Thread.sleep(1000);
        concurrentInstrumentOrderBookMap.addInstrument(Instrument.from("RBS.L"));

        // Registering shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LOGGER.info("Shutting down Simulator");
                ticksSimulator.shutDown();
                ticksSimulatorVenue1.shutDown();
                ticksSimulatorVenue2.shutDown();

                LOGGER.info("Shutting down InstrumentOrderBooks");
                concurrentInstrumentOrderBookMap.shutDown();

                instrumentOrderBookRepoExecutor.shutdown();
                LOGGER.info("Shutting down InstrumentOrderBookRepository");
                instrumentOrderBookRepository.shutDown();

            }
        });
    }

    private static TicksSimulator startTicksSimulator(ScheduledExecutorService scheduledExecutorService, LinkedBlockingQueue<Tick> tickQueue, List<Tick> ticks, Long scheduleFrequencyInMills) throws InterruptedException {
        TicksSimulator ticksSimulator = new TicksSimulator(ticks, tickQueue, scheduledExecutorService, scheduleFrequencyInMills);
        ticksSimulator.generateRandomTicks();
        return ticksSimulator;
    }

    private static Reader getReader(String name) {
        InputStream resourceAsStream = ApplicationRunner.class.getClassLoader().getResourceAsStream(name);
        return new BufferedReader(new InputStreamReader(resourceAsStream));
    }


}

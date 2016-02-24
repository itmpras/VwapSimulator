package com.prasanna.vwapsimulator.ticksimulator;

import com.prasanna.vwapsimulator.Parser.Parser;
import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.task.RandomTickSourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TicksSimulator {
   private final Parser parser;
   private final Reader reader;
   private static final Logger LOGGER = LoggerFactory.getLogger(TicksSimulator.class);
   private final BlockingQueue<Tick> destinationQueue;
   private final ScheduledExecutorService scheduledExecutorService;
   private final Long scheduleFrequency;
   private List<Tick> ticks;

   public TicksSimulator(Reader reader, Parser parser, BlockingQueue<Tick> destinationQueue, ScheduledExecutorService
      scheduledExecutorService, Long scheduleFrequencyInMills) {
      this.parser = parser;
      this.reader = reader;
      this.destinationQueue = destinationQueue;
      this.scheduledExecutorService = scheduledExecutorService;
      this.scheduleFrequency = scheduleFrequencyInMills;
   }

   public List<Tick> generateTicksFrom(Reader tickJsonString) {
      return parser.parse(tickJsonString, Tick.class);
   }

   public void generateRandomTicks() throws InterruptedException {
      ticks = generateTicksFrom(reader);
      scheduledExecutorService.scheduleWithFixedDelay(new RandomTickSourceTask(ticks, destinationQueue), scheduleFrequency, scheduleFrequency, TimeUnit
         .MILLISECONDS);
   }
   public void shutDown() {
      scheduledExecutorService.shutdown();
   }
}

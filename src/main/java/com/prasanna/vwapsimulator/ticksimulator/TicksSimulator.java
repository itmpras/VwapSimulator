package com.prasanna.vwapsimulator.ticksimulator;

import com.prasanna.vwapsimulator.Parser.Parser;
import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.task.RandomTickSourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TicksSimulator {
   private static final Logger LOGGER = LoggerFactory.getLogger(TicksSimulator.class);
   private final BlockingQueue<Tick> destinationQueue;
   private final ScheduledExecutorService scheduledExecutorService;
   private final Long scheduleFrequency;
   private final List<Tick> ticks;

   public TicksSimulator(List<Tick> ticks, BlockingQueue<Tick> queue, ScheduledExecutorService scheduler, long scheduleFrequencyInMills) {
      this.ticks = Collections.unmodifiableList(ticks);
      this.destinationQueue = queue;
      this.scheduledExecutorService = scheduler;
      this.scheduleFrequency = scheduleFrequencyInMills;
   }

   public void generateRandomTicks() throws InterruptedException {
      scheduledExecutorService.scheduleWithFixedDelay(new RandomTickSourceTask(ticks, destinationQueue), scheduleFrequency, scheduleFrequency, TimeUnit
         .MILLISECONDS);
   }
   public void shutDown() {
      scheduledExecutorService.shutdown();
   }
}

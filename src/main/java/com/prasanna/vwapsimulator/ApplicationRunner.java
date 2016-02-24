package com.prasanna.vwapsimulator;

import com.prasanna.vwapsimulator.Parser.GsonParser;
import com.prasanna.vwapsimulator.Parser.Parser;
import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.ticksimulator.TicksSimulator;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

import static org.slf4j.LoggerFactory.getLogger;

public class ApplicationRunner {

   private static final Logger LOGGER = getLogger(ApplicationRunner.class);

   public static void main(String[] args) throws InterruptedException {
      LOGGER.info("Application Runner");
      Parser parser = new GsonParser();
      InputStream resourceAsStream = ApplicationRunner.class.getClassLoader().getResourceAsStream("Ticks.json");
      Reader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
      ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
      TicksSimulator ticksSimulator = new TicksSimulator(reader, parser, new LinkedBlockingQueue<Tick>(50), scheduledExecutorService, 1000L);
      ticksSimulator.generateRandomTicks();
   }
}

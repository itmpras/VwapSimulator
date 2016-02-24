package com.prasanna.vwapsimulator.ticksimulator;

import com.prasanna.vwapsimulator.Parser.GsonParser;
import com.prasanna.vwapsimulator.Parser.Parser;
import com.prasanna.vwapsimulator.domain.Tick;
import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.DeterministicScheduler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class TicksSimulatorTest {
   public static final long SCHEDULE_FREQUENCY_IN_MILLS = 1000L;
   private Mockery context;
   private TicksSimulator ticksSimulator;
   private BlockingQueue<Tick> queue;
   private DeterministicScheduler scheduler = new DeterministicScheduler();

   @Before
   public void setUp() throws Exception {
      context = new JUnit4Mockery();
      queue = context.mock(BlockingQueue.class);
      List<Tick> ticks = getTicks();

      ticksSimulator = new TicksSimulator(ticks,queue, scheduler, SCHEDULE_FREQUENCY_IN_MILLS);
   }

   @Test
   public void shouldGenerateRandomTicksInRegularInterval() throws Exception {

      ticksSimulator.generateRandomTicks();

      context.checking(new Expectations() {
         {
            oneOf(queue).put(with(any(Tick.class)));
         }
      });
      scheduler.tick(SCHEDULE_FREQUENCY_IN_MILLS, TimeUnit.MILLISECONDS);
      context.checking(new Expectations() {
         {
            oneOf(queue).put(with(any(Tick.class)));
         }
      });

      scheduler.tick(SCHEDULE_FREQUENCY_IN_MILLS, TimeUnit.MILLISECONDS);
      context.assertIsSatisfied();
   }

   @After
   public void tearDown() throws Exception {

   }

   private List<Tick> getTicks() {
      GsonParser parser = new GsonParser();
      InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("Ticks.json");
      return parser.parse(new BufferedReader(new InputStreamReader(resourceAsStream)), Tick.class);
   }


}
package com.prasanna.vwapsimulator.orderbook.comparator;

import com.prasanna.vwapsimulator.domain.Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

/**
 * Created by gopalp on 24/02/2016.
 */
public class BuySideTickCompartor implements Comparator<Tick> {
   public final static Logger logger = LoggerFactory.getLogger(BuySideTickCompartor.class);
   @Override
   public int compare(Tick o1, Tick o2) {

      if (o1 == null || o2 == null) {
         logger.error("Illegal Argument {},{}", o1, o2);
         throw new IllegalArgumentException("Expecting valid ticks");
      }

      if (o1.getTickDirection() != o2.getTickDirection()) {
         logger.error("Illegal Argument {},{}", o1, o2);
         throw new IllegalArgumentException("Expecting ticks in same direction");
      }
      return 0;
   }
}

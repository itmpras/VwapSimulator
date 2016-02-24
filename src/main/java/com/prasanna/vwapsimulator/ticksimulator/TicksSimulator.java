package com.prasanna.vwapsimulator.ticksimulator;

import com.prasanna.vwapsimulator.Parser.Parser;
import com.prasanna.vwapsimulator.domain.Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.util.List;


public class TicksSimulator {
    private final Parser parser;
    private static final Logger LOGGER = LoggerFactory.getLogger(TicksSimulator.class);


    public TicksSimulator(Parser parser) {
        this.parser = parser;
    }


    public List<Tick> generateTicksFrom(Reader tickJsonString) {
        return parser.parse(tickJsonString, Tick.class);
    }
}

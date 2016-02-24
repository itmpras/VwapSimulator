package com.prasanna.vwapsimulator.ticksimulator;

import com.google.gson.*;
import com.prasanna.vwapsimulator.Parser.Parser;
import com.prasanna.vwapsimulator.domain.Tick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasniths on 23/02/16.
 */
public class TicksSimulator {
    Parser parser;

    public TicksSimulator(Parser parser) {
        this.parser = parser;
    }

    public List<Tick> generateTicksFor(String tickJsonString) {
        return parser.parse(tickJsonString, Tick.class);
    }
}

package com.prasanna.vwapsimulator.Parser;

import com.prasanna.vwapsimulator.domain.Tick;

import java.util.List;

/**
 * Created by prasniths on 24/02/16.
 */
public interface Parser {
    public <T> List<T> parse(String input, Class<T> tClass);
}

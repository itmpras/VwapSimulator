package com.prasanna.vwapsimulator.Parser;

import com.prasanna.vwapsimulator.domain.Tick;

import java.io.Reader;
import java.util.List;

/**
 * Created by prasniths on 24/02/16.
 */
public interface Parser {
    public <T> List<T> parse(Reader input, Class<T> tClass);
}

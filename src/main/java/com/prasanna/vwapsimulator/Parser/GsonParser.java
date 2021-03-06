package com.prasanna.vwapsimulator.Parser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.prasanna.vwapsimulator.domain.Tick;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GSon
 */
public class GsonParser implements Parser {

   private Gson gson;
   private JsonParser jsonParser;

   public GsonParser() {
      gson = new Gson();
      jsonParser = new JsonParser();
   }

   @Override
   public <T> List<T> parse(Reader input, Class<T> targetClass) {

      JsonArray jArray = jsonParser.parse(input).getAsJsonArray();
      List<T> target = new ArrayList<>();
      for (JsonElement obj : jArray) {
         T tick = gson.fromJson(obj, targetClass);
         target.add(tick);
      }

      return Collections.unmodifiableList(target);
   }
}

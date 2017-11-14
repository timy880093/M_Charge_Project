/**
 * 
 */
package com.gate.utils;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
/**
 * @author mac
 *
 */
public class TimeStampDeserializer implements JsonDeserializer<java.sql.Timestamp> {

	@Override
	  public Timestamp deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
	      String date = element.getAsString();
	      try{
	          return new Timestamp(new Long(date));
	      } catch (Exception exp) {
	          System.err.println("Failed to parse Timestamp:"+exp);
	          return null;
	      }
	   }
}

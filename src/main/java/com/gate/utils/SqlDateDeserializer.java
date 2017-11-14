/**
 * 
 */
package com.gate.utils;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.ParseException;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author mac
 *
 */
public class SqlDateDeserializer implements JsonDeserializer<Date>, JsonSerializer<Date> {

	String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy-MM-dd HH:mm",
			"yyyy/MM/dd", "yyyy-MM-dd'T'HH:mm:ss.SSSZZ" };

	@Override
	public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		String dateString = element.getAsString();
		if (dateString == null || dateString.trim().equals("")) {
			return null;
		}
		try {
			return new Date(DateUtils.parseDate(dateString, parsePatterns).getTime());
		} catch (ParseException exp) {
			System.err.println("Failed to parse Date:" + exp);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object,
	 * java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {

		DateTime time = new DateTime(src);
		try {
			if (time.getHourOfDay() + time.getMinuteOfHour() + time.getSecondOfMinute() == 0) {
				return new JsonPrimitive(time.toString(parsePatterns[0]));
			} else {
				return new JsonPrimitive(time.toString(parsePatterns[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return src == null ? null : new JsonPrimitive(src.getTime());
	}
}

/**
 * 
 */
package com.gate.web;

import java.io.Serializable;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author mac
 *
 */
public class JqgridObjectMapper implements Serializable {

	public static JqgridFilter map(String jsonString) {

		if (jsonString != null) {
			ObjectMapper mapper = new ObjectMapper();

			try {
				return mapper.readValue(jsonString, JqgridFilter.class);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return null;
	}
}

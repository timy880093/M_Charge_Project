/**
 * 
 */
package com.gate.web;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author mac
 *
 */
/**
 * A POJO that represents a jQgrid JSON requests {@link String}<br/>
 * A sample filter follows the following format:
 * 
 * <pre>
 * {"groupOp":"AND","rules":[{"field":"firstName","op":"eq","data":"John"}]}
 * </pre>
 */
public class JqgridFilter implements Serializable {

	private String source;
	/**
	 * operators: [
        { op: "eq", text: "is equal to" },
        { op: "ne", text: "is not equal to" },
        { op: "lt", text: "is less than" },
        { op: "le", text: "is less or equal to" },
        { op: "gt", text: "is greater than" },
        { op: "ge", text: "is greater or equal to" },
        { op: "in", text: "is in" },
        { op: "ni", text: "is not in" },
        { op: "bw", text: "begins with" },
        { op: "bn", text: "does not begin with" },
        { op: "ew", text: "ends with" },
        { op: "en", text: "does not end with" },
        { op: "cn", text: "contains" },
        { op: "nc", text: "does not contain" }
    	],
	*/
	private String groupOp;
	private ArrayList<Rule> rules;

	public JqgridFilter() {
		super();
	}

	public JqgridFilter(String source) {
		super();
		this.source = source;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the groupOp
	 */
	public String getGroupOp() {
		return groupOp;
	}

	/**
	 * @param groupOp
	 *            the groupOp to set
	 */
	public void setGroupOp(String groupOp) {
		this.groupOp = groupOp;
	}

	/**
	 * @return the rules
	 */
	public ArrayList<Rule> getRules() {
		return rules;
	}

	/**
	 * @param rules
	 *            the rules to set
	 */
	public void setRules(ArrayList<Rule> rules) {
		this.rules = rules;
	}

	/**
	 * Inner class containing field rules
	 */
	public static class Rule {
		private String junction;
		private String field;
		private String op;
		private String data;

		public Rule() {
		}

		public Rule(String junction, String field, String op, String data) {
			super();
			this.junction = junction;
			this.field = field;
			this.op = op;
			this.data = data;
		}

		/**
		 * @return the junction
		 */
		public String getJunction() {
			return junction;
		}

		/**
		 * @param junction
		 *            the junction to set
		 */
		public void setJunction(String junction) {
			this.junction = junction;
		}

		/**
		 * @return the field
		 */
		public String getField() {
			return field;
		}

		/**
		 * @param field
		 *            the field to set
		 */
		public void setField(String field) {
			this.field = field;
		}

		/**
		 * @return the op
		 */
		public String getOp() {
			return op;
		}

		/**
		 * @param op
		 *            the op to set
		 */
		public void setOp(String op) {
			this.op = op;
		}

		/**
		 * @return the data
		 */
		public String getData() {
			return data;
		}

		/**
		 * @param data
		 *            the data to set
		 */
		public void setData(String data) {
			this.data = data;
		}

	}

}

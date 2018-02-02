/**
 * 
 */
package com.gate.web;

import java.io.Serializable;
import java.util.List;

/**
 * @author mac
 *
 */
public class JqgridResponse<T extends Serializable> {

	/**
	 * Current page
	 */
	private String page;

	/**
	 * Total pages
	 */
	private String total;

	/**
	 * Total number of records
	 */
	private String records;

	/**
	 * Contains the actual data
	 */
	private List<T> rows;

	public JqgridResponse() {
	}

	public JqgridResponse(String page, String total, String records, List<T> rows) {
		super();
		this.page = page;
		this.total = total;
		this.records = records;
		this.rows = rows;
	}

	/**
	 * @return the page
	 */
	public String getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(String page) {
		this.page = page;
	}

	/**
	 * @return the total
	 */
	public String getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(String total) {
		this.total = total;
	}

	/**
	 * @return the records
	 */
	public String getRecords() {
		return records;
	}

	/**
	 * @param records the records to set
	 */
	public void setRecords(String records) {
		this.records = records;
	}

	/**
	 * @return the rows
	 */
	public List<T> getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "JqgridResponse [page=" + page + ", total=" + total + ", records=" + records + "]";
	}
}

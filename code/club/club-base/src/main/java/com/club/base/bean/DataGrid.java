package com.club.base.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * DataGrid的包装类描述
 * @model 表格返回包装
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
public class DataGrid<T> {

	/**
	 * 记录行
	 */
	private List<T> rows;

	/**
	 * 总数
	 */
	private long total;

	public DataGrid() {
		this.rows = new ArrayList<>();
		this.total = 0;
	}

	public DataGrid(List<T> list, long total) {
		this.rows = list;
		this.total = total;
		if (list == null) {
			this.rows = new ArrayList<T>();
		}
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}

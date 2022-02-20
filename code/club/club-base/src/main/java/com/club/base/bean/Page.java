package com.club.base.bean;


 /**
  * LargePage的包装类描述
  * @model 不限制记录数，分页参数
  * @author 阳春白雪 | sample@gmail.com
  * @since 1.0
  * @date 2022年2月20日
  */
public class Page {
	public static final String ORDER_DESC = "desc";
	public static final String ORDER_ASC = "asc";
	public static final String ORDER_SPLIT = ",";

	 /**
	  * 当前页
	  */
	private int page = 1;

	 /**
	  * 当前页，条数
	  */
	private int rows = 10;
	 /**
	  * 当前页
	  */
	private String sort;
	 /**
	  * 排序
	  */
	private String order;
	 /**
	  * 是否查询记录数
	  */
	private boolean count = true;

	 /**
	  * 是否滚动加载
	  */
	private boolean roll = false;
	 /**
	  * 是否启用分页
	  */
	private boolean enable = true;

	 /**
	  * 是否启用分页
	  */
	 private boolean offset = false;

	public Page() {
	}

	public Page(int page, int rows) {
		this.page = page;
		this.rows = rows;
	}

	 public int getPage() {
		 return page;
	 }

	 public int getRows() {
		 return rows;
	 }

	 public boolean isRoll() {
		 return roll;
	 }

	 public void setRoll(boolean roll) {
		 this.roll = roll;
	 }

	 public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public boolean isCount() {
		return count;
	}

	public void setCount(boolean count) {
		this.count = count;
	}

	public int getPageNum() {
		return page;
	}

	public void setPageNum(int pageNum) {
		setPage(pageNum);
	}

	public int getPageSize() {
		return rows;
	}

	public void setPageSize(int pageSize) {
		setRows(pageSize);
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getStart() {
		return getPageSize() * (getPageNum() - 1);
	}

	public int getEnd() {
		return getPageSize() * getPageNum();
	}

	 public boolean isEnable() {
		 return enable;
	 }

	 public void setEnable(boolean enable) {
		 this.enable = enable;
	 }

	 public boolean isOffset() {
		 return offset;
	 }

	 public void setOffset(boolean offset) {
		 this.offset = offset;
	 }
 }

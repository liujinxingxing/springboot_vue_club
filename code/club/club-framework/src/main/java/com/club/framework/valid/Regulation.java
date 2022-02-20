package com.club.framework.valid;


/**
 * 校验规则 常量池
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public interface Regulation {

	/**
	 * TEXT_INPUT_LEN 文本框长度
	 */
	int TEXT_INPUT_LEN = 30;
	
	/**
	 * TEXT_INPUT_LEN 大文本框长度
	 */
	int TEXT_INPUT_BIGLEN = 100;

	/**
	 * EMAIL_LEN 邮箱长度
	 */
	int EMAIL_LEN = 300;

	/**
	 * TEXT_AREA_LEN 小文本域长度
	 */
	int TEXT_AREA_LEN = 300;
	
	/**
	 * TEXT_AREA_LEN 中文本域长度
	 */
	int TEXT_AREA_MIDDLE_LEN = 1000;
	
	/**
	 * URL_LEN 地址长度
	 */
	int URL_LEN = 1000;
	
	
	/**
	 * TEXT_LONG_AREA_LEN 大文本域长度
	 */
	int TEXT_LONG_AREA_LEN = 4000;

	/**
	 * NUM_REGEXP 数字字符串数字正则
	 */
	String NUM_REGEXP = "[0-9]*";

	/**
	 * CHAR_NUM_REGEXP 必须字母开头仅包含字母数字下划线
	 */
	String CHAR_NUM_REGEXP = "^[a-z-A-Z](_*-*([a-zA-Z0-9]*))*$";

	/**
	 * SPECIAL_CHARACTERS_REGEXP 位字母数字下划线中文
	 */
	String SPECIAL_CHARACTERS_REGEXP = "^(_*-*([a-zA-Z0-9\u4E00-\u9FA5]*))*$";
}

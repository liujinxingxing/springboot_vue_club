package com.club.framework.valid.util;

import com.club.base.constant.ErrorCode;
import com.club.base.exception.FrameworkException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;


/**
 * 检验参数工具类
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public class ValidateUtils {


	private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	/**
	 *  检验
	 *
	 * @param object - 参数
	 * @param groups - 分组
	 * @return void
	 * @author 阳春白雪 | sample@gmail.com
	 * @date 2022年2月20日
	 * @since 1.0
	 */
	public static <T> void validate(T object, Class<?>... groups) {
		Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
		if (CollectionUtils.isNotEmpty(violations)) {
			StringBuilder errorMessage = new StringBuilder("请求参数错误：");
			for (ConstraintViolation<T> violation : violations) {
				errorMessage.append("{ ");
				errorMessage.append(violation.getPropertyPath().toString()).append(" : ");
				errorMessage.append(", 错误值为【").append(violation.getMessage());
				errorMessage.append("】 };");
			}
			throw new FrameworkException(ErrorCode.PARAM_ERROR.getCode(), StringUtils.stripEnd(errorMessage.toString(), ";"));
		}
	}
}

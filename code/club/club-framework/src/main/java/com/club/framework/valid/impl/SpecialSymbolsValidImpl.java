package com.club.framework.valid.impl;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.club.framework.valid.SpecialSymbolsValid;
import org.apache.commons.lang.StringUtils;

/**
 * 特殊字符校验实现类
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public class SpecialSymbolsValidImpl implements ConstraintValidator<SpecialSymbolsValid, String>{

	private static final String SYMBOL = "[~!@#$%^&*+./:;,.<>?' `=|·！￥……——？》《，。‘“；：\\{\\}]";
	
	@Override
	public void initialize(SpecialSymbolsValid specialSymbolsValid) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isEmpty(value)) {
            return true;
        }
		return !Pattern.compile(SYMBOL).matcher(value).find();
	}

}

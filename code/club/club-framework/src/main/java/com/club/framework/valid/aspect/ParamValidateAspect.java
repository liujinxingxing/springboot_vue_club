package com.club.framework.valid.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.club.base.constant.ErrorCode;
import com.club.base.exception.FrameworkException;
import com.club.framework.valid.annotation.ParamValid;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.club.framework.valid.vo.FieldErrorVo;

 /**
  * 参数校验切面
  * @author 阳春白雪 | sample@gmail.com
  * @since 1.0
  * @ignore
  * @date 2022年2月20日
  */
@Aspect
@Component
public class ParamValidateAspect {

	 /**
	  * 切点
	  * @author 阳春白雪 | sample@gmail.com
	  * @date 2022年2月20日
	  * @since 1.0
	  */
	 @Pointcut("execution(* com..*.controller..*.*(..))")
	 public void controllerAspect() {
	 }
	
	 /**
	  * 异常切面
	  * @author 阳春白雪 | sample@gmail.com
	  * @date 2022年2月20日
	  * @since 1.0
	  */
	@Before("controllerAspect()")
	public void doParamValidateForController(JoinPoint joinPoint) throws Exception {
		Object[] args = joinPoint.getArgs();
		if (args == null || args.length == 0) {
			return;
		}
		Signature signature = joinPoint.getSignature();
		String methodName = signature.getName();
		Method[] methods = signature.getDeclaringType().getMethods();
		Method targetMethod = null;
		for (Method method : methods) {
			if (methodName.equals(method.getName())) {
				targetMethod = method;
				break;
			}
		}
		List<FieldErrorVo> modelFieldErrors = new ArrayList<FieldErrorVo>();
		MethodSignature methodSignature = (MethodSignature) signature;
		String[] parameterNames = methodSignature.getParameterNames();
		Annotation[][] argsAnnotations = targetMethod.getParameterAnnotations();
		int i = 0;
		for (Annotation[] annotation : argsAnnotations) {
			ParamValid paramValid = getParamValid(annotation, ParamValid.class);
			if (paramValid != null) {
				Object arg = args[i];
				if (arg instanceof String) {
					String param = (String) arg;
					if (!param.matches(paramValid.regex())) {
						modelFieldErrors.add(new FieldErrorVo(parameterNames[i], paramValid.regex(), param));
					}
				}
			}
			++i;
		}
		if (modelFieldErrors.size() > 0) {
			throw new FrameworkException(ErrorCode.REQ_PARAM_ERROR.getCode(), JSON.toJSONString(modelFieldErrors));
		}
	}

	public ParamValid getParamValid(Annotation[] annotations, Class<?> clz) {
		for (Annotation annotation : annotations) {
			Class<? extends Annotation> annotationType = annotation.annotationType();
			if (annotationType.getName().equals(clz.getName()) && annotation instanceof ParamValid) {
				return (ParamValid) annotation;
			}
		}
		return null;
	}

}
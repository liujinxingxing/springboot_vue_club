package com.club.framework.spring.execute;

import com.club.base.constant.ErrorCode;
import com.club.base.exception.FrameworkException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 执行器
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
public class Invoker {

    public static final int FIRSTLY = 50;

    public static final int SECOND = 40;

    public static final int MIDDLE = 30;

    public static final int LAST_SECOND = 20;

    public static final int LASTLY = 10;

    /**
     * 实例的方法（目标对象的方法）
     */
    private Method method;

    /**
     * 实例（目标对象）
     */
    private Object target;

    /**
     * 位置
     */
    private int priorityLevel = MIDDLE;

    /**
     * 功能描述: 生成一个执行器，这个可以快速生成执行器的实例（一般程序会有多个执行器
     * ，相当于web中每一次请求的执行器）
     *
     * @param method 执行器将执行的方法
     * @param target 执行器执行方法的实例（也就是方法的实例对象）
     * @return Invoker - 返回值类型
     * @author Daye.Shing
     * @date 2018年9月14日下午10:20:01
     */
    public static Invoker valueOf(Method method, Object target) {
        Invoker invoker = new Invoker();
        invoker.setMethod(method);
        invoker.setTarget(target);
        return invoker;
    }

    /**
     * 功能描述: 生成一个执行器，这个可以快速生成执行器的实例（一般程序会有多个执行器
     * ，相当于web中每一次请求的执行器）
     *
     * @param method 执行器将执行的方法
     * @param target 执行器执行方法的实例（也就是方法的实例对象）
     * @return Invoker - 返回值类型
     * @author Daye.Shing
     * @date 2018年9月14日下午10:20:01
     */
    public static Invoker valueOf(Method method, Object target,int priorityLevel) {
        Invoker invoker = new Invoker();
        invoker.setMethod(method);
        invoker.setTarget(target);
        invoker.setPriorityLevel(priorityLevel);
        return invoker;
    }

    /**
     * 功能描述: 执行器执行启动
     *
     * @param paramValues 目标参数
     * @return Object - 返回值类型     执行结果，结果是多样话的，所以用一个obj
     * @author Daye.Shing
     * @date 2018年9月14日下午10:24:24
     */
    public Object invoke(Object... paramValues) throws FrameworkException {
        try {
            return method.invoke(target, paramValues);
        } catch (IllegalAccessException e) {
            throw new FrameworkException(ErrorCode.METHOD_NO_PERMISSION);
        } catch (IllegalArgumentException e) {
            throw new FrameworkException(ErrorCode.METHOD_TARGET_ARGUMENT_ERROR);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            if (cause instanceof FrameworkException) {
                throw (FrameworkException) cause;
            }
            throw new FrameworkException(ErrorCode.SERVER_ERROR);
        }
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invoker)) return false;
        Invoker invoker = (Invoker) o;
        return getPriorityLevel() == invoker.getPriorityLevel() &&
                Objects.equals(getMethod(), invoker.getMethod()) &&
                Objects.equals(getTarget(), invoker.getTarget());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getMethod(), getTarget(), getPriorityLevel());
    }
}

package com.club.framework.spring.execute;

import com.club.base.exception.FrameworkException;
import com.club.base.constant.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 执行器helper
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
@Slf4j
public class InvokerHelper {

    /**
     * 属性名称：invokers
     * 属性描述：将执行器加载到内存中，不用老实新建实例，
     * 浪费性能（这仅仅是一个双层的执行器管理器），
     * 当时已经非常够用了，因为现在的web大部分也是双层
     * @author Daye.Shing
     * @date 2018年9月14日下午10:28:03
     */
    private static Map<String, ConcurrentHashMap<String, Invoker>> invokers = new ConcurrentHashMap<String, ConcurrentHashMap<String, Invoker>>();

    private static final Map<String,Invoker> EXECUTES = new ConcurrentHashMap<String,Invoker>();

    private static Random RANDOM = new Random();

    /**
     * 功能描述: 将执行器加载到内存
     * @param module 模块            （模块可以理解为一个业务的大分类）
     * @param businiss 业务      （这个也可以理解为一个业务的小分类）
     * @param invoker 执行器
     * void - 返回值类型
     * @author  Daye.Shing
     * @date 2018年9月14日下午10:31:08
     */
    public static void addInvoker(String module, String businiss, Invoker invoker){
        //回去业务大分类
        ConcurrentHashMap<String, Invoker> map = invokers.get(module);
        //如果没有将创建这个分类
        if(map == null){
            // 新建模块  容器集合
            map = new ConcurrentHashMap<String, Invoker>();
            // 将模块容器放到顶层集合中
            invokers.put(module, map);
        }
        // 将执行器放入集合，（也就是加载到内存）
        map.put(businiss, invoker);
    }

    /**
     * 功能描述: 获取执行器（从内存获取实例和实例的方法）
     * @param module         存放的模块
     * @param businiss       存放的业务
     * @return Invoker - 返回值类型    存在将返回一个带有实例和实例方法的执行器，没有将返回一个null
     * @author  Daye.Shing
     * @date 2018年9月14日下午10:36:15
     */
    public static Invoker getInvoker(String module, String businiss)throws FrameworkException {
        Map<String, Invoker> map = invokers.get(module);
        if(map != null){
            return map.get(businiss);
        }
        throw new FrameworkException(ErrorCode.INVOKE_IS_NULL);
    }

    public static void addExecute(Invoker invoker){
        EXECUTES.put(Integer.toString(getKey(invoker.getPriorityLevel() * 100000)) ,invoker);
    }

    public static List<Invoker> getExecutes(){
        List<Invoker> ret = new ArrayList<>();
        Set<String> keys = EXECUTES.keySet();
        for (String key : keys) {
            ret.add(EXECUTES.get(key));
        }
        EXECUTES.clear();
        return ret;
    }

    private static int getKey(int priorityLevel){
        if(EXECUTES.containsKey(Integer.toString(priorityLevel))){
            return getKey(priorityLevel - RANDOM.nextInt(500));
        }
        return priorityLevel;
    }

}

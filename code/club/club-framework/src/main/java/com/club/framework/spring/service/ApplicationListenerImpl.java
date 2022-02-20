package com.club.framework.spring.service;

import com.club.framework.spring.execute.Invoker;
import com.club.framework.spring.execute.InvokerHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 应用扫描执行器
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
@Slf4j
@Component
public class ApplicationListenerImpl implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            List<Invoker> executes = InvokerHelper.getExecutes();
            if(CollectionUtils.isNotEmpty(executes)){
                log.debug("有{}个加载任务执行", executes.size());
                for (Invoker execute : executes) {
                    try {
                        execute.invoke();
                    }catch (Exception e){
                    }
                }
            }
        }
    }

}

package cn.spring.logger.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

/**
 * Created by fic on 2019.03.30 10:13
 * Package:spring0330.cn.spring.logger.aop
 * Desc:代理类
 */
public class UserServiceLogger {
    public static final Logger LOGGER = Logger.getLogger(UserServiceLogger.class);

    /**
     * 前置增强方法
     * JoinPoint jp 连接点
     */
    public void beforeAdvice(JoinPoint jp) {
        //获取目标对象类名
        String simpleName = jp.getTarget().getClass().getSimpleName();
        //获取目标对象中连接点的方法名
        String name = jp.getSignature().getName();
        //获取方法的参数列表
        String args = jp.getArgs()[0].toString();
        LOGGER.info("调用" + simpleName + "的" + name + "方法,方法的参数列表是" + args);
    }

    /**
     * 后置增强方法
     */
    public void afterReturningAdvice(JoinPoint jp, Object res) {
        //获取目标对象类名
        String simpleName = jp.getTarget().getClass().getSimpleName();
        //获取目标对象中连接点的方法名
        String name = jp.getSignature().getName();

        LOGGER.info("调用" + simpleName + "的" + name + "方法,方法的返回值是" + res);
    }

}

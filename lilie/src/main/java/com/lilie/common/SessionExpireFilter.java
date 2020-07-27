//package com.lilie.common;
//
//
//import com.lilie.pojo.User;
//import com.lilie.util.CookieUtil;
//import com.lilie.util.JsonUtil;
//
//import org.apache.commons.lang.StringUtils;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
///**
// * Created by geely
// */
//public class SessionExpireFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//        //因为写了cookie了，把session用户信息放在了redis中，进行了设置有效期。在这拿到信息，判断是否为空，如果不为空，拿到用户信息继续进行session的重置。
////        说白了，过滤器就相当于一个循环期，只要每次请求一个控制器，就要进入这个过滤器，进行过滤
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//
//        if (StringUtils.isNotEmpty(loginToken)) {
//            //判断logintoken是否为空或者""；
//            //如果不为空的话，符合条件，继续拿user信息
//
//            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//            User user = JsonUtil.string2Obj(userJsonStr, User.class);
//            if (user != null) {
//                //如果user不为空，则重置session的时间，即调用expire命令
//                RedisShardedPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
//            }
//        }
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//
//    @Override
//    public void destroy() {
//
//    }
//}

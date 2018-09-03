package com.nowcoder.interceptor;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by nowcoder on 2018/7/3.
 * 登录验证：LoginRequiredInterceptor
 * HandlerInterceptor：拦截器接口（直接继承就可以穿基恩一个拦截器的类）
 * //创建一个user的b包装类：hostHolder（ThreadLocal）
 * preHandler：先hostHolder中有么用户，没有直接sendRedirect到登录框页面
 * postHandle：
 * afterCompletion
 * 将拦截器配置到spring类的是ToutiaoWebConfiguration
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    //访问跳转
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (hostHolder.getUser() == null) {
            //重定向到首页，并且约定pop = 1，弹出登直接跳到登录框中登录（自己写的前端的）
            httpServletResponse.sendRedirect("/?pop=1");
            return false;
        }
        //如果存在。不用管了
        return true;
    }
  //在controll结束的时候，通常会把结果返回给view视图，在拦截器的postHandler中做到
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
   //全部完成后进入拦截器的afterCompletion方法，进行扫尾工作呀，如（把线程中的本地变量删除）
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}



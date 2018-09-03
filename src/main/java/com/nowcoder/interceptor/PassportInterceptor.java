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
 * Created by nowcoder on 2016/8/3.

 访问首页的时候，最先访问的是拦截器，此时controller还没有进来
 对于登录，注册成功后会自动登录:登录操作中，在service层进行逻辑判断，向上返回到controller，乡下Dao去和数据库交互
 登录代码中：服务器会生成 string类型的ticket，存入到cookies中，key值是ticket。value是ticket的值用过response下发到服务器

 权限验证：PassportInterceptor
 先访问preHandle：遍历cookie，找到ticket对应的值（浏览器缓存的那个ticket登录）
                 然后去数据库里面找这个ticket，判断信息是否有效，如有效，
                  去userDAO里面通过ticket找到这个用户，
                  把登陆的用户放到线程本地变量中，此时才进入controller
 postHandle:

 https://blog.csdn.net/chenchunlin526/article/details/69939337
 @Component 把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/>
 定义Spring管理Bean
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;

    /**
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     * 通过passportInteger的preHandler方法之后，进入到LoginRequiresInterceptor的
     * PreHandler方法，如果没有拿到user类，返回false不能进入controller类
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if (ticket != null) {
            // //ticket可能是伪造的，需要重新查询一下
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;
            }
            //如果是有效的；需要替换下来
            User user = userDAO.selectById(loginTicket.getUserId());
            //保存该用户，按照依赖注入的方法，因为以后很多地方都可能调用这个当前用户
            hostHolder.setUser(user);//存在线程里面threadLocal
        }
        return true;//意味着允许进入controller层
    }

    /**
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //结束了，需要判断，渲染之前存储用户，就可以知道user是否存在
        if (modelAndView != null && hostHolder.getUser() != null) {
            //把用户加进来：好处：渲染之前存储起来
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}

















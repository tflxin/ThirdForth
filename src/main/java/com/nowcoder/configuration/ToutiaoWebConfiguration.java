package com.nowcoder.configuration;

import com.nowcoder.interceptor.LoginRequiredInterceptor;
import com.nowcoder.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by nowcoder on 2018/7/3.
 * 拦截器的先后执行顺序由配置类ToutiaoConfiguration来决定

 先加入一个拦截器，注册进去，就会回调 通过自动配置的方式passportIntercept
 大家在使用2.0版本的springboot的时候
 使用WebMvcConfigurationSupport类配置拦截器时一定要重写addResourceHandlers来实现静态资源的映射,
 不要使用application.properties中添加配置来实现映射，
 不然资源会映射不成功导致打开页面资源一直加载不到。


 @Configuration
 public class WebMvcConfg implements WebMvcConfigurer {

 @Override
 public void addViewControllers(ViewControllerRegistry registry) {
 registry.addViewController("/index").setViewName("index");
 }


 */
//初始化，没有他就不会初始化
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {
    //加入拦截器，判断访问的这个人是谁
    @Autowired
    PassportInterceptor passportInterceptor;

    //登录拦截器：判断他访问的那个页面是否符合我的要求
    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //自动注册，每次回调：设计模式
        //先判断用户是谁，在判断满足要求
        registry.addInterceptor(passportInterceptor);//处理所有的
        //只是访问/setting页面的时候调用拦截器
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");
        super.addInterceptors(registry);
    }
/**
 *
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
*/
}















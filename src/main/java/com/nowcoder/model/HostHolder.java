package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * Created by nowcoder on 2018/7/3.
 * z对象的持有者：ThreadLocal:为了每一个对象分配一个副本：保持线程独立，安全性
 * 操纵user
 */
@Component
public class HostHolder {
    //定义的一个依赖注入的方法
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();;
    }
}

package com.nowcoder.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by rainday on 2018/6/30.
 自定义的一个试图类ViewObject 是一个map包装类，方便吧每一条咨询的信息（包括
 新闻信息，发帖的用户）放到一起，传到moder，方便前端拿到，做展示

 */
public class ViewObject {
    //专门用来试视图展示
    private Map<String, Object> objs = new HashMap<String, Object>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}

package com.nowcoder;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.NewsDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;

import com.nowcoder.model.News;
import com.nowcoder.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;


/*
 * 初始化数据库的测试用例 1 随机建立10个用户
 * 因为是测试用例所以有Assert
 * */

@RunWith(SpringJUnit4ClassRunner.class)

@SpringApplicationConfiguration(classes = ToutiaoApplication.class)

@Sql("/init-schema.sql")//表示跑之前跑我做好的这个

public class InitDatabaseTests {
   
 @Autowired
    UserDAO userDAO;

   
 @Autowired
    NewsDAO newsDAO;

   
 @Autowired
    LoginTicketDAO loginTicketDAO;

  
@Test
    public void context() {
    
    Random random = new Random();
     
   for (int i = 0; i < 11; ++i) {
         
   User user = new User();
           
 user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
     
       user.setName(String.format("user%d", i));
  
          user.setPassword("");
      
      user.setSalt("");
         
     userDAO.addUser(user);

    
        News news = new News();
         
     news.setCommentCount(i);
            
    Date date = new Date();
     
    date.setTime(date.getTime() + 1000*3600*5*i);
          
     news.setCreatedDate(date);
         
     news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            
     news.setLikeCount(i+1);
            
     news.setUserId(i+1);
            
     news.setTitle(String.format("TITLE{%d}", i));
            
      news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            
                  newsDAO.addNews(news);

            
              user.setPassword("newpassword");
            
              userDAO.updatePassword(user);

            
           //登陆的ticket

            LoginTicket ticket = new LoginTicket();
            ticket.setStatus(0);
            ticket.setUserId(i+1);
            ticket.setExpired(date);
            ticket.setTicket(String.format("TICKET%d", i+1));
            loginTicketDAO.addTicket(ticket);

            loginTicketDAO.updateStatus(ticket.getTicket(), 2);

        }

          /*
        * 常用断言介绍: 1. assertEquals([String message],Object target,Object result)  target与result不相等，中断测试方法，输出message assertEquals(a, b)
            测试a是否等于b（a和b是原始类型数值(primitive value)或者必须为实现比较而具有equal方法）
            assertEquals断言两个对象相等，若不满足，方法抛出带有相应信息
     arrest主要用来保证程序的正确性，通常在程序开发和测试时使用。为了提高程序运行的效率，在软件发布后，arrest检查默认是被关闭的
        * */
        //说明两个变量相等
        Assert.assertEquals("newpassword", userDAO.selectById(1).getPassword());
        //删除用户1
        userDAO.deleteById(1);
        //把1取出来一定是个null
        Assert.assertNull(userDAO.selectById(1));

        Assert.assertEquals(1, loginTicketDAO.selectByTicket("TICKET1").getUserId());
        Assert.assertEquals(2, loginTicketDAO.selectByTicket("TICKET1").getStatus());
    }

}

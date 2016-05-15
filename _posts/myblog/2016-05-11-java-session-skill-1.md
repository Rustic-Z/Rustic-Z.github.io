---
title: java中session详解
tagline: ""
last_updated: ""
category : java
layout: post
tags : [java,session]
published: true
description: ""
---
{% include JB/setup %}

# java session 基本原理  

## 1、Session简单介绍  

**一个浏览器就是一个新session,关了浏览器session就结束了**  
session是在服务器端建立的，浏览器访问服务器会有一个sessionid，浏览器端通过sessionid定位服务器端的session,session的创建和销毁由服务器端控制。  
当浏览器关闭后，session还存在在服务器端，只不过你新开的浏览器去访问服务器会创建另一个session,这个时候的sessionid已经不一样了。也就不能访问上一次的哪个session里面的内容了。  

**session的创建和销毁由服务器端控制**  
服务器端才有session,客户端只是通过sessionid来匹配session.  
那服务器端session如何建的呢？  
普通html不会创建，jsp默认是创建的，只要你访问任何一个jsp就会创建（不过只创建一次），你关闭浏览器重新访问又会创建一个，这些创建的session由服务器自己控制销毁，你也可以在服务器端代码中销毁。  

**什么情况下需要用上这种服务器端的session方式？**  
默认情况下，jsp被访问就会创建session(最开始是空的没有数据的),你的应用中的代码只是往session里面put数据。  
最后说一下，只有服务器端才有session.客户端被存到本地的是cookie.不过安全性低。所以不能放重要的数据。

## 2、Session和Cookie的主要区别  

* Cookie是把用户的数据写给用户的浏览器。
* Session技术把用户的数据写到用户独占的session中。
* Session对象由服务器创建，开发人员可以调用request对象的getSession方法得到session对象。

## 3、session实现原理  

服务器创建session出来后，会把session的id号，以cookie的形式回写给客户端，这样，只要客户端的浏览器不关，再去访问服务器时，都会带着session的id号去，服务器发现客户端浏览器带sessionid过来了，就会使用内存中与之对应的session为之服务。  
第一次访问时，服务器会创建一个新的sesion，并且把session的Id以cookie的形式发送给客户端浏览器。  
当浏览器再次请求服务器，会把存储到cookie中的sessionId一起传递到服务器端，以获取当前会话保存在服务器端的信息。  
需要注意的是，session生成后，只要用户继续访问，服务器就会更新session的最后访问时间，并维护该session。用户每访问服务器一次，无论是否读写session，服务器都认为该用户的Session“活跃（active）”了一次，并更新session的最后访问时间。  

## 4、session对象的创建和销毁  

**session对象的创建**  
在程序中第一次调用request.getSession()方法时就会创建一个新的Session，可以用isNew()方法来判断Session是不是新创建的  
创建session:  

```java
//使用request对象的getSession()获取session，如果session不存在则创建一个
HttpSession session = request.getSession();
//获取session的Id
String sessionId = session.getId();
//判断session是不是新创建的
if (session.isNew()) {
   response.getWriter().print("session创建成功，session的id是："+sessionId);
}else {
   response.getWriter().print("服务器已经存在session，session的id是："+sessionId);
}
```

**session对象的销毁**  
session对象默认30分钟没有使用，则服务器会自动销毁session，在web.xml文件中可以手工配置session的失效时间，例如：  

```java
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="2.5"
    xmlns="http://java.sun.com/xml/ns/javaee"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
    http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">
  <display-name></display-name>

  <welcome-file-list>
    <welcome-file>index.jsp</welcome-file>
  </welcome-file-list>

  <!-- 设置Session的有效时间:以分钟为单位-->
    <session-config>
        <session-timeout>15</session-timeout>
    </session-config>

</web-app>
```

当需要在程序中手动设置Session失效时，可以手工调用session.invalidate方法，摧毁session。  

```java
HttpSession session = request.getSession();
//手工调用session.invalidate方法，摧毁session
session.invalidate();
```

# java session 操作  

```java
               方  法  名                                                         描    述
void setAttribute(String attribute, Object value)    //设置Session属性。value参数可以为任何Java Object。通常为Java Bean。value信息不宜过大
String getAttribute(String attribute)                //返回Session属性
Enumeration getAttributeNames()                      //返回Session中存在的属性名
void removeAttribute(String attribute)               //移除Session属性
String getId()                                       //返回Session的ID。该ID由服务器自动创建，不会重复
long getCreationTime()                               //返回Session的创建日期。返回类型为long，常被转化为Date类型，例如：Date createTime = new Date(session.get CreationTime())
long getLastAccessedTime()                           //返回Session的最后活跃时间。返回类型为long
int getMaxInactiveInterval()                         //返回Session的超时时间。单位为秒。超过该时间没有访问，服务器认为该Session失效
void setMaxInactiveInterval(int second)              //设置Session的超时时间。单位为秒
void putValue(String attribute, Object value)        //不推荐的方法。已经被setAttribute(String attribute, Object Value)替代
Object getValue(String attribute)                    //不被推荐的方法。已经被getAttribute(String attr)替代
boolean isNew()                                      //返回该Session是否是新创建的
void invalidate()                                    //使该Session失效
```

# 服务器之间共享session的方案  

主要有：  
1. 客户端cookie加密;  
2. application server的session复制;  
3. 使用数据库保存session;  
4. 使用共享存储来保存session;  
5. 使用memcached来保存session;  
6. 使用terracotta来保存session;  

接下来将会实践一下采用memcached来共享session的方案。

# 感谢

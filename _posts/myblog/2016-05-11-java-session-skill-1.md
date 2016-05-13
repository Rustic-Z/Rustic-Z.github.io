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

## 一、java session 详解  

### 1、Session简单介绍  

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

### 2、Session和Cookie的主要区别  

* Cookie是把用户的数据写给用户的浏览器。
* Session技术把用户的数据写到用户独占的session中。
* Session对象由服务器创建，开发人员可以调用request对象的getSession方法得到session对象。

### 3、session实现原理  

服务器创建session出来后，会把session的id号，以cookie的形式回写给客户端，这样，只要客户端的浏览器不关，再去访问服务器时，都会带着session的id号去，服务器发现客户端浏览器带sessionid过来了，就会使用内存中与之对应的session为之服务。  
第一次访问时，服务器会创建一个新的sesion，并且把session的Id以cookie的形式发送给客户端浏览器。  
当浏览器再次请求服务器，会把存储到cookie中的sessionId一起传递到服务器端，以获取当前会话保存在服务器端的信息。


### 4、理解javax.servlet.http.HttpSession  


### 5、session对象的创建和销毁时机


## java session 操作  

## 服务器之间共享session的方案  

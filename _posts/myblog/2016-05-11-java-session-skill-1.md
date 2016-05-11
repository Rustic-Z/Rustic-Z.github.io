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

## java session 详解  

"一个浏览器就是一个新session,关了浏览器session就结束了"  

session是在服务器端建立的，浏览器访问服务器会有一个sessionid，浏览器端通过sessionid定位服务器端的session,session的创建和销毁由服务器端控制。  
当浏览器关闭后，session还存在在服务器端，只不过你新开的浏览器去访问服务器会创建另一个session,这个时候的sessionid已经不一样了。也就不能访问上一次的哪个session里面的内容了。  

"session的创建和销毁由服务器端控制"  

服务器端才有session,客户端只是通过sessionid来匹配session.  
那服务器端session如何建的呢？  
普通html不会创建，jsp默认是创建的，只要你访问任何一个jsp就会创建（不过只创建一次），你关闭浏览器重新访问又会创建一个，这些创建的session由服务器自己控制销毁，你也可以在服务器端代码中销毁。  

什么情况下需要用上这种服务器端的session方式？  
默认情况下，jsp被访问就会创建session(最开始是空的没有数据的),你的应用中的代码只是往session里面put数据。  
最后说一下，只有服务器端才有session.客户端被存到本地的是cookie.不过安全性低。所以不能放重要的数据。  

## java session 操作  

## 服务器之间共享session的方案  

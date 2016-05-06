---
title: java中操作cookie的方法
tagline: ""
last_updated: ""
category : java
layout: post
tags : [java,cookie]
published: true
description: ""
---
{% include JB/setup %}

## cookie定义：  

浏览器与WEB服务器之间是使用HTTP协议进行通信的；而HTTP协议是无状态协议。也就是说，当某个用户发出页面请求时，WEB服务器只是简单的进行响应，然后就关闭与该用户的连接。  

因此当一个请求发送到WEB服务器时，无论其是否是第一次来访，服务器都会把它当作第一次来对待，这样的不好之处可想而知。为了弥补这个缺陷，Netscape开发出了cookie这个有效的工具来保存某个用户的识别信息， 它是一种WEB服务器通过浏览器在访问者的硬盘上存储信息的手段。 它是服务器发送给浏览器的体积很小的纯文本信息。

## cookie属性：  

除名字与值外，每个cookie有四个可选属性：  
1. expires：指定cookie的生存期。默认情况下cookie是暂时的，浏览器关闭就失效。  
2. path：它指定了与cookie关联在一起的网页。默认是在和当前网页同一目录的网页中有效。如果把path设置为"/"，那么它对该网站的所有网页都可见了。  
3. domain：设定cookie有效的域名，如果把path设置为"/"，把domain设为".sohu.com"，那么 A.sohu.com和B.sohu.com的所有网页都能够访问此cookie。  
4. secure：布尔值，它指定了网络上如何传输cookie。默认情况下，cookie是不安全的，可以通过一个不安全的，普通的HTTP协议传输；若设置cookie为安全的，那么它将只在浏览器和服务器通过HTTPS或其它的安全协议连接在一起时才被传输。  

## java中cookie的设置：  

### 1、路径的设置：  
正常的cookie只能在一个应用中共享，既一个cookie只能有创建它的应用获得。
如在本机的tomcat/webapp下面有两个应用a和b，那么在a应用中设置cookie，不指定路径的话默认是：  

```
cookie.setPath("/a/");
```

那么在同一服务器下的b应用是访问不到该cookie的。  

如果在a应用中设置cookie时，指定路径：  

```
cookie.setPath("/");
```

那么a和b两个应用都能访问到该cookie。  

还有一种情况，在a中设置cookie时指定路径：  

```
cookie.setPath("/b/");
```

这时，a创建cookie的a访问不到cookie，b可以。  

### 2、所属域的设置  

如果在设置cookie的时候指定生效域：  

```
cookie.setDomain(".aaa.com");
```

那么只有通过url访问xxx.aaa.com时才能访问到改cookie，就是时域名解析ip是本机，通过localhost访问同样也拿不到cookie。  

下面是java设置cookie的一个简单小例子：  

```java
//设置cookie value 其中，cookie中的值必须全部转换为String
Cookie cookieId = new Cookie("cookieId",
    cookiesDTO.getUserId() == null ? null : cookiesDTO.getUserId().toString());
//设置cookie生效时间
cookieId.setMaxAge(60*60*24);
//设置cookie生效域
cookieId.setDomain(Constants.REDIRECT_DOMAIN);
this.getResponse().addCookie(cookieId);
```

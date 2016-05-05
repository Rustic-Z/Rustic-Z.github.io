---
title: spring中事务管理
tagline: ""
last_updated: ""
category : java
layout: post
tags : [java,spting]
published: true
description: ""
---
{% include JB/setup %}

前几天在写一个关于金额的业务功能时，用到事务管理，本意是想通过事务控制来使处理金额这部分关键功能达到同时成功或同时失败的目的。  

在处理这块时遇到一个问题，当时为了达到目的。直接在action方法上加上了一个事务注解，导致整个action类在对象注入中陷入混乱。  

原因是部分service是开启了自动事务提交的，而我新增的action方法又调用了多个不同的service对象。  

这时，问题出现了，如果将整个action设事务的话，就相当与一个事务中又有多个事务。外面的action方法要求整个同时成功或同时失败，而里面引入的service只要求自己对象中的方法同时成功或失败。这就导致spring在处理事务设置时混乱失败，从而影响后面的对象注入。  

解决的方法当然是把相应的业务逻辑封装在service方法中并设置事务了。而正常的java业务层次结构也应该是将业务放在相应的业务层的。

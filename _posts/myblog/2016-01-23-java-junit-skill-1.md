---
title: junit测试用例操作数据库不回滚
tagline: ""
last_updated: ""
category : java
layout: post
tags : [java,junit]
published: true
description: ""
---
{% include JB/setup %}

昨天为了测试一个线上关于时间判断的功能，想在mysql数据库里面插入一个datetime的数值，直接用sql不好操作。于是便想到了用单元测试去跑，coding。。单元测试运行成功。。。。  

可是，去数据库里面一查，数据还是没有变化。google一下，原来是junit为了防止测试用例给数据库造成的持久性的影响，默认对deleted和update操作做了自动回滚。。。。  

修改办法：  
在@Test之后加上@Rollback(false)就可以取消自动回滚，让junit测试用例真正影响数据库的数据。>_<

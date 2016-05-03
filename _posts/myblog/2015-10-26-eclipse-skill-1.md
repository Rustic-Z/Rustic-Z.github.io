---
title: 解决Eclipse中Java工程间循环引用而报错的问题
tagline: ""
last_updated: ""
category : eclipse
layout: post
tags : [eclipse]
published: true
description: "关于eclipse构建java工程的相关小笔记"
---
{% include JB/setup %}

今天在开发的过程中遇到一个问题。  

由于原来的工程被分拆成两个项目，一个是原项目所需的所有环境及框架配置文件（即空壳工程），而运行所依赖的所有源代码在另一个工程中。  

当在用eclipse编译的时候就会抛出工程间循环引用的错误，错误信息如下：  
```
Description Resource Path Location Type
A cycle was detected in the build path of project '***'. The cycle consists of projects {***, ***}
```

参考网上的一个解决方法可以让eclipse在编译的时候忽略错误，项目成功运行。  

下面贴出忽略编译的设置办法，做一笔记：
```
Eclipse Menu -> Window -> Preferences... -> Java -> Compiler -> Building -> Building path problems -> Circular dependencies -> 将Error改成Warning。
```

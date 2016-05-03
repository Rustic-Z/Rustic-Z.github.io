---
title: 让fedora中VirtualBox支持usb设备共享的简单方法
tagline: ""
last_updated: ""
category : linux
layout: post
tags : [fedora,VirtualBox]
---
{% include JB/setup %}

自从将操作系统换成fedora练手之后，就一直碰到杂七杂八的很多问题。。。（-_-||，实在小白。。。）  

其中之一便是用VirtualBox安装的虚拟机一直挂载不了我的USB设备，导致不论是我的u盘还是手机、平板都无法在我虚拟机中的win7识别，进而又导致我的ipad不能导入文件（这是最重要的原因！！）  

原先网上搜了很多方法，包括安装官方提供的Extension扩展。。。  

今天偶然在网上看到一片帖子完美解决了我的方法，方法很简单，直接在vboxusers（安装VirtualBox会自动创建这个用户组）这个用户组中加入你的user账户就ok了。  

下面将我的系统环境和解决方法列出，供参考：  
* 操作系统：fedora22  
* VirtualBox：5.0.4  

执行终端命令行：  
```shell
$ sudo usermod -a -G vboxusers YOUR USERNAME  //（YOUR USERNAME是你的用户名）
```

接着重启你的VirtualBox就可以了，看下是不是能够识别呢？

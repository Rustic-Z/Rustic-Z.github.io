---
title: nginx下部署wordpress博客项目
tagline: ""
last_updated: ""
category : wordpress
layout: post
tags : [wordpress]
published: true
description: ""
---
{% include JB/setup %}

主要参考文档：  
[在CentOS 6上搭建LNMP环境](https://mos.meituan.com/library/19/how-to-install-lnmp-on-centos6/)  
[在CentOS 6上安装WordPress](https://mos.meituan.com/library/16/how-to-install-wordpress-on-centos6/)  

# 安装nginx  

`nginx`是一个非常轻量级的HTTP服务器，Nginx，它的发音为“engine X”， 是一个高性能的HTTP和反向代理服务器，同时也是一个IMAP/POP3/SMTP 代理服务器。  
下面我们来安装`nginx`:  

```shell
root@iZ28fa5s0q4Z:~# apt-get install nginx
```

出现以下信息说明nginx已经安装成功：  

```shell
root@iZ28fa5s0q4Z:~# nginx -v
nginx version: nginx/1.4.6 (Ubuntu)
```  

如果会出现安装不成功的情况请先更新系统软件源`apt-get update`。  

# 安装mysql  

待补充...

# 安装php  

因为我们的wordpress是运行在php环境下的，所以这边我们需要先安装php:  

```shell
root@iZ28fa5s0q4Z:~# apt-get install php
```  

查看php版本信息：  

```shell
root@iZ28fa5s0q4Z:~# php -v
PHP 5.5.9-1ubuntu4.18 (cli) (built: Jun  1 2016 12:46:54)
Copyright (c) 1997-2014 The PHP Group
Zend Engine v2.5.0, Copyright (c) 1998-2014 Zend Technologies
    with Zend OPcache v7.0.3, Copyright (c) 1999-2014, by Zend Technologies
```  

# 在nginx下部署wordpress  

由于之前公司需要在公司主站域名下发布一个官方博客，而且为了不占用两个主要服务器的空间，我们选择的是将博客程序部署在另一台服务器上的。所以这里主要通过配置nginx的localtion来指定访问域名下的某子目录时统一反向代理到另一台服务器上的wordpress程序。

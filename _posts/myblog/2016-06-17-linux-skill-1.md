---
title: linux常用易忘命令合集
tagline: ""
last_updated: ""
category : linux
layout: post
tags : [linux]
published: true
description: ""
---
{% include JB/setup %}

[参考文档1](https://segmentfault.com/a/1190000002975306)  

命令行卸载软件包:  

```shell
root@iZ28fa5s0q4Z:~# apt-get autoremove softwareName
```  

查询文件或文件夹的磁盘使用空间:  

```shell
du -h --max-depth=1 work/testing
du -h --max-depth=1 work/testing/*
```  

将某个文件下下的所有文件打包成zip文件:  

```shell
[root@iZ23w59no07Z themes]# zip -r wp-theme.zip wp-theme/*
```  

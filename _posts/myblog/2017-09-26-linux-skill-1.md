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

ubuntu下卸载软件:  

```shell
zhangchao@zhangchao:~$ dpkg --list <packegename>  //查找相关软件包
zhangchao@zhangchao:~$ sudo apt-get --purge remove <packegename>  //卸载某软件包
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

## 更换ubuntu软件源：  

备份现有软件源：  

```shell
sudo cp /etc/apt/sources.list /etc/apt/sources.list_backup
```  

更换网易维护的软件源，根据不同的系统选择`sources.list`文件替换本机的文件。  
[网易ubuntu软件源帮助中心](http://mirrors.163.com/.help/ubuntu.html)  

由于没有公钥，无法验证下列签名： NO_PUBKEY 8D5A09DC9B929006  

```shell
sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 8D5A09DC9B929006
```  

新建用户组：  

```shell
root@iZ28fa5s0q4Z:~# addgroup login
```  

新建用户：  

```shell
root@iZ28fa5s0q4Z:~# useradd -d /home/rustic -s /bin/bash -m rustic
```  

其中，`-d`指定用户的主目录，`-s`指定用户的shell，`-m`表示如果该目录不存在，则创建该目录。  

给用户设置密码：  

```shell
root@iZ28fa5s0q4Z:/home# passwd rustic
```  

将用户添加至用户组中：  

```shell
root@iZ28fa5s0q4Z:/home# usermod -a -G login rustic
```  

linux 中 `grep` 使用技巧:  

1、在grep中使用`OR`操作:  
`grep -E 'key1|key2'`  

2、在grep中使用`AND`操作:  
`grep -E 'key1.*key2'`  

3、查询某文件指定字符串上下n行的内容:  
`grep 'key' -A n -B n`  
其中`-A`指定向下多少行，`-B`指定向上多少行，后面引号内的内容是指定查询的内容。

查看指定端口的进程:  

```shell  
netstat -anp|grep 9217
```  

批量删除远程分支:  

```shell
git branch -r| awk -F '[/]' '/201707/{printf "%s\n",$2}' | awk '!/branch-etc/{printf "%s\n",$1}' | xargs -I {} git push origin :{}
```  
`awk`用`/`做为分隔符切分`git branch -r`的远程分支结果，接着`awk`过滤掉不需要删除的分支`branch-etc`，最后用`xargs`将每行的参数替换成字符串`{}`，并逐行执行后面的`git push origin :{}`。

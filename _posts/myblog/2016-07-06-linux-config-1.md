---
title: linux服务器禁用ssh 22端口、root以及密码方式登录
tagline: ""
last_updated: ""
category : linux
layout: post
tags : [linux, ssh]
published: true
description: ""
---
{% include JB/setup %}

# 登录组用户设置  

新建login用户组:  

```shell
[root@iZ23w59no07Z ~]# groupadd login
```  

新建登录用户并加到login用户组中:  

```shell
[root@iZ23w59no07Z ~]# useradd -d /home/rustic -s /bin/bash -m rustic
```  

设置密码:  

```shell
[root@iZ23w59no07Z ~]# passwd rustic
```  

将新用户添加到用户组login中:  

```shell
[root@iZ23w59no07Z ~]# usermod -a -G login rustic
```  

为新用户设定sudo权限:  

```shell
[root@iZ23w59no07Z ~]# visudo
```  

在`root    ALL=(ALL)       ALL`下一行添加  

```shell
rustic  ALL=(ALL)       ALL
```  

ok,保存退出，然后用新用户登录服务器试试。  

## 设置ssh免密登录  

在新用户目录下新建`.ssh`目录，并在`.ssh`目录下新建`authorized_keys`文件。

```shell
[rustic@iZ23w59no07Z ~]# mkdir -p .ssh/
```  

将本地需要登录服务器的机子的public key添加到`authorized_keys`文件中:  

```shell
[rustic@iZ23w59no07Z ~]# vim authorized_keys
```  

给予`.ssh`文件夹700权限，`authorized_keys`文件600权限:  

```shell
[rustic@iZ23w59no07Z ~]# chmod 600 ~/.ssh/* && chmod 700 ~/.ssh/
```  

推出服务器，尝试用新用户登录服务器，看是否不需要输入密码了。  

## 禁用ssh root登录及更改端口配置  

用root账号登录服务器，编辑SSH配置文件/etc/ssh/sshd_config：  

```shell
[root@iZ23w59no07Z ~] vim /etc/ssh/sshd_config
```  

修改以下内容:  

```shell
Port 23333

Protocol 2

PermitRootLogin no
PermitEmptyPasswords no
PasswordAuthentication no

RSAAuthentication yes
PubkeyAuthentication yes
AuthorizedKeysFile .ssh/authorized_keys

UseDNS no
```  

以上内容主要是更换ssh登录端口以及禁用root及密码登录。  

另外可以指定某几个用户通过ssh登录服务器，在配置文件最下面添加:  

```shell
AllowUsers rustic
```  

保存文件，并重启ssh:  

```shell
[root@iZ23w59no07Z ~] server sshd restart
``` 

## 感谢
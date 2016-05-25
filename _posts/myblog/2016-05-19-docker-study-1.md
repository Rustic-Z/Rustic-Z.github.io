---
title: docker学习笔记-docker基本概念及安装
tagline: ""
last_updated: ""
category : docker
layout: post
tags : [docker]
published: true
description: ""
---
{% include JB/setup %}

# docker基本概念  

## 什么是docker?  
Docker 是一个开源项目，诞生于2013年初，最初是aotCloud公司内部的一个业余项目，它基于google公司推出的Go语言实现，项目后来加入了Linux基金会。  

Docker 项目的目标是实现轻量级的操作系统虚拟化解决方案。Docker 的基础是[linux容器（LXC）](https://www.ibm.com/developerworks/cn/linux/l-lxc-containers/)等技术。  

在LXC的基础上Docker进行了进一步的封装，让用户不需要去关心容器的管理，使得操作更为简便。用户操作Docker 的容器就像操作一个快捷轻量级的虚拟机一样简单。  

## 为什么要使用 Docker  
对于平常的开发与运维来讲，我们总是在软件开发完成之后的部署与维护上花费较大时间。那么我们肯定是希望一次创建或部署一个软件运行环境之后，可以在任意平台上正常运行改环境，省去重新部署与维护的时间。  

这时，开发者可以运用Docker 创建一个标准的镜像来构建一套开发容器，开发完成之后，运维人员可以直接使用该容器来部署环境。Docker  可以快速创建容器，快速迭代应用程序，并让整个过程全程可见，使用团队中的其他成员更容易理解应用程序是如何创建和工作的。Docker 容器很轻很快，容器的启动时间是秒级的，大量的节约开发、测试、部署的时间。  

## 镜像(image)  
Docker 镜像(image) 就是一个只读的模板。  

例如：一个镜像可以包含一个完整的`ubuntu`操作系统环境，里面仅安装了Apache或用户需要的其他应用程序。  

镜像可以用来创建Docker 容器。  

Docker 提供了一个很简单的机制来创建镜像或者更新现有的镜像，用户甚至可以直接从其他人那里下载一个已经做好的镜像来直接使用。  

## Docker 容器(Container)  
Docker 利用容器来运行应用。  

容器是镜像创建的运行实例。它可以被启动、开始、停止、删除。每个容器都是相互隔离的、保证安全的平台。  

可以把容器看做是一个简易版的`Linux环境`(包括root用户权限、进程空间、用户空间和网络空间等)和运行在其中的`应用程序`。  

## Docker 仓库(Repository)  
仓库是集中存放镜像文件的场所。  

仓库分为公开仓库和私有仓库两种形式。  

最大的公开仓库是`Docker Hub`，存放了数量庞大的镜像供用户下载。国内的公开仓库包括`Docke Pool`、`灵雀云`等，可以提供大陆用户更稳定快速的访问。  

用户可以在本地网络中创建一个私有仓库，当用户创建了自己的镜像之后就可以使用`push`命令将它上传到公有或私有的仓库中，这样下次在另外一台机子中就可以随时`pull`下来这个镜像使用了。  

其实`Docker`的理念感觉与`git`很类似。`git`管理的是文件，而`Docker`管理的是容器镜像。  

# `Ubuntu`中安装`Docker`  

`docker`目前只支持安装在`64位`的系统环境中，并且要求内核版本不低于 3.10。  

## 准备工作  
查看内核版本详细信息：  

```shell
root@iZ28fa5s0q4Z:~# uname -a
Linux iZ28fa5s0q4Z 3.13.0-32-generic #57-Ubuntu SMP Tue Jul 15 03:51:08 UTC 2014 x86_64 x86_64 x86_64 GNU/Linux
```  

`docker`目前支持的最低`Ubuntu`版本为`12.04 LTS`。  

安装apt-transport-https：  

```shell
root@iZ28fa5s0q4Z:~# apt-get install apt-transport-https ca-certificates
```  

添加源的gpg密钥：  

```shell
root@iZ28fa5s0q4Z:~# apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D
```  

获取当前操作系统的代号：  

```shell
root@iZ28fa5s0q4Z:~# lsb_release -c
Codename:	trusty
```  

接着添加`docker`的官方apt软件源：  

```shell
root@iZ28fa5s0q4Z:~# cat <<EOF > /etc/apt/sources.list.d/docker.list
> deb https://apt.dockerproject.org/repo ubuntu-trusty main
> EOF
```  

上面的`ubuntu-trusty`需要对应的改成自己系统的版本代号。  

更新软件包缓存：  

```shell
root@iZ28fa5s0q4Z:~# apt-get update
```

## 预安装  
为了让`docker`使用aufs存储，推荐安装`linux-image-extra`软件包：  

```shell
root@iZ28fa5s0q4Z:~# apt-get install -y linux-image-extra-$(uname -r)
```  

安装`apparmor`，apparmor是Linux内核的一个安全模块：  

```shell
root@iZ28fa5s0q4Z:~# apt-get install apparmor
```  

## 安装`Docker`  
在成功添加源之后，就可以安装最新版本的`Docker`了，软件包名称是`docker-engine`。  

```shell
root@iZ28fa5s0q4Z:~# apt-get install -y docker-engine
```  

检查`Docker`版本，若出现类似版本信息，则说明已经安装成功了。  

```shell
root@iZ28fa5s0q4Z:~# docker -v
Docker version 1.11.1, build 5604cbe
```  

# 感谢  

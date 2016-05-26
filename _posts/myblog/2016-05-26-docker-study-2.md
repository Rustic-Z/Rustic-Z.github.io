---
title: docker学习笔记-docker镜像
tagline: ""
last_updated: ""
category : docker
layout: post
tags : [docker]
published: true
description: ""
---
{% include JB/setup %}

# 获取镜像  
我们可以使用类似`git`的命令`docker pull`来从仓库获取所需要的镜像。  

首先我们从远程仓库下载一个`Ubuntu 14.04`操作系统的镜像。  

```shell
root@iZ28fa5s0q4Z:~# docker pull ubuntu:14.04
14.04: Pulling from library/ubuntu

6599cadaf950: Pull complete
23eda618d451: Pull complete
f0be3084efe9: Pull complete
52de432f084b: Pull complete
a3ed95caeb02: Pull complete
Digest: sha256:15b79a6654811c8d992ebacdfbd5152fcf3d165e374e264076aa435214a947a3
Status: Downloaded newer image for ubuntu:14.04
```  

该命令等同于`docker pull registry.hub.docker.com/ubuntu:14.04`，即从注册服务器上`registry.hub.docker`中的`ubuntu`仓库下载标记为`14.04`的镜像。  

完成之后，我们就可以查看并使用该镜像了。  

# 列出本地镜像  
我们使用`docker images`显示本地已有的镜像：  

```shell
root@iZ28fa5s0q4Z:~# docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
mydocker/ubuntu     rvm                 e28b49754b4a        15 seconds ago      229.7 MB
ubuntu              14.04               90d5884b1ee0        3 weeks ago         188 MB
ubuntu              12.04               2bffcdf4b693        3 weeks ago         138.5 MB
```  

其中：  
* `REPOSITORY`表示来自哪一个仓库  
* `TAG`是镜像的标记  
* `IMAGE ID`是镜像的id  
* `CREATED`创建时间  
* `SIZE`是镜像的大小  

# 修改镜像  
我们现在来修改一个镜像，首先需要使用镜像启动容器：  

```shell
root@iZ28fa5s0q4Z:~# docker run -t -i ubuntu:14.04 /bin/bash  //也可以不加/bin/bash，默认进入之后自动是启动bash应用的
```  

我们在容器中给这个镜像安装rvm：  

```shell
root@31567ecb6df1:/# apt-get install gcc
root@31567ecb6df1:/# gpg --keyserver hkp://keys.gnupg.net --recv-keys 409B6B1796C275462A1703113804BB82D39DC0E3
root@31567ecb6df1:/# apt-get install curl
root@31567ecb6df1:/# curl -sSL https://get.rvm.io | bash -s stable
root@31567ecb6df1:/# source /usr/local/rvm/scripts/rvm
root@31567ecb6df1:/# rvm -v
```  

具体的安装教程可以参考另一篇文章[ubuntu中安装搭建jekyll环境](http://rustic-z.github.io/2016/04/24/jekyll-blog-build)。  

然后我们把对这个镜像的修改操作进行提交：  

```shell
root@iZ28fa5s0q4Z:~# docker commit -a "rustic" -m "install rvm" 31567ecb6df1 mydocker/ubuntu:rvm
sha256:e28b49754b4af523c34cc50de721a37040b2e28b5a3b08ae208702c8e0391217
```  

其中，`-a`指定修改人，`-m`指定提交描述，紧接着的一串字符表示操作该镜像的容器id，即刚才我们命令行`root@`用户后面的容器id`31567ecb6df1`，再后面就是我们自定义的镜像所在仓库名及镜像标签。  

`docker images`来看下：  

```shell
root@iZ28fa5s0q4Z:~# docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
mydocker/ubuntu     rvm                 e28b49754b4a        15 seconds ago      229.7 MB
ubuntu              14.04               90d5884b1ee0        3 weeks ago         188 MB
ubuntu              12.04               2bffcdf4b693        3 weeks ago         138.5 MB
```  

之后我们变可以用新镜像来启动容器了：  

```shell
root@iZ28fa5s0q4Z:~# docker run -t -i mydocker/ubuntu:rvm
root@1ac4e34ffaa9:/#
```

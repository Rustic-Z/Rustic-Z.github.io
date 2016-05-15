---
title: ubuntu中安装搭建jekyll环境
tagline: ""
last_updated: ""
category : jekyll
layout: post
tags : [blog,jekyll]
published: true
description: ""
---
{% include JB/setup %}

# 使用rvm安装ruby和gem  

## 先查看系统版本：  

```shell
root@iZ28fa5s0q4Z:~# uname -a
Linux iZ28fa5s0q4Z 3.13.0-32-generic #57-Ubuntu SMP Tue Jul 15 03:51:08 UTC 2014 x86_64 x86_64 x86_64 GNU/Linux
```

## 安装gcc：  

```shell
root@iZ28fa5s0q4Z:~# apt-get install gcc
```

## 安装rvm：  

```shell
root@iZ28fa5s0q4Z:~# gpg --keyserver hkp://keys.gnupg.net --recv-keys 409B6B1796C275462A1703113804BB82D39DC0E3  //添加钥匙环
//如果curl没有安装，请先安装
root@iZ28fa5s0q4Z:~# apt-get install curl
root@iZ28fa5s0q4Z:~# curl -sSL https://get.rvm.io | bash -s stable  //下载rvm源文件
//如果上面下载不了的话请用下面
curl -L https://raw.githubusercontent.com/wayneeseguin/rvm/master/binscripts/rvm-installer | bash -s stable
```

然后，载入 RVM 环境（新开 Termal 就不用这么做了，会自动重新载入的）  

```shell
root@iZ28fa5s0q4Z:/# source /usr/local/rvm/scripts/rvm  //下载完成后，会提示rvm安装在了哪个文件下面。我是root用户则直接安装在/usr/local/下
```

修改 RVM 下载 Ruby 的源，到 Ruby China 的镜像:  

```shell
root@iZ28fa5s0q4Z:/# echo "ruby_url=https://cache.ruby-china.org/pub/ruby" > /usr/local/rvm/user/db
```

查看是否安装成功：  

```shell
root@iZ28fa5s0q4Z:/# rvm -v
rvm 1.27.0 (latest) by Wayne E. Seguin <wayneeseguin@gmail.com>, Michal Papis <mpapis@gmail.com> [https://rvm.io/]
```

安装rvm的环境依赖:  

```shell
root@iZ28fa5s0q4Z:~# rvm requirements
```

列出rvm管理的软件和版本：  

```shell
root@iZ28fa5s0q4Z:~# rvm list known
# MRI Rubies
[ruby-]1.8.6[-p420]
[ruby-]1.8.7[-head] # security released on head
[ruby-]1.9.1[-p431]
[ruby-]1.9.2[-p330]
[ruby-]1.9.3[-p551]
[ruby-]2.0.0[-p648]
[ruby-]2.1[.8]
[ruby-]2.2[.4]
[ruby-]2.3[.0]
[ruby-]2.2-head
ruby-head

# for forks use: rvm install ruby-head-<name> --url https://github.com/github/ruby.git --branch 2.2

# JRuby
jruby-1.6[.8]
jruby-1.7[.23]
jruby[-9.0.5.0]
jruby-head

# Rubinius
rbx-1[.4.3]
rbx-2.3[.0]
rbx-2.4[.1]
rbx[-2.5.8]
rbx-head

# Opal
opal

# Minimalistic ruby implementation - ISO 30170:2012
mruby[-head]

# Ruby Enterprise Edition
ree-1.8.6
ree[-1.8.7][-2012.02]

# GoRuby
goruby

# Topaz
topaz

# MagLev
maglev[-head]
maglev-1.0.0

# Mac OS X Snow Leopard Or Newer
macruby-0.10
macruby-0.11
macruby[-0.12]
macruby-nightly
macruby-head

# IronRuby
ironruby[-1.1.3]
ironruby-head
```

## 安装ruby：  

```shell
root@iZ28fa5s0q4Z:~# rvm install ruby-2.3.0  //我们这个安装最新的ruby-2.3.0
//如果安装不成功的话，需要运行如下代码重新下载
root@iZ28fa5s0q4Z:~# rvm reinstall ruby-2.3.0
```

经过漫长的下载，编译过程，完成以后，Ruby, Ruby Gems 就安装好了。  
这里需要注意的是，ruby源代码可能会下载很长时间，可以直接到相关目录下执行git clone下载ruby，再继续安装。  

选择当前的ruby为默认版本，并查看是否安装成功：  

```shell
root@iZ28fa5s0q4Z:~# rvm use ruby-2.3.0
root@iZ28fa5s0q4Z:~# rvm use 2.3.0 --default
root@iZ28fa5s0q4Z:~# ruby -v
ruby 2.3.0p0 (2015-12-25 revision 53290) [x86_64-linux]
```

修改gem软件源，并更新gem：  

```shell
root@iZ28fa5s0q4Z:~# gem sources --add https://gems.ruby-china.org/ --remove https://rubygems.org/
root@iZ28fa5s0q4Z:~# gem update --system
```

# 安装jekyll  

执行如下命令安装jekyll：  

```shell
root@iZ28fa5s0q4Z:~# gem install jekyll
```

查看jekyll版本确认是否安装成功：  

```shell
root@iZ28fa5s0q4Z:~# jekyll -v
jekyll 3.1.3
```

使用jekyll new一个新的blog程序：  

```shell
root@iZ28fa5s0q4Z:~/workspace# jekyll new myblog
New jekyll site installed in /root/workspace/myblog.
```

启动jekyll项目：  

```shell
root@iZ28fa5s0q4Z:~/workspace/myblog# jekyll serve
Configuration file: /root/workspace/myblog/_config.yml
            Source: /root/workspace/myblog
       Destination: /root/workspace/myblog/_site
 Incremental build: disabled. Enable with --incremental
      Generating...
                    done in 0.387 seconds.
 Auto-regeneration: enabled for '/root/workspace/myblog'
Configuration file: /root/workspace/myblog/_config.yml
    Server address: http://127.0.0.1:4000/
  Server running... press ctrl-c to stop.
```

现在可以通过在浏览器访问<http://127.0.0.1:4000/>，访问你的blog程序了。

# 感谢

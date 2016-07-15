---
title: git pull 与 git pull --rebase
tagline: ""
last_updated: ""
category : java
layout: post
tags : [java,Multi-thread]
published: true
description: ""
---
{% include JB/setup %}

# git pull 与 git pull --rebase  

## git pull  

一般我们在开发一个新的功能时，通常会在某个稳定版本的分支基础上，新拉一个分支出来进行开发。比如在`mobile`分支的基础上新建分支`wxpay`进行微信支付的开发，此时可能需要两个人同时在该分支上进行改动并提交。  

在之前的所有代码提交远程的操作中，喔图技术小组用的一直是:  

`git add .` ->`git commit -am ""`->`git pull`->`git push`  

的方式进行代码提交到仓库。  

那么以这样的方式进行远程代码提交的话通常会出现以下情况:  

![git pull](/images/git-pull-rebase/2016-07-08 14:52:11屏幕截图.png)  

这样的情况会导致出现分支合并频繁。  

出现这样的原因是因为，我们通常拉取远程分支如`wxpay(remote)`的同时，本地存在一个与远程拥有同样信息同样名字的分支`wxpay(local)`。  

此时我们在`wxpay(local)`分支pull`wxpay(remote)`时，会将远程分支`merge`到本地`wxpay`，同时将远程分支一系列的提交合并记录也更新到本地分支，接着我们`git push`本地分支到远程时，也就同时把这新产生的提交合并记录push到远程分支去。  
　那么这个时候远程分支曲线就会看到一个新的分支从一个点拉出来然后在另一个点又被合进去。而其实这个时候我们在同一分支的改动可能是非常小的。  

`git pull`原理如下图:  
![git pull](/images/git-pull-rebase/2016-07-08 14:17:50屏幕截图.png)  

![git pull](/images/git-pull-rebase/2016-07-08 14:18:20屏幕截图.png)  

## git pull --rebase  

这里加上`rebase`的意思是，

- 首先将本地分支从上次`pull`下来之后的变更全部暂存起来  
- 恢复到上次pull时候的状态  
- 从远程分支更新变更记录到本地  
- 最后将暂存起来的本地变更更新到该分支上。  

这样，一次从拉取远程分支到本地的操作就完成了，只不过此时将两个分支的提交记录整合到一个分支上，并忽略了合并记录。  
合并记录在我们这对某一个分支频繁的提交拉取中，其实是可以忽略的，这样反而可以在远程分支树中保持分支合并记录的一个整洁。  

`git pull --rebase`原理如下图:  

![git pull](/images/git-pull-rebase/2016-07-08 14:18:33屏幕截图.png)  

![git pull](/images/git-pull-rebase/2016-07-08 14:18:47屏幕截图.png)  

![git pull](/images/git-pull-rebase/2016-07-08 14:19:04屏幕截图.png)  

---
title: java多线程学习-基本概念及接口
tagline: ""
last_updated: ""
category : java
layout: post
tags : [java,Multi-thread]
published: true
description: ""
---
{% include JB/setup %}

# java多线程的使用  

## 继承Thread类实现多线程编程  
在java中实现多线程编程主要有两种方式：一种是继承Thread类，另一种是实现Runnable接口。  
值得注意的是，Thread类其实是实现了Runnable接口的。  
下面是一个简单的线程类：  

```java
public class MyThread extends Thread {
	@Override
	public void run() {
		super.run();
		System.out.println("MyThread线程执行结束！");
	}
}
```

用main方法调用线程测试：  

```java
public class TestRun {
	public static void main(String[] args) {
		MyThread myThread = new MyThread();
		myThread.start();
		System.out.println("main线程执行结束！");
	}
}
```

执行main方法之后观察执行结果：  

![oauth_2_0简单授权码流程图](/images/multi-thread/multi-thread-1.png)  

从执行结果看出，执行次序并不是按照代码执行顺序来的。说明线程运行结果与调用顺序没有关系。

# java多线程基本方法简介  

# 线程停止  

# 线程暂停  

# 线程优先级  

# 守护线程  

# 感谢

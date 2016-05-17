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

在java中实现多线程编程主要有两种方式：一种是继承Thread类，另一种是实现Runnable接口。  

值得注意的是，Thread类其实是实现了Runnable接口的。  

## 继承Thread类     
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

![multi-thread-1](/images/multi-thread/multi-thread-1.png)  

从执行结果看出，执行次序并不是按照代码执行顺序来的。说明线程运行结果与调用顺序没有关系。  

## 实现Runnable接口  
实现Runnable接口的一个简单示例：  

```java
public class MyRunnable implements Runnable {
	public void run() {
		System.out.println("MyRunnable线程执行结束！");
	}
}
```

用main方法调用线程类测试：  

```java
public class TestRun {
	public static void main(String[] args) {
		Runnable runnable = new MyRunnable();
		Thread thread = new Thread(runnable);  //Thread类的具体构造函数可以参看java api
		thread.start();
		System.out.println("main线程执行结束！");
	}
}
```

执行结果：  

![multi-thread-1](/images/multi-thread/multi-thread-2.png)  

另外，由于Thread.java也实现了Runnable接口，也就意味着我们可以在Thread构造函数中传入一个thread对象。  

而由于java8之前java类是单继承的，所以为了支持多继承，我们完全可以用实现Runnable接口的方式实现多线程。

# java多线程基本方法简介  

## start()方法  
Thread.java类中的start()方法并不是执行改线程，而是通知“线程规划器”此线程已经准备就绪，等待调用线程对象的run()方法。  

这个过程其实就是让系统安排一个时间来调用Thread中的run()方法，也就是使线程得到运行，启动线程，具有异步执行的效果。  

而如果你直接调用线程对象的run()方法，则是由当前线程去执行thread类中的run()方法，是同步的，这时，方法执行顺序完全是按照代码顺序来的。  

## currentThread()方法  

# 线程停止  

# 线程暂停  

# 线程优先级  

# 守护线程  

# 感谢

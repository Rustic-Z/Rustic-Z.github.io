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

![multi-thread-2](/images/multi-thread/multi-thread-2.png)  

另外，由于Thread.java也实现了Runnable接口，也就意味着我们可以在Thread构造函数中传入一个thread对象。  

而由于java8之前java类是单继承的，所以为了支持多继承，我们完全可以用实现Runnable接口的方式实现多线程。

# java多线程基本方法简介  

## start()方法  
Thread.java类中的start()方法并不是执行改线程，而是通知“线程规划器”此线程已经准备就绪，等待调用线程对象的run()方法。  

这个过程其实就是让系统安排一个时间来调用Thread中的run()方法，也就是使线程得到运行，启动线程，具有异步执行的效果。  

而如果你直接调用线程对象的run()方法，则是由当前线程去执行thread类中的run()方法，是同步的，这时，方法执行顺序完全是按照代码顺序来的。  

## currentThread()方法  
currentThread()方法可返回代码段正在被哪个线程调用的信息。首先我们来看下currentThread()方法返回的信息。

```java
public class MyThread extends Thread {
	@Override
	public void run() {
		super.run();
		System.out.println("查看currentThread方法返回的内容：" + Thread.currentThread());
		System.out.println("MyThread线程执行结束！");
	}
}
```

```java
public class TestRun {
	public static void main(String[] args) {
		MyThread myThread = new MyThread();
		myThread.start();
		System.out.println("main线程执行结束！");
	}
}
```

执行结果：  

![multi-thread-3](/images/multi-thread/multi-thread-3.png)  

其中Thread-0为线程名，5为线程优先级（没有指定的情况下，默认是5），main代表线程所在的线程组。  

## isAlive()方法  
方法isAlive()的功能是判断当前的线程是否处于活动状态。  

创建MyThread2类，其中有一个构造函数，我们将以传入线程对象的方式来创建线程对象  

```java
public class MyThread2 extends Thread {
	public MyThread2() {
		System.out.println("MyThread2Object --- begin");
		System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
		System.out.println("Thread.currentThread().isAlive() = " + Thread.currentThread().isAlive());
		System.out.println("this.getName() = " + this.getName());
		System.out.println("this.isAlive() = " + this.isAlive());
		System.out.println("MyThread2Object --- end");
	}

	@Override
	public void run() {
		super.run();
		System.out.println("run --- begin");
		System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
		System.out.println("Thread.currentThread().isAlive() = " + Thread.currentThread().isAlive());
		System.out.println("this.getName() = " + this.getName());
		System.out.println("this.isAlive() = " + this.isAlive());
		System.out.println("run --- end");
	}
}
```

调用线程  

```java
public class TestRun {
	public static void main(String[] args) {
		MyThread2 myThread2 = new MyThread2();
		Thread thread = new Thread(myThread2);
		System.out.println("main begin thread2 isAlive = " + thread.isAlive());
		thread.setName("线程1");
		thread.start();
		System.out.println("main end thread2 isAlive = " + thread.isAlive());
	}
}
```

执行结果：  

![multi-thread-4](/images/multi-thread/multi-thread-4.png)  

可以看出构造函数构造对象时，这些代码是由main方法调用的。而使用this指定查看线程信息时，可以查看对象是由另外一个线程统一托管的。这里的this有一些细微的差别。  

## sleep()方法  
方法sleep()的作用是在指定的毫秒数内让当前“正在执行的线程”休眠（暂停执行）。  

这个“正在执行的线程”是指this.currentThread()返回的线程。  

我们来看代码：  

```java
public class MyThread extends Thread {
	@Override
	public void run() {
		try {
			System.out.println("run threadName=" + this.currentThread().getName() + " begin =" + System.currentTimeMillis());
			Thread.sleep(2000);
			System.out.println("run threadName=" + this.currentThread().getName() + " end =" + System.currentTimeMillis());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
```

```java
public class TestRun {
	public static void main(String[] args) {
		MyThread myThread = new MyThread();
		System.out.println("begin =" + System.currentTimeMillis());
		myThread.start();
		System.out.println("end =" + System.currentTimeMillis());
	}
}
```

执行结果：  

![multi-thread-5](/images/multi-thread/multi-thread-5.png)  

# 线程停止  
线程停止由于有较多内容，决定另起篇幅进行学习记录。

# 线程暂停  

## suspend()方法  
我们这里的suspend()方法可以将线程暂时停止，直到你调用函数恢复线程运行状态。

## resume()方法  
resume()方法可以将suspend()方法停止的线程恢复到运行状态。  

## 线程暂停举例  
下面举个例子进行暂停：  

```java
public class MyThread extends Thread {
	private long i = 0L;
	@Override
	public void run() {
		while(true) {
			i++;
		}
	}

	public long getI() {
		return i;
	}

	public void setI(long i) {
		this.i = i;
	}
}
```  

用测试类调用该线程：  

```java
public class TestRun {
	public static void main(String[] args) {
		try {
			MyThread myThread = new MyThread();
			myThread.start();
			//让main方法睡眠5秒
			Thread.sleep(5000);
			//让线程停止
			myThread.suspend();
			System.out.println("A= " + System.currentTimeMillis() + " i = " + myThread.getI());
			Thread.sleep(5000);
			System.out.println("A= " + System.currentTimeMillis() + " i = " + myThread.getI());
			//恢复线程运行状态
			myThread.resume();
			System.out.println("B= " + System.currentTimeMillis() + " i = " + myThread.getI());
			Thread.sleep(5000);
			System.out.println("B= " + System.currentTimeMillis() + " i = " + myThread.getI());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
```  

运行结果如下:  
![multi-thread-6](/images/multi-thread/multi-thread-6.png)   

## 线程停止的缺点  

### 线程占用  

从上面我们可以看到线程停止的效果，只要你部执行线程恢复的方法，那么这个被停止的线程将一直处在停止状态。  

假设被执行到的方法是同步的，或者是`加锁的代码块或方法`。那么此时，该线程将会一直`占用`着这些资源，也就导致了`线程死锁`的出现，其他线程将无限等待该被停止线程。所以，这个方法是危险的，在应用到实际项目中，使用也需要谨慎。而从上面的例子可以看到，这两个方法也是被注解的。  

### 不同步  

那么除了线程死锁这个比较值得注意的问题外，还有另外一个问题，也就是`数据不同步`的问题。  

我们假设想，如果一个用户操作，需要同时两个数据一起同步过来才能达到我们程序期望达到的目的。但若是在这期间，操作两个数据其中一个数据的线程暂停了。那么一个数据返回时，得不到与其对应的另一个数据。此时，就会导致数据不同步的问题出现，这也是比较值得注意的地方。比如用户登录、绑定相关操作等。  

# 线程优先级  

在操作系统中，线程可以划分优先级，优先级较高的线程得到的CPU资源较多，也就是CPU优先执行优先级较高的线程对象中的任务。  

在java中，线程的优先级分为1~10这10个等级，如果小于1或大于10，则JDK抛出异常throw new IllegalArgumentException().  

## 线程优先级示例：  

```java
public class MyThread1 extends Thread {
	@Override
	public void run() {
		long beginTime = System.currentTimeMillis();
		long result = 0;
		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < 5000; i++) {
				Random random = new Random();
				random.nextInt();
				result = result + i;
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("##### thread 1 use time = " + (endTime - beginTime));
	}
}
```  

```java
public class MyThread2 extends Thread {
	@Override
	public void run() {
		long beginTime = System.currentTimeMillis();
		long result = 0;
		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < 5000; i++) {
				Random random = new Random();
				random.nextInt();
				result = result + i;
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("***** thread 2 use time = " + (endTime - beginTime));
	}
}
```  

测试线程：  

```java
public class TestRun {
	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			MyThread1 thread1 = new MyThread1();
			thread1.setPriority(10);
			thread1.start();
			MyThread2 thread2 = new MyThread2();
			thread2.setPriority(1);
			thread2.start();
		}
	}
}
```  

运行结果如下：  
![multi-thread-7](/images/multi-thread/multi-thread-7.png)  ![multi-thread-8](/images/multi-thread/multi-thread-8.png)  ![multi-thread-9](/images/multi-thread/multi-thread-9.png)  

运行结果如上，我们可以看到优先级高的线程其运行结果总是比优先级低的线程较快出现，也就是较先执行。  

## 线程优先级具有继承性  
在java中，线程的优先级具有继承性，比如A线程启动B线程，则B线程的优先级与A是一样的。  

# 守护线程  
在java线程中有两种线程，一种是用户线程，另外一种是守护（Daemon）线程。  

什么是守护线程？守护线程是一种特殊的线程，当进程中不存在非守护线程，也就是普通线程时，则守护线程将自动销毁。典型的守护线程就是垃圾回收线程，当进程中没有非守护线程了，则垃圾回收线程也就没有存在的必要了，将自动销毁。  

```java
public class MyThread extends Thread {
	private int i = 0;
	@Override
	public void run() {
			try {
				while (true) {
					i++;
					System.out.println("i = " + i);
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
```  

```java
public class TestRun {
	public static void main(String[] args) {
		try {
			MyThread thread = new MyThread();
			thread.setDaemon(true);  //设置thread线程守护当前线程
			thread.start();
			Thread.sleep(5000);
			System.out.println("当前线程结束了，守护线程也没有存在的必要了！");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
```  

运行结果如下：  
![multi-thread-10](/images/multi-thread/multi-thread-10.png)  

# 感谢

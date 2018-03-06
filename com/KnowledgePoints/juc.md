### 线程的停止

- 采用interrup来停止线程

        public class MyThread extends Thread {
            public void run(){
                super.run();
                try {
                    for(int i=0; i<500000; i++){
                        if(this.interrupted()) {
                            System.out.println("线程已经终止， for循环不再执行");
                                throw new InterruptedException();
                        }
                        System.out.println("i="+(i+1));
                    }

                    System.out.println("这是for循环外面的语句，也会被执行");
                } catch (InterruptedException e) {
                    System.out.println("进入MyThread.java类中的catch了。。。");
                    e.printStackTrace();
                }
            }
        }

        public class RunDemo {
            public static void main(String args[]){
                Thread thread = new MyThread();
                thread.start();
                try {
                    Thread.sleep(2000);
                    thread.interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

### volatile

1. 内存可见性问题

      每个线程有独立缓存，共享数据改变时先读到分线程中，修改数据后再写入主存中  

      下面代码演示一个线程中更改共享数据，另一个线程数据未变的情况  

        public class TestVolatile {
            public static void main(String[] args) {
                ThreadDemo td = new ThreadDemo();
                new Thread(td).start();
                while(true) {
                    if(td.isFlag()) {
                        System.out.println("------------");
                        break;
                    }
                }
            }
        }

        class ThreadDemo implements Runnable{
            private volatile boolean flag = false;
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                flag = true;
                System.out.println("flag=" + isFlag());
            }
            public boolean isFlag() {
                return flag;
            }
            public void setFlag(boolean flag) {
                this.flag = flag;
            }
        }

      > while效率很高，读取了当前线程缓存数据后，就没有机会再读取修改后的数据了，发生了不同步的问题
      > 只有当volatile修饰时保证其内存可见性，确保更新通知到其他线程

2. 与synchronized区别
      
      volatile不具备互斥性  

      volatile不能保证变量的原子性  

      > i++原子性问题，实际执行有读改写三步操作：int temp = i; i = i + 1; i = temp

### 原子变量  

1. volatile只能保证可见性，不能保证原子性  

      采用原子变量解决该问题  

      - CAS算法保证数据原子性，是硬件对并发操作共享数据的支持 
      - 内存值V 预估值A 更新值B，仅当V==A时，V = B，否则不做任何操作  
      
      下面代码延时了原子变量的用法  

        public class TestAtomic {

            public static void main(String[] args) {
                Atomic ac = new Atomic();
                for(int i = 0; i < 10; i++) {
                    new Thread(ac).start();
                }
            }
            
        }

        class Atomic implements Runnable{
            
            // private int serialNumber = 0;
            private AtomicInteger serialNumber = new AtomicInteger();
            
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ":" + getSerialNumber());
            }
            
            public int getSerialNumber() {
                // return serialNumber++;
                return serialNumber.getAndIncrement();
            }
            
        }      

2. 模拟CAV算法  

        public class TestCompareAndSwap {
            
            public static void main(String[] args) {
                CompareAndSwap cas = new CompareAndSwap();
                
                for(int i=0;i<10;i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int expectedValue = cas.get();
                            boolean b = cas.compareAndSet(expectedValue, (int)Math.random() * 101);
                            System.out.println(b);
                        }
                    }).start();
                }
            }

        }

        class CompareAndSwap{
            private int value;
            
            public synchronized int get() {
                return value;
            }
            
            /**
            * 
            * @param expecteValue 预估值
            * @param newValue
            * @return
            */
            public synchronized int compareAndSwap(int expecteValue, int newValue) {
                int oldValue = value;
                if(oldValue == expecteValue) {
                    this.value = newValue;
                }
                return oldValue;
            }
            
            public synchronized boolean compareAndSet(int expectedValue, int newValue) {
                return expectedValue == compareAndSwap(expectedValue, newValue);
            }
        }

### ConcurrentHashMap

- 一个线程安全的Hash表，ConcurrentHashMap介于HashMap与Hashtable之间，内部采用“锁分端”机制替代Hashtable的独占锁，进而提高性能  

- CopyOnWriteArrayList一个线程安全的ArrayList，当读数和遍历远远大于列表更新时，性能优于同步的ArrayList

- CopyOnWriteArrayList支持迭代时修改 

        public class TestCopyOnWriteArrayList {

            public static void main(String[] args) {
                HelloThread ht = new HelloThread();
                for(int i=0;i<10;i++) {
                    new Thread(ht).start();
                }
            }
            
        }

        class HelloThread implements Runnable{
            
            // 迭代时添加有异常
            // private static List<String> list = Collections.synchronizedList(new ArrayList<String>());

            // 写入时复制一个新的，所以并发迭代时选用较多。添加操作多时效率低
            private static CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
            
            static {
                list.add("AA");
                list.add("BB");
                list.add("CC");
            }
            
            @Override
            public void run() {
                Iterator<String> it = list.iterator();
                while(it.hasNext()) {
                    System.out.println(it.next());
                    list.add("aa");
                }
            }
        }

### CountDownLatch

- 闭锁：在完成某些运算时，只有其他线程所有运算全部完成，当前代码才继续  

- 等待线程的数量自己定义，为了保证执行，需要在finally中调用countDown()表示某个线程已经执行完毕  

        public class TestCountDownLatch {

            public static void main(String[] args) {
                final CountDownLatch latch = new CountDownLatch(5);
                
                long start = System.currentTimeMillis();
                
                LatchDemo ld = new LatchDemo(latch);
                for(int i=0;i<5;i++) {
                    new Thread(ld).start();
                }
                
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                System.out.println("耗时：" + (System.currentTimeMillis() - start));
                
            }
            
        }

        class LatchDemo implements Runnable{
            
            private CountDownLatch latch;
            
            public LatchDemo(CountDownLatch latch) {
                this.latch = latch;
            }
            
            @Override
            public void run() {
                synchronized(this){
                    try {
                        for(int i=0;i<50000;i++) {
                            if(i%2==0) {
                                System.out.println(i);
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                }
            }
        }

### Callable

- 与Runnable都是为那些其实例可被另一个线程执行的类设计的，但Runnable没有返回结果，不能抛出经过检查的异常 

- Callable需要依赖FutureTask，FutrueTask也可用作闭锁  

        public class TestCallable {
            
            public static void main(String[] args) {
                ThreadDemo2 td = new ThreadDemo2();
                FutureTask<Integer> result = new FutureTask<>(td);
                new Thread(result).start();
                
                try {
                    Integer sum = result.get();// 可用于闭锁操作
                    System.out.println(sum);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        }

        class ThreadDemo2 implements Callable<Integer>{

            @Override
            public Integer call() throws Exception {
                int sum = 0;
                for(int i=0;i<10000;i++) {
                    sum+=i;
                }
                return sum;
            }
            
        }       

### Lock

- 解决多线程安全问题 

      > synchronized同步代码块、同步方法
      > lock同步锁，通过lock()方法上锁，unlock()方法释放锁 
      > synchronized也叫隐式锁，lock叫显式锁
      > unlock()需要写在finllay中

        public class TestLock {

            public static void main(String[] args) {
                Ticket ticket = new Ticket();
                new Thread(ticket, "1号").start();
                new Thread(ticket, "2号").start();
                new Thread(ticket, "3号").start();
            }
            
        }

        class Ticket implements Runnable{
            private int tick = 100;
            
            private Lock lock = new ReentrantLock();
            
            @Override
            public void run() {
                while(true) {
                    
                    lock.lock();
                    
                    try {
                        if(tick > 0) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println(Thread.currentThread().getName()+"完成买票，余票："+(--tick));
                        } else {
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }      

### 生产者与消费者

1. synchronized具有互斥性和内存可见性

      - wait()后一定要有线程调用notify()或notifyAll()方法，否则会一直等待唤醒        

      - 多被唤醒线程时为了避免notifyAll唤醒时造成虚假唤醒，所以wait()方法需要放在循环中

      - notify唤醒的是其他线程，不包括自己  

            public class TestProductorAndConsumer {
                public static void main(String[] args) {
                    Clerk clerk = new Clerk();
                    Productor pro = new Productor(clerk);
                    Consumer cus = new Consumer(clerk);
                    
                    new Thread(pro, "生产者A").start();
                    new Thread(cus, "消费者A").start();
                    new Thread(pro, "生产者C").start();// 多生产者或消费者时会有虚假唤醒，为了避免虚假唤醒，应该总是使用在循环中
                    new Thread(cus, "消费者D").start();
                }
            }

            /**
            *店员
            */
            class Clerk{
                private int product = 0;
                
                // 进货
                public synchronized void get() {
                    while (product >= 1) {// 通过把if改为while，唤醒时再判断一次
                        System.out.println("产品已满！");
                        try {
                            wait();// 等待 当卖货线程提前结束，没人唤醒时就会无限等待下去
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }/*else {
                        System.out.println(Thread.currentThread().getName()+"："+ ++product);
                        notifyAll();// 通知销售
                    }*/
                    // 避免等待唤醒不了
                    System.out.println(Thread.currentThread().getName()+"："+ ++product);
                    notifyAll();// 通知销售
                }
                
                // 卖货
                public synchronized void sale() {
                    while (product<=0) {
                        System.out.println("缺货！");
                        try {
                            wait();// 等待
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }/*else {
                        System.out.println(Thread.currentThread().getName()+"："+ --product);
                        notifyAll();// 通知其他线程
                    }*/
                    System.out.println(Thread.currentThread().getName()+"："+ --product);
                    notifyAll();// 通知其他线程
                }
            }

            // 生产者
            class Productor implements Runnable{
                private Clerk clerak;
                
                public Productor(Clerk clerk) {
                    this.clerak = clerk;
                }
                
                @Override
                public void run() {
                    for(int i=0;i<20;i++) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        clerak.get();
                    }
                }
            }

            // 消费者
            class Consumer implements Runnable{
            private Clerk clerak;
                
                public Consumer(Clerk clerk) {
                    this.clerak = clerk;
                }
                
                @Override
                public void run() {
                    for(int i=0;i<20;i++) {
                        
                        clerak.sale();
                    }
                }
            }      

2. Lock

      - lock实现类ReentrantLock         
      - lock对应的控制线程通信Condition   
      - wait notify notifyAll 分别对应 await signal signalAll  

            public class TestProductorAndConsumer2 {
                public static void main(String[] args) {
                    Clerk clerk = new Clerk();
                    Productor pro = new Productor(clerk);
                    Consumer cus = new Consumer(clerk);
                    
                    new Thread(pro, "生产者A").start();
                    new Thread(cus, "消费者A").start();
                    new Thread(pro, "生产者C").start();// 多生产者或消费者时会有虚假唤醒，为了避免虚假唤醒，应该总是使用在循环中
                    new Thread(cus, "消费者D").start();
                }
            }

            //店员
            class Clerk{
                private int product = 0;
                
                private Lock lock = new ReentrantLock();
                private Condition condition = lock.newCondition();
                
                // 进货
                public void get() {
                    lock.lock();
                    
                    try {
                        while (product >= 1) {// 通过把if改为while，唤醒时再判断一次
                            System.out.println("产品已满！");
                            try {
                                condition.await();;// 等待 当卖货线程提前结束，没人唤醒时就会无限等待下去
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            System.out.println(Thread.currentThread().getName()+"："+ ++product);
                            notifyAll();// 通知销售
                        }
                        // 避免等待唤醒不了
                        System.out.println(Thread.currentThread().getName()+"："+ ++product);
                        condition.signalAll();;// 通知销售
                    } finally {
                        lock.unlock();
                    }
                    
                    
                }
                
                // 卖货
                public synchronized void sale() {
                    
                    lock.lock();
                    
                    try {
                        while (product<=0) {
                            System.out.println("缺货！");
                            try {
                                condition.await();;// 等待
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            System.out.println(Thread.currentThread().getName()+"："+ --product);
                            notifyAll();// 通知其他线程
                        }
                        System.out.println(Thread.currentThread().getName()+"："+ --product);
                        condition.signalAll();;// 通知其他线程
                    } finally {
                        lock.unlock();
                    }
                }
            }

            // 生产者
            class Productor implements Runnable{
                private Clerk clerak;
                
                public Productor(Clerk clerk) {
                    this.clerak = clerk;
                }
                
                @Override
                public void run() {
                    for(int i=0;i<20;i++) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        clerak.get();
                    }
                }
            }


            // 消费者
            class Consumer implements Runnable{
            private Clerk clerak;
                
                public Consumer(Clerk clerk) {
                    this.clerak = clerk;
                }
                
                @Override
                public void run() {
                    for(int i=0;i<20;i++) {
                        
                        clerak.sale();
                    }
                }
            }

### 线程交替  

- 编写一个线程开启3个线程，按顺序打印ABC

- 线程通信需要三个控制

        private Condition condition1 = lock.newCondition();
        private Condition condition2 = lock.newCondition();
        private Condition condition3 = lock.newCondition();            

            public class TestABCAlternate {

                public static void main(String[] args) {
                    AlternateDemo a = new AlternateDemo();
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            for (int i = 1; i < 20; i++) {
                                a.loopA(i);
                            }
                        }
                    }, "A").start();
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            for (int i = 1; i < 20; i++) {
                                a.loopB(i);
                            }
                        }
                    }, "B").start();
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            for (int i = 1; i < 20; i++) {
                                a.loopC(i);
                                System.out.println("---------------------------------");
                            }
                        }
                    }, "C").start();
                }

            }

            class AlternateDemo {
                private int number = 1;// 当前执行线程标记

                private Lock lock = new ReentrantLock();
                private Condition condition1 = lock.newCondition();
                private Condition condition2 = lock.newCondition();
                private Condition condition3 = lock.newCondition();

                /**
                * 
                * @param totalLoop
                *            第几轮
                */
                public void loopA(int totalLoop) {
                    lock.lock();

                    try {
                        // 判断
                        if (number != 1) {
                            condition1.await();
                        }
                        // 打印
                        for (int i = 1; i <= 5; i++) {
                            System.out.println(Thread.currentThread().getName() + "\t" + i + "\t" + totalLoop);
                        }
                        // 唤醒
                        number = 2;
                        condition2.signal();
                    } catch (Exception e) {

                    } finally {
                        lock.unlock();
                    }
                }

                public void loopB(int totalLoop) {
                    lock.lock();

                    try {
                        // 判断
                        if (number != 2) {
                            condition2.await();
                        }
                        // 打印
                        for (int i = 1; i <= 15; i++) {
                            System.out.println(Thread.currentThread().getName() + "\t" + i + "\t" + totalLoop);
                        }
                        // 唤醒
                        number = 3;
                        condition3.signal();
                    } catch (Exception e) {

                    } finally {
                        lock.unlock();
                    }
                }

                public void loopC(int totalLoop) {
                    lock.lock();

                    try {
                        // 判断
                        if (number != 3) {
                            condition3.await();
                        }
                        // 打印
                        for (int i = 1; i <= 20; i++) {
                            System.out.println(Thread.currentThread().getName() + "\t" + i + "\t" + totalLoop);
                        }
                        // 唤醒
                        number = 1;
                        condition1.signal();
                    } catch (Exception e) {

                    } finally {
                        lock.unlock();
                    }
                }
            }        

### ReadWriteLock

- 读写锁 写写互斥、读写互斥、读读不互斥  

        public class TestReadWriteLock {

            public static void main(String[] args) {
                ReadWriteLockDemo rw = new ReadWriteLockDemo();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        rw.set((int)(Math.random() * 101));
                    }
                }, "write:").start();
                
                for(int i = 0;i<20;i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            rw.get();
                        }
                    }).start();
                }
            }
            
        }

        class ReadWriteLockDemo{
            private int number = 0;
            
            private ReadWriteLock lock = new ReentrantReadWriteLock();
            
            // 读
            public void get() {
                lock.readLock().lock();
                try {
                    System.out.println(Thread.currentThread().getName() +":"+ number);
                } catch (Exception e) {
                } finally {
                    lock.readLock().unlock();
                }
            }
            // 写
            public void set(int number) {
                lock.writeLock().lock();
                try {
                    System.out.println(Thread.currentThread().getName());
                    this.number = number;
                } catch (Exception e) {
                } finally {
                    lock.writeLock().unlock();
                }
            }
        }            
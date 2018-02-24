### RxJava中泛型

- interface OnSubscrible<T> extends Action1<Subscrible<? super T>>  
    可以同时现实一个对象，以及持有这个对象的容器  

- super 表示参数   extends 表示返回值

- Observable中初始化被观察者对象，创建观察者对象。只有订阅时才会执行订阅流程

### 事件转换 

- 调用map时创建转换后的被观察者，这个被观察者对象中处理转换逻辑

- 转换抽象为Func1接口

        public interface Func1<T, R> {

            R call(T t);

        } 

        .map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                System.out.println(s);
                return 5;
            }
        })   

        String --> Integer  
        T --> R  
        接收到转换后的数据  

- map中将数据传递给观察者Subscrible        

        OperatorMap中
        // 转换后返回需要的T
        @Override
        public Subscrible<? super T> call(Subscrible<? super R> subscrible)

        R r = transform.call(t);
        actual.onNext(r);

- OnSubscribleLeft中间被观察者调用call，实际调用了OperatorMap中的call获取观察者

- OnSubscribleLeft最终调用了上面获取的观察者


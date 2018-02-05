### 重试机制

- 主要代码

        model.checkBind(this, getUserService())
                .flatMap(new Function<NetEaseCodeResult, ObservableSource<NetEaseCodeResult>>() {
                    @Override
                    public ObservableSource<NetEaseCodeResult> apply(NetEaseCodeResult netEaseCodeResult) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<NetEaseCodeResult>() {
                            @Override
                            public void subscribe(ObservableEmitter<NetEaseCodeResult> e) throws Exception {
                                System.out.println("===========================判断结果");
                                if (result) {
                                    System.out.println("===========================下一步");
                                    e.onNext(netEaseCodeResult);
                                } else {
                                    System.out.println("===========================异常重试");
                                    result = true;
                                    e.onError(new Throwable("111"));
                                }
                            }
                        });
                    }
                })
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {

                    private int i = 0;

                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> objectObservable) throws Exception {
                        return objectObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                System.out.println("===========================确认when");
                                if (throwable.getMessage().equals("111") && i == 0){
                                    System.out.println("===========================100ms 重试");
                                    i++;
                                    return Observable.timer(100, TimeUnit.MILLISECONDS);
                                }else{
                                    System.out.println("===========================非重试异常");
                                    return Observable.error((Throwable) throwable);
                                }
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FavObserver<NetEaseCodeResult>(){
                    @Override
                    public void onNext(NetEaseCodeResult netEaseCodeResult) {
                        super.onNext(netEaseCodeResult);
                        System.out.println("===========================onNext");
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        System.out.println("===========================onError");
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        System.out.println("===========================onComplete");
                    }
                });

- 执行结果  
I/System.out: ===========================执行内容  
I/System.out: ===========================判断结果  
I/System.out: ===========================异常重试  
I/System.out: ===========================确认when  
I/System.out: ===========================100ms 重试  
I/System.out: ===========================执行内容  
I/System.out: ===========================判断结果  
I/System.out: ===========================下一步  
I/System.out: ===========================onNext  

- 另一种写法，在变量处理上有问题，需要修改

        public class LoginModel {

            private ApiService apiService;
            private Disposable disposable;
            // 缓存数据
            private ApiResponse<List<MobileInfo>> cache;

            public LoginModel(ApiService apiService) {
                this.apiService = apiService;
            }

            public void mobileVerify(String mobile, final CallbackListener<List<MobileInfo>> listener){
                apiService.mobileVerify(mobile)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(new Consumer<ApiResponse<List<MobileInfo>>>() {
                        @Override
                        public void accept(ApiResponse<List<MobileInfo>> listApiResponse) throws Exception {
                            if(listApiResponse.stat == 1) {

                                if(listApiResponse.data != null && listApiResponse.data.size() > 0 && listApiResponse.data.get(0).type > 2){
                                    // 外包 出差
                                    cache = listApiResponse;
                                }else{
                                    if(disposable != null){
                                        disposable.dispose();
                                    }
                                    listener.onSuccess(listApiResponse.data);
                                }
                            }
                        }
                    })
                    .observeOn(Schedulers.io())
                    .flatMap(new Function<ApiResponse<List<MobileInfo>>, ObservableSource<ApiResponse>>() {
                        @Override
                        public ObservableSource<ApiResponse> apply(ApiResponse<List<MobileInfo>> listApiResponse) throws Exception {
                            return apiService.updateStatus(listApiResponse.data.get(0).orderid, listApiResponse.data.get(0).visitnumber);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiSubscriber<ApiResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onNext(ApiResponse value) {
                            super.onNext(value);
                            if(value.stat == 1) {
                                listener.onSuccess(cache.data);
                            }
                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            listener.onError();
                        }
                    });
            }

        }

关于重订阅的一些说明，虽然是rxjava1的说明但对于rxjava2仍然适用  
- 由于每一个error都被flatmap过，因此我们不能通过直接调用.onNext(null)触发重订阅或者.onError(error)来避免重订阅  
- 输入的Observable必须作为输出Observable的源。你必须对Observable<Throwable>做出反应，然后基于它发送事件；你不能只返回一个通用泛型流，如下写法  

        .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override public Observable<?> call(Observable<? extends Throwable> errors) {
                return Observable.just(null);}
            })


### compose消除重复代码

- compose()需要传入一个Observable.Transformer类型的参数  
- 创建Observable.Transformer对象，实现需要重复的代码  

- 一个线程切换的示例

        public static <T> ObservableTransformer<T, T> applySchedulers() {
            return new ObservableTransformer<T, T>() {
                public ObservableSource<T> apply(Observable<T> upstream) {
                    return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                }
            };
        }


### 多数据来源处理concat+first

- 可以实现多数据来源时，有一方返回数据即停止，执行是按参数顺序进行  
- rxjava1和2是有区别的，只有调用了onComplete才会执行下一个来源数据，只要调用了onNext就不会再执行后面的数据
- 另外异常如果不能捕获，那么整个数据获取就失败了，不会进入下一个

        public static void testConcat() {
            //final String[] data = {null, null, "network"};
            final String[] data = {"memory", "disk", "network"};
            Observable<String> memorySource = Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> subscriber) throws Exception{
                    System.out.println("===========1 ");
                    String d = data[0];
                    SystemClock.sleep(1000);
                    data[4] = "11"; // 模拟出错
                    if (d != null) {
                        System.out.println("===========1 onNext");
                        subscriber.onNext(d); // 有onNext不会进入下一轮
                    }
                    System.out.println("===========1 onCompleted");
                    subscriber.onComplete(); // 没有completed不会进入下一轮
                }
            }).subscribeOn(Schedulers.io());
            Observable<String> diskSource = Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> subscriber) {
                    System.out.println("===========2 ");
                    String d = data[1];
                    SystemClock.sleep(1000);
                    if (d != null) {
                        System.out.println("===========2 onNext");
                        subscriber.onNext(d);
                    }
                    System.out.println("===========2 onCompleted");
                    subscriber.onComplete();
                }
            }).subscribeOn(Schedulers.io());
            Observable<String> networkSource = Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> subscriber) {
                    System.out.println("===========3 ");
                    String d = data[2];
                    if (d != null) {
                        System.out.println("===========3 onNext");
                        subscriber.onNext(d);
                    }
                    System.out.println("===========3 onCompleted");
                    subscriber.onComplete();
                }
            }).subscribeOn(Schedulers.io());
            Observable.concat(memorySource, diskSource, networkSource)
                    .firstElement()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            System.out.println("Getting data from " + s);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                            System.out.println("Error: " + throwable.getMessage());
                        }
                    });
        }


        public static void testConcat() {
            final String[] data = {null, null, "network"};
            // final String[] data = {"memory", "disk", "network"};
            Observable<String> memorySource = Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    System.out.println("===========1 ");
                    String d = data[0];
                    SystemClock.sleep(1000);
                    // data[4] = "11"; 模拟出错
                    if (d != null) {
                        System.out.println("===========1 onNext");
                        subscriber.onNext(d); // 有onNext不会进入下一轮
                    }
                    System.out.println("===========1 onCompleted");
                    subscriber.onCompleted(); // 没有completed不会进入下一轮
                }
            }).subscribeOn(Schedulers.io());
            Observable<String> diskSource = Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    System.out.println("===========2 ");
                    String d = data[1];
                    SystemClock.sleep(1000);
                    if (d != null) {
                        System.out.println("===========2 onNext");
                        subscriber.onNext(d);
                    }
                    System.out.println("===========2 onCompleted");
                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.io());
            Observable<String> networkSource = Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    System.out.println("===========3 ");
                    String d = data[2];
                    if (d != null) {
                        System.out.println("===========3 onNext");
                        subscriber.onNext(d);
                    }
                    System.out.println("===========3 onCompleted");
                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.io());
            Observable.concat(memorySource, diskSource, networkSource)
                    .first()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            System.out.println("Getting data from " + s);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            System.out.println("Error: " + throwable.getMessage());
                        }
                    });
        }

### 取消订阅错误捕获

        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtil.i("RxJavaPlugins error :", throwable);
            }
        });        


### 轮询任务异常处理

        Observable.interval(MagicNumberConstant.FIVE, TimeUnit.SECONDS, Schedulers.io()).flatMap(new Function<Long, ObservableSource<NetEaseSongListResult>>() {

            @Override
            public ObservableSource<NetEaseSongListResult> apply(Long aLong) throws Exception {
                return mFavoriteModel.getNetEaseSongList(mContext, mMainActivity.getIUserAccountService(), 0, false).delaySubscription(MagicNumberConstant.ONE_HUNDRED, TimeUnit.MILLISECONDS).
                    /*doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            System.out.println("====================111");
                        }
                    }).*/onErrorReturn(new Function<Throwable, NetEaseSongListResult>() {
                        @Override
                        public NetEaseSongListResult apply(Throwable throwable) throws Exception {
                            System.out.println("====================222");
                            return new NetEaseSongListResult();
                        }
                });
            }
        }).subscribe(new FavObserver<NetEaseSongListResult>(){
            @Override
            public void onNext(NetEaseSongListResult netEaseSongListResult) {
                System.out.println("===========================3333");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("==========================4444");
            }

            @Override
            public void onComplete() {
                System.out.println("===========================555");
            }
        });        
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
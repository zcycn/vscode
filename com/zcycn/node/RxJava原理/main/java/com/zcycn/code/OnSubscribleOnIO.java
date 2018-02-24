package com.zcycn.code;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>Class: com.zcycn.code.OnSubscribleOnIO</p>
 * <p>Description: </p>
 * <pre>
 * 线程切换中间类
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/2/23/20:03
 */

public class OnSubscribleOnIO<T> implements OnSubscrible<T> {

    private static ExecutorService executorService = Executors.newCachedThreadPool();
    Observable<T> source;

    public OnSubscribleOnIO(Observable<T> source) {
        this.source = source;
    }

    @Override
    public void call(final Subscrible<? super T> subscrible) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                source.subscrible(subscrible);
            }
        };
        executorService.submit(runnable);
    }
}

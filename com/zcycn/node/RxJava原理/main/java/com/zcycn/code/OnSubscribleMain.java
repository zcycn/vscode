package com.zcycn.code;

import android.os.Handler;

/**
 * <p>Class: com.zcycn.code.OnSubscribleMain</p>
 * <p>Description: </p>
 * <pre>
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/2/23/20:14
 */

public class OnSubscribleMain<T> implements OnSubscrible<T> {

    private Handler handler;
    private Observable<T> source;

    public OnSubscribleMain(Handler handler, Observable<T> source) {
        this.handler = handler;
        this.source = source;
    }

    @Override
    public void call(final Subscrible<? super T> subscrible) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                source.subscrible(subscrible);
            }
        });
    }
}

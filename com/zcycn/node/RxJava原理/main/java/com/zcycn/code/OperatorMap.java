package com.zcycn.code;

/**
 * <p>Class: com.zcycn.code.OperatorMap</p>
 * <p>Description: </p>
 * <pre>
 * 具体中间转换
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/2/23/12:47
 */

public class OperatorMap<T, R> implements Operator<R, T>{

    Func1<? super T, ? extends R> transform;

    public OperatorMap(Func1<? super T, ? extends R> transform) {
        this.transform = transform;
    }

    // 转换后返回需要的T
    @Override
    public Subscrible<? super T> call(Subscrible<? super R> subscrible) {
        return new MapSuscrible<>(subscrible, transform);
    }

    // 最终观察者
    private class MapSuscrible<T, R> extends Subscrible<T>{

        private Subscrible<? super R> actual;
        private Func1<? super T, ? extends R> transform;

        public MapSuscrible(Subscrible<? super R> actual, Func1<? super T, ? extends R> transform) {
            this.actual = actual;
            this.transform = transform;
        }

        @Override
        public void onNext(T t) {
            R r = transform.call(t);
            actual.onNext(r);
        }
    }
}

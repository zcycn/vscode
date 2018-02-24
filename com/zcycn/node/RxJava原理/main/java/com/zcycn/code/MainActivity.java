package com.zcycn.code;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable.create(new OnSubscrible<String>() {
            @Override
            public void call(Subscrible<? super String> subscrible) {
                subscrible.onNext("hello rxJava");
            }
        }).subscribleOnIO().subscribleMain().subscrible(new Subscrible<String>() {
            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        });

        Observable.create(new OnSubscrible<String>() {
            @Override
            public void call(Subscrible<? super String> subscrible) {
                subscrible.onNext("rxJava Map");
            }
        }).map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                System.out.println(s);
                return 5;
            }
        }).subscrible(new Subscrible<Integer>() {
            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }
        });
    }
}

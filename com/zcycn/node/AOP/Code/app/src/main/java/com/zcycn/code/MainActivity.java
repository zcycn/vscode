package com.zcycn.code;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 *
 *
 */
public class MainActivity extends AppCompatActivity {

    final String str = "你好吗";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shake();
            }
        });
    }


    @BehaviorTrace(value = str, type = 0)
    public String shake(){
        System.out.println("========================");
        return "我是返回值";
    }
}

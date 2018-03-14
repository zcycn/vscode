package com.zcycn.code;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zcycn.code.present.BasePersenter;
import com.zcycn.code.view.BaseView;

/**
 * <p>Class: com.zcycn.code.BaseActivity</p>
 * <p>Description: </p>
 * <pre>
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/3/14/9:51
 */

public abstract class BaseActivity<V extends BaseView, T extends BasePersenter<V>> extends AppCompatActivity{

    protected T mPresent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresent = createPresent();
        mPresent.attachView((V) this);
    }

    // 子类实现具体的构建过程
    protected abstract T createPresent();

    @Override
    protected void onDestroy() {
        mPresent.detach();
        super.onDestroy();
    }
}

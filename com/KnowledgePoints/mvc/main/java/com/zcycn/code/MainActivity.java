package com.zcycn.code;

import android.os.Bundle;
import android.widget.ListView;

import com.zcycn.code.adapter.GirlListAdapter;
import com.zcycn.code.bean.Girl;
import com.zcycn.code.present.GirlPresent;
import com.zcycn.code.view.IGirlView;

import java.util.List;

// 规范是主观的不足，架构是客观需求的不足
public class MainActivity extends BaseActivity<IGirlView, GirlPresent<IGirlView>> implements IGirlView{

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.listview);
        mPresent.fectch();
    }

    @Override
    protected GirlPresent<IGirlView> createPresent() {
        return new GirlPresent<>();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showGirls(List<Girl> girls) {
        mListView.setAdapter(new GirlListAdapter(this, girls));
    }
}

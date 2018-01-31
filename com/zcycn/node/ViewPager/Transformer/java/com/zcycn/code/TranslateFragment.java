package com.zcycn.code;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>Class: com.zcycn.code.TranslateFragment</p>
 * <p>Description: </p>
 * <pre>
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/1/30/21:32
 */

public class TranslateFragment extends Fragment {

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        int layoutId = bundle.getInt("layoutId");
        int pageIndex = bundle.getInt("pageIndex");

        View view = inflater.inflate(layoutId, null);
        view.setTag(pageIndex);
        return view;
    }

}

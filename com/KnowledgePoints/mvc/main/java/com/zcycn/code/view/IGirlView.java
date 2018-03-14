package com.zcycn.code.view;

import com.zcycn.code.bean.Girl;

import java.util.List;

/**
 * <p>Class: com.zcycn.code.view.IGrilView</p>
 * <p>Description: </p>
 * <pre>
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/3/13/20:01
 */

public interface IGirlView extends BaseView{

    void showLoading();

    void showGirls(List<Girl> girls);

}

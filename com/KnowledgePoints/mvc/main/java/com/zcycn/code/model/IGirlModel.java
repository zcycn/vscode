package com.zcycn.code.model;

import com.zcycn.code.bean.Girl;

import java.util.List;

/**
 * <p>Class: com.zcycn.code.model.IGirlModel</p>
 * <p>Description: </p>
 * <pre>
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/3/13/20:06
 */

public interface IGirlModel {

    void loadGirl(GirlOnLoadListener girlOnLoadListener);

    interface GirlOnLoadListener{
        void onComplete(List<Girl> girls);
    }

}

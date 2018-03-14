package com.zcycn.code.present;

import com.zcycn.code.bean.Girl;
import com.zcycn.code.model.GirlModel;
import com.zcycn.code.model.IGirlModel;
import com.zcycn.code.view.IGirlView;

import java.util.List;

/**
 * <p>Class: com.zcycn.code.present.GirlPresent</p>
 * <p>Description: </p>
 * <pre>
 *
 * </pre>
 *
 * @author zhuchengyi
 * @date 2018/3/13/20:00
 */

public class GirlPresent<T> extends BasePersenter<IGirlView>{

    IGirlModel mIGirlModel = new GirlModel();

    @Override
    public void fectch(){
        if (mViewRef != null && mViewRef.get() != null) {
            IGirlView mIGirlView = mViewRef.get();
            mIGirlView.showLoading();
        }
        if(mIGirlModel != null){
            mIGirlModel.loadGirl(new IGirlModel.GirlOnLoadListener() {
                @Override
                public void onComplete(List<Girl> girls) {
                    if (mViewRef != null && mViewRef.get() != null) {
                        IGirlView mIGirlView = mViewRef.get();
                        mIGirlView.showGirls(girls);
                    }
                }
            });
        }
    }

}

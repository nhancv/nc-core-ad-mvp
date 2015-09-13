package com.cvnhan.androidcr.mvp.presenter;

import android.util.Log;

import com.cvnhan.androidcr.mvp.model.YKMAModelRadio;
import com.cvnhan.androidcr.mvp.view.RadioView;
import com.cvnhan.core.utils.NCMCUtilRxHelper;

import rx.Subscription;

/**
 * Created by NhanCao on 13-Sep-15.
 */

/**
 * Created by NhanCao on 19-Jul-15.
 */
public class YKMAPresenterRadio implements YKMAIPresenterBase<RadioView> {
    private final String TAG = YKMAPresenterRadio.class.getSimpleName();
    private final YKMAModelRadio model;
    private Subscription subscription;

    public YKMAPresenterRadio(YKMAModelRadio model) {
        this.model = model;
    }

    @Override
    public void onStart(RadioView view) {
        subscription = model.getAllRadio()
                .compose(NCMCUtilRxHelper.applySchedulers())
                .subscribe(
                        dbRadios -> {
                            view.render(dbRadios);
                        },
                        e -> {
                            Log.e(TAG, e.toString());
                            e.printStackTrace();
                        });
    }

    @Override
    public void onStop() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}

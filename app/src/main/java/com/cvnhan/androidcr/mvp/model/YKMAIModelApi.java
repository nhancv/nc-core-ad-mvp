package com.cvnhan.androidcr.mvp.model;

import com.cvnhan.androidcr.mvp.model.remote.MRRadioRes;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by NhanCao on 13-Sep-15.
 */
public interface YKMAIModelApi {
    @GET("/pol/controller/get_list.php")
    Observable<MRRadioRes> getAllRadio();
}

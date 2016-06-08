package com.cvnhan.androidcr.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cvnhan.androidcr.MyApp;
import com.cvnhan.androidcr.R;
import com.cvnhan.androidcr.ui.activity.ActivityTitleController;
import com.cvnhan.androidcr.ui.activity.MainActivity;

import javax.inject.Inject;

import butterknife.OnClick;

public class HomeFragment extends FragmentBase {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Inject
    ActivityTitleController titleController;

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).component().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        titleController.setTitle("Home Fragment");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @OnClick(R.id.btnHomeFragment)
    public void btnHomeFragmentOnClick() {
        Log.e(TAG, "btnHomeFragmentOnClick: ");
        ((MyApp) getActivity().getApplication()).getApplicationBus().post("test");
    }

}

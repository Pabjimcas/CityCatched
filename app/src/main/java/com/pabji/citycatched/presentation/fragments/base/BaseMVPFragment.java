package com.pabji.citycatched.presentation.fragments.base;

import android.os.Bundle;
import android.widget.Toast;

import com.pabji.citycatched.domain.scopes.HasComponent;
import com.pabji.citycatched.presentation.mvp.presenters.base.BasePresenter;
import com.pabji.citycatched.presentation.mvp.views.base.BaseView;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

/**
 * Created by pabji on 16/06/2016.
 */
public abstract class BaseMVPFragment<P extends BasePresenter<V>, V extends BaseView> extends MvpFragment<V,P> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    public void showError(int error) {
        Toast.makeText(getActivity(),"Error: "+error,Toast.LENGTH_SHORT).show();
    }

    public void goBack(){
        getActivity().getSupportFragmentManager().popBackStack();
    }
}

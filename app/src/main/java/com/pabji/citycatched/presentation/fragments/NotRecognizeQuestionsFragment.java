package com.pabji.citycatched.presentation.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pabji.citycatched.R;
import com.pabji.citycatched.presentation.activities.NoRecognizedActivity;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.adapters.NoRecognizedAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Pablo Jiménez Casado on 04/08/2016.
 */

public class NotRecognizeQuestionsFragment extends Fragment{

    @BindView(R.id.tv_question)
    TextView tvQuestion;

    @BindView(R.id.iv_no_recognized)
    ImageView imageView;

    private Unbinder unbind;


    public static NotRecognizeQuestionsFragment newInstance(String question) {
        NotRecognizeQuestionsFragment frag = new NotRecognizeQuestionsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("question",question);
        frag.setArguments(bundle);
        return frag;
    }

    @Inject
    public NotRecognizeQuestionsFragment() {
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_recognized_questions,container,false);
        unbind = ButterKnife.bind(this,view);
        tvQuestion.setText(getArguments().getString("question"));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    @OnClick(R.id.btn_no)
    public void clickNo(){
        ((NoRecognizedActivity)getActivity()).getPresenter().clickNo();
    }

    @OnClick(R.id.btn_yes)
    public void clickYes(){
        ((NoRecognizedActivity)getActivity()).getPresenter().clickYes();
    }
}



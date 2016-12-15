package com.pabji.citycatched.presentation.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.components.BuildingCommentsComponent;
import com.pabji.citycatched.domain.components.DaggerBuildingCommentsComponent;
import com.pabji.citycatched.domain.modules.BuildingCommentsModule;
import com.pabji.citycatched.domain.scopes.HasComponent;
import com.pabji.citycatched.presentation.activities.base.BaseMVPActivity;
import com.pabji.citycatched.presentation.adapters.BuildingCommentsAdapter;
import com.pabji.citycatched.presentation.mvp.models.Comment;
import com.pabji.citycatched.presentation.mvp.presenters.BuildingCommentsPresenter;
import com.pabji.citycatched.presentation.mvp.views.BuildingCommentsView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BuildingCommentsActivity extends BaseMVPActivity<BuildingCommentsPresenter,BuildingCommentsView> implements BuildingCommentsView,HasComponent<BuildingCommentsComponent> {

    private BuildingCommentsComponent component;

    private Unbinder unbind;
    private String buildingId;

    private String buildingImage;
    private String buildingName;

    @BindView(R.id.rv_comments)
    RecyclerView rvComments;

    @BindView(R.id.et_comment)
    TextInputLayout etComment;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    BuildingCommentsAdapter commentsAdapter;
    private AlertDialog alertDialog;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, BuildingCommentsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.initializeInjector();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_comments);
        unbind = ButterKnife.bind(this);
        buildingImage = getIntent().getStringExtra("buildingImage");
        rvComments.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvComments.setAdapter(commentsAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadComments();
            }
        });


        presenter.init(this);
    }


    @NonNull
    @Override
    public BuildingCommentsPresenter createPresenter() {
        return component.presenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
    }

    @Override
    public BuildingCommentsComponent getComponent() {
        return component;
    }

    private void initializeInjector() {
        this.component = DaggerBuildingCommentsComponent.builder()
                .cityCatchedApplicationComponent(getInjector())
                .buildingCommentsModule(new BuildingCommentsModule(this))
                .build();
        this.component.inject(this);
    }

    @Override
    public void showComments(List<Comment> commentList) {
        commentsAdapter.setData(commentList);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showBuildingRatingDialog() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_rating, null);
        RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.rating);

        final TextView mark = (TextView)dialogView.findViewById(R.id.mark);

        ratingBar.setRating(3);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mark.setText(String.valueOf(rating));
            }
        });

        Button btn = (Button) dialogView.findViewById(R.id.btn_dialog);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendComment(Float.parseFloat(mark.getText().toString()));
                alertDialog.dismiss();
            }
        });

        alert.setView(dialogView);


        alertDialog = alert.create();
        alertDialog.show();

    }

    @OnClick(R.id.btn_send_comment)
    public void sendComment(){
        presenter.makeAComment(etComment.getEditText().getText().toString());
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etComment.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        etComment.clearFocus();
        etComment.getEditText().setText("");
    }
}

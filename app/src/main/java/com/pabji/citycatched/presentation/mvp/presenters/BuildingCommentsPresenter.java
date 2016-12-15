package com.pabji.citycatched.presentation.mvp.presenters;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pabji.citycatched.domain.features.GetBuildingCommentsInteractor;
import com.pabji.citycatched.domain.features.SendBuildingCommentInteractor;
import com.pabji.citycatched.presentation.mvp.models.Comment;
import com.pabji.citycatched.presentation.mvp.presenters.base.BasePresenter;
import com.pabji.citycatched.presentation.mvp.views.BuildingCommentsView;
import com.pabji.citycatched.presentation.navigation.Router;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class BuildingCommentsPresenter extends BasePresenter<BuildingCommentsView> {

    @Inject
    Router router;

    @Inject
    GetBuildingCommentsInteractor getCommentsInteractor;

    @Inject
    SendBuildingCommentInteractor sendCommentInteractor;

    Context context;

    private List<Comment> commentList = new ArrayList<>();
    private FirebaseUser mAuth;
    private String commentId;
    private String description;
    private int page;

    @Inject
    public BuildingCommentsPresenter() {
    }

    public void init(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        loadComments();
    }

    public void loadComments() {
        getCommentsInteractor.execute(new Subscriber<List<Comment>>() {
            @Override
            public void onCompleted() {
                getView().showComments(commentList);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Comment> comments) {
                commentList = comments;
            }
        });
    }

    public void makeAComment(String description) {

        getView().showBuildingRatingDialog();
        this.description = description;
    }

    public void sendComment(float rating){
        Comment comment = new Comment();
        Date date = new Date();
        comment.setDate(date.getTime());
        comment.setUsername(mAuth.getDisplayName());
        comment.setUserId(mAuth.getUid());
        comment.setMark(rating);
        comment.setUserImage(mAuth.getPhotoUrl().toString());
        comment.setDescription(description);
        sendCommentInteractor.execute(mAuth.getUid(),comment, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                loadComments();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String id) {
                commentId = id;
            }
        });
    }
}

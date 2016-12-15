package com.pabji.citycatched.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.components.DaggerRevisionListComponent;
import com.pabji.citycatched.domain.scopes.HasComponent;
import com.pabji.citycatched.presentation.activities.base.BaseActivity;
import com.pabji.citycatched.domain.components.RevisionListComponent;
import com.pabji.citycatched.domain.modules.RevisionListModule;
import com.pabji.citycatched.presentation.fragments.BuildingListVisitedFragment;

public class RevisionListActivity extends BaseActivity implements HasComponent<RevisionListComponent> {

    private RevisionListComponent component;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, RevisionListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision_list);
        this.initializeInjector();
        this.initializeActivity();
    }

    @Override
    public RevisionListComponent getComponent() {
        return component;
    }

    private void initializeActivity() {
        addFragment(R.id.content, BuildingListVisitedFragment.newInstance());
    }

    private void initializeInjector() {
        this.component = DaggerRevisionListComponent.builder()
                .cityCatchedApplicationComponent(getInjector())
                .revisionListModule(new RevisionListModule(this))
                .build();
    }

}

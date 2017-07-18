package net.berserkir.gsmeac.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import net.berserkir.gsmeac.R;
import net.berserkir.gsmeac.database.helper.BriefHelper;
import net.berserkir.gsmeac.database.model.Brief;

public class BriefViewerActivity extends GSMEACActivity {

    private static final String EXTRA_BRIEF_ID = "brief";

    private long mBriefId;

    private Toolbar mToolbar;
    private TextView mNameEditText;
    private TextView mGroundEditText;
    private TextView mSituationEditText;
    private TextView mMissionEditText;
    private TextView mExecutionEditText;
    private TextView mAdministrationAndLogisticsEditText;
    private TextView mCommandAndSignalsEditText;

    public static Intent intentForBriefId(Context context, long briefId) {
        Intent intent = new Intent(context, BriefViewerActivity.class);
        intent.putExtra(EXTRA_BRIEF_ID, briefId);
        return intent;
    }

    @Override
    protected void onResume() {

        super.onResume();

        // refetch brief when activity is resumed
        fetchBrief();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // setup layout
        setContentView(R.layout.activity_brief_viewer);

        // get brief id if provided by intent
        mBriefId = getIntent().getLongExtra(EXTRA_BRIEF_ID, 0);

        // get reference to views
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNameEditText = (TextView) findViewById(R.id.nameTextView);
        mGroundEditText = (TextView) findViewById(R.id.groundTextView);
        mSituationEditText = (TextView) findViewById(R.id.situationTextView);
        mMissionEditText = (TextView) findViewById(R.id.missionTextView);
        mExecutionEditText = (TextView) findViewById(R.id.executionTextView);
        mAdministrationAndLogisticsEditText = (TextView) findViewById(R.id.administrationAndLogisticsTextView);
        mCommandAndSignalsEditText = (TextView) findViewById(R.id.commandAndSignalsTextView);

        // set toolbar
        setSupportActionBar(mToolbar);

        // show back button in toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // fetch and show the brief
        fetchBrief();

    }

    private void initFromBrief(Brief brief) {
        if (brief != null) {
            mBriefId = brief.getId();
            mNameEditText.setText(brief.getName());
            mGroundEditText.setText(brief.getGround());
            mSituationEditText.setText(brief.getSituation());
            mMissionEditText.setText(brief.getMission());
            mExecutionEditText.setText(brief.getExecution());
            mAdministrationAndLogisticsEditText.setText(brief.getAdministrationAndLogistics());
            mCommandAndSignalsEditText.setText(brief.getCommandAndSignals());
        }
    }

    private void fetchBrief() {

        // if brief id was provided by intent, fetch brief
        if (mBriefId != 0) {

            // fetch brief from database by id
            Brief brief = BriefHelper.getInstance().getBriefById(mBriefId);

            // init from database brief
            initFromBrief(brief);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu
        getMenuInflater().inflate(R.menu.activity_brief_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_edit: {

                // launch brief editor
                startActivity(BriefEditorActivity.intentForBriefId(mContext, mBriefId));

                return true;

            }

        }

        return super.onOptionsItemSelected(item);

    }

}
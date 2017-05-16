package nz.org.cadetforces.gsmeac.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import nz.org.cadetforces.gsmeac.R;
import nz.org.cadetforces.gsmeac.database.helper.BriefHelper;
import nz.org.cadetforces.gsmeac.database.model.Brief;

public class BriefEditorActivity extends GSMEACActivity implements TextWatcher {

    private static final String EXTRA_BRIEF_ID = "brief";

    private long mBriefId;
    private boolean mHasEditedFields;

    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;

    private ImageView mNameInfoButton;
    private EditText mNameEditText;

    private ImageView mGroundInfoButton;
    private EditText mGroundEditText;

    private ImageView mSituationInfoButton;
    private EditText mSituationEditText;

    private ImageView mMissionInfoButton;
    private EditText mMissionEditText;

    private ImageView mExecutionInfoButton;
    private EditText mExecutionEditText;

    private ImageView mAdministrationAndLogisticsInfoButton;
    private EditText mAdministrationAndLogisticsEditText;

    private ImageView mCommandAndSignalsInfoButton;
    private EditText mCommandAndSignalsEditText;

    public static Intent intentForBriefId(Context context, long briefId){
        Intent intent = new Intent(context, BriefEditorActivity.class);
        intent.putExtra(EXTRA_BRIEF_ID, briefId);
        return intent;
    }

    @Override
    public void onBackPressed() {

        // show confirm exit dialog if user has edited fields
        if(mHasEditedFields){

            MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
            builder.title(R.string.title_confirm);
            builder.content(R.string.info_exit_unsaved_changed);
            builder.positiveText(R.string.action_exit);
            builder.negativeText(R.string.action_cancel);
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    BriefEditorActivity.super.onBackPressed();
                }
            });
            showDialog(builder.build());

        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // setup layout
        setContentView(R.layout.activity_brief_editor);

        // get brief id if provided by intent
        mBriefId = getIntent().getLongExtra(EXTRA_BRIEF_ID, 0);

        // get reference to views
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);

        mNameInfoButton = (ImageView) findViewById(R.id.nameInfoButton);
        mNameEditText = (EditText) findViewById(R.id.nameEditText);

        mGroundInfoButton = (ImageView) findViewById(R.id.groundInfoButton);
        mGroundEditText = (EditText) findViewById(R.id.groundEditText);

        mSituationInfoButton = (ImageView) findViewById(R.id.situationInfoButton);
        mSituationEditText = (EditText) findViewById(R.id.situationEditText);

        mMissionInfoButton = (ImageView) findViewById(R.id.missionInfoButton);
        mMissionEditText = (EditText) findViewById(R.id.missionEditText);

        mExecutionInfoButton = (ImageView) findViewById(R.id.executionInfoButton);
        mExecutionEditText = (EditText) findViewById(R.id.executionEditText);

        mAdministrationAndLogisticsInfoButton = (ImageView) findViewById(R.id.administrationAndLogisticsInfoButton);
        mAdministrationAndLogisticsEditText = (EditText) findViewById(R.id.administrationAndLogisticsEditText);

        mCommandAndSignalsInfoButton = (ImageView) findViewById(R.id.commandAndSignalsInfoButton);
        mCommandAndSignalsEditText = (EditText) findViewById(R.id.commandAndSignalsEditText);

        // set toolbar
        setSupportActionBar(mToolbar);

        // show back button in toolbar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // setup dialogs for info buttons
        setupInfoButtons();

        // fetch and show the brief
        fetchBrief();

        // setup edit text watchers after fetching brief
        // because fetching brief modifies the edit texts
        setupEditTextWatchers();

    }

    private void setupEditTextWatchers() {
        mNameEditText.addTextChangedListener(this);
        mGroundEditText.addTextChangedListener(this);
        mSituationEditText.addTextChangedListener(this);
        mMissionEditText.addTextChangedListener(this);
        mExecutionEditText.addTextChangedListener(this);
        mAdministrationAndLogisticsEditText.addTextChangedListener(this);
        mCommandAndSignalsEditText.addTextChangedListener(this);
    }

    private void setupInfoButtons() {
        mNameInfoButton.setOnClickListener(new InfoOnClickListener(R.string.gsmeac_name, R.string.gsmeac_name_hint));
        mGroundInfoButton.setOnClickListener(new InfoOnClickListener(R.string.gsmeac_ground, R.string.gsmeac_ground_hint));
        mSituationInfoButton.setOnClickListener(new InfoOnClickListener(R.string.gsmeac_situation, R.string.gsmeac_situation_hint));
        mMissionInfoButton.setOnClickListener(new InfoOnClickListener(R.string.gsmeac_mission, R.string.gsmeac_mission_hint));
        mExecutionInfoButton.setOnClickListener(new InfoOnClickListener(R.string.gsmeac_execution, R.string.gsmeac_execution_hint));
        mAdministrationAndLogisticsInfoButton.setOnClickListener(new InfoOnClickListener(R.string.gsmeac_administration_and_logistics, R.string.gsmeac_administration_and_logistics_hint));
        mCommandAndSignalsInfoButton.setOnClickListener(new InfoOnClickListener(R.string.gsmeac_command_and_signals, R.string.gsmeac_command_and_signals_hint));
    }

    private void initFromBrief(Brief brief){
        if(brief != null){
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

    private void fetchBrief(){

        // if brief id was provided by intent, fetch brief
        if(mBriefId != 0){

            // fetch brief from database by id
            Brief brief = BriefHelper.getInstance().getBriefById(mBriefId);

            // init from database brief
            initFromBrief(brief);

        }

    }

    private void save() {

        // get brief info from edit texts
        String name = mNameEditText.getText().toString();
        String ground = mGroundEditText.getText().toString();
        String situation = mSituationEditText.getText().toString();
        String mission = mMissionEditText.getText().toString();
        String execution = mExecutionEditText.getText().toString();
        String administrationAndLogistics = mAdministrationAndLogisticsEditText.getText().toString();
        String commandAndSignals = mCommandAndSignalsEditText.getText().toString();

        // get brief object if id set, otherwise create new brief object
        Brief brief = mBriefId != 0 ? BriefHelper.getInstance().getBriefById(mBriefId) : new Brief();

        // set brief info on brief object
        brief.setGround(ground)
                .setName(name)
                .setSituation(situation)
                .setMission(mission)
                .setExecution(execution)
                .setAdministrationAndLogistics(administrationAndLogistics)
                .setCommandAndSignals(commandAndSignals);

        // save or update brief
        boolean saved = BriefHelper.getInstance().createBrief(brief);

        if(saved){

            // update fields to values of brief object
            initFromBrief(brief);

            // brief was saved so fields are no longer edited
            mHasEditedFields = false;

            // tell the user brief was saved
            Toast.makeText(mContext, R.string.info_brief_saved, Toast.LENGTH_SHORT).show();

        } else {

            // tell the user saving brief failed
            displayErrorMessage(mContext.getString(R.string.info_failed_to_save_brief));

        }

        // focus on coordinator layout so edit texts are no longer focused
        mCoordinatorLayout.requestFocus();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu
        getMenuInflater().inflate(R.menu.activity_new_brief, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.action_save: {

                // save brief when save button is clicked in menu
                save();

                return true;

            }

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // do nothing
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // a field has changed, update boolean
        mHasEditedFields = true;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // do nothing
    }

    private class InfoOnClickListener implements View.OnClickListener {

        private @StringRes int titleResId;
        private @StringRes int infoResId;

        public InfoOnClickListener(@StringRes int titleResId, @StringRes int infoResId){
            this.titleResId = titleResId;
            this.infoResId = infoResId;
        }

        @Override
        public void onClick(View view) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
            builder.title(titleResId);
            builder.content(Html.fromHtml(mContext.getString(infoResId)));
            builder.positiveText(R.string.action_dismiss);
            showDialog(builder.build());
        }

    }

}

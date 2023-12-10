package net.berserkir.apps.gsmeac.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import net.berserkir.apps.gsmeac.R;
import net.berserkir.apps.gsmeac.adapter.BriefSectionAdapter;
import net.berserkir.apps.gsmeac.database.helper.BriefHelper;
import net.berserkir.apps.gsmeac.database.model.Brief;

public class BriefViewerActivity extends GSMEACActivity {

    private static final String EXTRA_BRIEF_ID = "brief";

    private long mBriefId;

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private BriefSectionAdapter mBriefSectionAdapter;

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
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        // setup briefs adapter for recycler view
        mBriefSectionAdapter = new BriefSectionAdapter(mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mBriefSectionAdapter);

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

            // clear previous items from adapter
            mBriefSectionAdapter.clear();

            // name
            mBriefSectionAdapter.add(new BriefSectionAdapter.BriefSection(
                    mContext.getString(R.string.gsmeac_name),
                    brief.getName()
            ));

            // ground
            mBriefSectionAdapter.add(new BriefSectionAdapter.BriefSection(
                    mContext.getString(R.string.gsmeac_ground),
                    brief.getGround()
            ));

            // situation
            mBriefSectionAdapter.add(new BriefSectionAdapter.BriefSection(
                    mContext.getString(R.string.gsmeac_situation),
                    brief.getSituation()
            ));

            // mission (and mission repeated)
            mBriefSectionAdapter.add(new BriefSectionAdapter.BriefSection(
                    mContext.getString(R.string.gsmeac_mission),
                    String.format("%s\n\n%s",
                            brief.getMission(),
                            mContext.getString(R.string.gsmeac_mission_i_say_again, brief.getMission())
                    )
            ));

            // execution
            mBriefSectionAdapter.add(new BriefSectionAdapter.BriefSection(
                    mContext.getString(R.string.gsmeac_execution),
                    brief.getExecution()
            ));

            // administration and logistics
            mBriefSectionAdapter.add(new BriefSectionAdapter.BriefSection(
                    mContext.getString(R.string.gsmeac_administration_and_logistics),
                    brief.getAdministrationAndLogistics()
            ));

            // command and signals
            mBriefSectionAdapter.add(new BriefSectionAdapter.BriefSection(
                    mContext.getString(R.string.gsmeac_command_and_signals),
                    brief.getCommandAndSignals()
            ));

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
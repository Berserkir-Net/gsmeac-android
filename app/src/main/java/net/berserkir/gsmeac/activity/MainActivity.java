package net.berserkir.gsmeac.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import net.berserkir.gsmeac.GSMEACApplication;
import net.berserkir.gsmeac.R;
import net.berserkir.gsmeac.adapter.BriefsAdapter;
import net.berserkir.gsmeac.database.helper.BriefHelper;
import net.berserkir.gsmeac.database.model.Brief;
import net.berserkir.gsmeac.util.comparator.MostRecentlyUpdatedBriefComparator;

import java.util.Collections;
import java.util.List;

public class MainActivity extends GSMEACActivity {

    private Toolbar mToolbar;
    private TextView mEmptyTextView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;

    private BriefsAdapter mBriefsAdapter;

    @Override
    protected void onResume() {

        super.onResume();

        // load briefs when activity is resumed
        loadBriefs();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // setup layout
        setContentView(R.layout.activity_main);

        // get reference to views
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mEmptyTextView = (TextView) findViewById(android.R.id.empty);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floatingactionbutton);

        // set toolbar
        setSupportActionBar(mToolbar);

        // launch brief editor activity when floating action button is clicked
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, BriefEditorActivity.class));
            }
        });

        // setup briefs adapter for recycler view
        mBriefsAdapter = new BriefsAdapter(mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mBriefsAdapter);

        // set brief click listener
        mBriefsAdapter.setBriefClickListener(new BriefsAdapter.BriefClickListener() {

            @Override
            public void onBriefClick(Brief brief) {
                // launch brief viewer
                startActivity(BriefViewerActivity.intentForBriefId(mContext, brief.getId()));
            }

            @Override
            public void onBriefLongClick(final Brief brief) {

                // ask if user wants to delete this brief

                MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
                builder.title(brief.getName());
                builder.content(R.string.info_delete_brief);
                builder.positiveText(R.string.action_delete);
                builder.negativeText(R.string.action_cancel);
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        // delete brief
                        boolean deleted = BriefHelper.getInstance().deleteBrief(brief);

                        if(deleted){

                            // remove brief from adapter
                            mBriefsAdapter.remove(brief);

                            updateEmptyView();

                            // tell the user brief was deleted
                            Toast.makeText(mContext, R.string.info_brief_deleted, Toast.LENGTH_SHORT).show();

                        } else {

                            // tell the user deleting brief failed
                            displayErrorMessage(mContext.getString(R.string.info_failed_to_delete_brief));

                        }

                    }
                });
                showDialog(builder.build());

            }

        });

        // reload briefs when user swipes to refresh
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBriefs();
            }
        });

        // load briefs when activity is launched
        loadBriefs();

    }

    private void loadBriefs(){

        // clear adapter
        mBriefsAdapter.clear();

        // load briefs from database
        List<Brief> briefs = BriefHelper.getInstance().getBriefs();

        // sort briefs by most recently updated
        Collections.sort(briefs, new MostRecentlyUpdatedBriefComparator());

        // add all briefs to adapter
        for(Brief brief : briefs){
            mBriefsAdapter.add(brief);
        }

        updateEmptyView();

        // stop swipe refresh layout from refreshing state
        mSwipeRefreshLayout.setRefreshing(false);

    }

    private void updateEmptyView() {

        // show empty info text if no briefs exist in adapter
        mEmptyTextView.setVisibility(mBriefsAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.action_about: {
                displayAboutDialog();
                return true;
            }

        }

        return super.onOptionsItemSelected(item);

    }

    public void displayAboutDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
        builder.title(R.string.action_about);
        builder.content(mContext.getString(R.string.info_about, GSMEACApplication.getVersion(), GSMEACApplication.getVersionCode()));
        builder.positiveText(R.string.action_dismiss);
        builder.autoDismiss(true);
        showDialog(builder.build());
    }

}

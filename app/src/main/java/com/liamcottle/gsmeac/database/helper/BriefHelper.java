package com.liamcottle.gsmeac.database.helper;

import com.liamcottle.gsmeac.database.GSMEACDatabase;
import com.liamcottle.gsmeac.database.model.Brief;
import com.yahoo.squidb.data.SquidCursor;
import com.yahoo.squidb.data.TableModel;
import com.yahoo.squidb.sql.Query;

import java.util.ArrayList;
import java.util.List;

public class BriefHelper {

    private static BriefHelper sInstance = null;

    public static BriefHelper getInstance() {
        if(sInstance == null){
            synchronized(BriefHelper.class){
                if(sInstance == null){
                    sInstance = new BriefHelper();
                }
            }
        }
        return sInstance;
    }

    private GSMEACDatabase mGSMEACDatabase = GSMEACDatabase.getInstance();

    private BriefHelper() {

    }

    //region Queries

    private Query getBriefsOrderedByNameQuery() {
        return Query.select().from(Brief.TABLE).orderBy(Brief.NAME.asc());
    }

    //endregion

    //region Cursors

    public SquidCursor<Brief> getBriefsOrderedByNameCursor() {
        return mGSMEACDatabase.query(Brief.class, getBriefsOrderedByNameQuery());
    }

    //endregion

    /**
     * Get Brief by Id
     * @param id long Brief Id
     * @return Brief
     */
    public Brief getBriefById(long id) {
        return mGSMEACDatabase.fetch(Brief.class, id);
    }

    /**
     * Get all Briefs
     * @return List
     */
    public List<Brief> getBriefs() {

        List<Brief> results = new ArrayList<>();
        SquidCursor<Brief> briefs = mGSMEACDatabase.query(Brief.class, getBriefsOrderedByNameQuery());

        try {
            for(briefs.moveToFirst(); !briefs.isAfterLast(); briefs.moveToNext()) {
                Brief brief = new Brief();
                brief.readPropertiesFromCursor(briefs);
                results.add(brief);
            }
        } finally {
            briefs.close();
        }

        return results;

    }

    public boolean createBrief(Brief brief) {

        long timestamp = System.currentTimeMillis();

        // only set created at value if id is not set
        if(brief.getId() == TableModel.NO_ID){
            brief.setCreatedAt(timestamp);
        }

        // set updated at value each time
        brief.setUpdatedAt(timestamp);

        return mGSMEACDatabase.persist(brief);

    }

    public boolean deleteBrief(Brief brief) {
        return mGSMEACDatabase.delete(Brief.class, brief.getId());
    }

    public int deleteAllBriefs() {
        return mGSMEACDatabase.deleteAll(Brief.class);
    }

}
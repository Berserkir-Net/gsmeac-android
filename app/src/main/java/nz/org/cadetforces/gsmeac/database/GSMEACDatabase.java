package nz.org.cadetforces.gsmeac.database;

import nz.org.cadetforces.gsmeac.database.model.Brief;
import com.yahoo.squidb.data.ISQLiteDatabase;
import com.yahoo.squidb.data.ISQLiteOpenHelper;
import com.yahoo.squidb.data.SquidDatabase;
import com.yahoo.squidb.sql.Table;

public class GSMEACDatabase extends SquidDatabase {

    private static final int VERSION = 1;

    private static GSMEACDatabase instance = null;

    public static GSMEACDatabase getInstance() {
        if(instance == null){
            synchronized(GSMEACDatabase.class){
                if(instance == null){
                    instance = new GSMEACDatabase();
                }
            }
        }
        return instance;
    }

    private GSMEACDatabase() {
        super();
    }

    @Override
    public String getName() {
        return "gsmeac.db";
    }

    @Override
    protected int getVersion() {
        return VERSION;
    }

    @Override
    protected Table[] getTables() {
        return new Table[]{
                Brief.TABLE
        };
    }

    @Override
    protected boolean onUpgrade(ISQLiteDatabase db, int oldVersion, int newVersion) {
        return false;
    }

    @Override
    protected ISQLiteOpenHelper createOpenHelper(String databaseName, OpenHelperDelegate delegate, int version) {
        return OpenHelperCreator.getCreator().createOpenHelper(databaseName, delegate, version);
    }

}
package net.berserkir.gsmeac.database;

import com.yahoo.squidb.data.ISQLiteOpenHelper;
import com.yahoo.squidb.data.SquidDatabase;

public abstract class OpenHelperCreator {

    private static OpenHelperCreator sCreator = null;

    public static OpenHelperCreator getCreator() {
        return sCreator;
    }

    public static void setCreator(OpenHelperCreator creator) {
        sCreator = creator;
    }

    protected abstract ISQLiteOpenHelper createOpenHelper(String databaseName, SquidDatabase.OpenHelperDelegate delegate, int version);

}
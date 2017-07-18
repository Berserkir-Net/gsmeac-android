package net.berserkir.gsmeac;

import android.app.Application;
import android.content.pm.PackageManager;
import net.berserkir.gsmeac.database.OpenHelperCreator;
import com.yahoo.squidb.android.AndroidOpenHelper;
import com.yahoo.squidb.data.ISQLiteOpenHelper;
import com.yahoo.squidb.data.SquidDatabase;

public class GSMEACApplication extends Application {

    private static GSMEACApplication sInstance;

    private String mVersion;
    private int mVersionCode;

    @Override
    public void onCreate() {

        super.onCreate();
        sInstance = this;

        OpenHelperCreator.setCreator(new OpenHelperCreator() {
            @Override
            protected ISQLiteOpenHelper createOpenHelper(String databaseName, SquidDatabase.OpenHelperDelegate delegate, int version) {
                return new AndroidOpenHelper(sInstance, databaseName, delegate, version);
            }
        });

    }

    public static GSMEACApplication get(){
        return sInstance;
    }

    public static String getVersion(){

        GSMEACApplication application = get();

        if(application == null){
            return "Unknown";
        }

        if(application.mVersion != null){
            return application.mVersion;
        }

        PackageManager packageManager = application.getPackageManager();
        String packageName = application.getPackageName();

        try {
            application.mVersion = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return application.mVersion;

    }

    public static int getVersionCode(){

        GSMEACApplication application = get();

        if(application == null){
            return -1;
        }

        if(application.mVersionCode != 0){
            return application.mVersionCode;
        }

        PackageManager packageManager = application.getPackageManager();
        String packageName = application.getPackageName();

        try {
            application.mVersionCode = packageManager.getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return application.mVersionCode;

    }

}

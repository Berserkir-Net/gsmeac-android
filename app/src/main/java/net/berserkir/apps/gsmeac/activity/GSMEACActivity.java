package net.berserkir.apps.gsmeac.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import com.afollestad.materialdialogs.MaterialDialog;
import net.berserkir.apps.gsmeac.R;

public class GSMEACActivity extends AppCompatActivity {

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case android.R.id.home: {
                onBackPressed();
                return true;
            }

        }

        return super.onOptionsItemSelected(item);

    }

    public void showDialog(Dialog dialog){
        try {
            dialog.show();
        } catch(Exception ignore){}
    }

    public void dismissDialog(Dialog dialog){
        try {
            dialog.dismiss();
        } catch(Exception ignore){}
    }

    public void displayErrorMessage(String message){
        if(!TextUtils.isEmpty(message) && !isFinishing()){
            MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
            builder.title(R.string.title_oops);
            builder.content(message);
            builder.positiveText(R.string.action_dismiss);
            showDialog(builder.build());
        }
    }

}

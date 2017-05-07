package com.liamcottle.gsmeac.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.liamcottle.gsmeac.R;

public class MainActivity extends GSMEACActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

    }

}

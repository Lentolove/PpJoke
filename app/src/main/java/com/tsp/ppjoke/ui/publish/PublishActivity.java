package com.tsp.ppjoke.ui.publish;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.tsp.libnavannotation.ActivityDestination;
import com.tsp.main.R;


/**
 * author : shengping.tian
 * time   : 2021/10/28
 * desc   :
 * version: 1.0
 */
@ActivityDestination(pageUrl = "main/tabs/publish", needLogin = true)
public class PublishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
    }
}

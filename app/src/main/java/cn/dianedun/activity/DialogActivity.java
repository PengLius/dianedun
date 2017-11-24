package cn.dianedun.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.vise.xsnow.manager.AppManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.dianedun.R;

/**
 * Created by Administrator on 2017/11/20.
 */

public class DialogActivity extends Activity {
    @Bind(R.id.dialog_qx)
    TextView dialog_qx;

    @Bind(R.id.dialog_qr)
    TextView dialog_qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);
        dialog_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.vise.xsnow.manager.AppManager.getInstance().finishAllActivity();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}

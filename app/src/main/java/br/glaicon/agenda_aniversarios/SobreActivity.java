package br.glaicon.agenda_aniversarios;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.List;

public class SobreActivity extends ActionBarActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        editText = (EditText) findViewById(R.id.edtSugestao);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sobre, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_enviar:
                EnviarEmail();
                editText.setText("");
                break;
        }

        return true;
    }

    private void EnviarEmail() {
        Intent enviarEmailIntent = new Intent(Intent.ACTION_SEND);
        enviarEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"gjpeixer@hotmail.com"});
        enviarEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "E-mail de sugestão");
        enviarEmailIntent.putExtra(Intent.EXTRA_TEXT, editText.getText().toString());
        enviarEmailIntent.setType("text/plain");

        List<ResolveInfo> activityList = getPackageManager().queryIntentActivities(enviarEmailIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains(".email")) {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                enviarEmailIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                enviarEmailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                enviarEmailIntent.setComponent(name);
                startActivity(enviarEmailIntent);
                break;
            }
        }
    }
}



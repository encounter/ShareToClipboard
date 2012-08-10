package com.encounterpc.clipboard;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.ClipboardManager;
import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class ShareActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String type = getIntent().getType();
        boolean isText = type.equals("text/plain");
        Object content;
        if (extras != null && (isText ? (content = extras.getString(android.content.Intent.EXTRA_TEXT)) != null :
                (content = getIntent().getParcelableExtra(Intent.EXTRA_STREAM)) != null)) {
            if (isText) {
                ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE))
                        .setText((String) content);
                makeText(this, getString(R.string.success), LENGTH_LONG).show();
            } else {
                File downloads = new File(Environment.getExternalStorageDirectory(), "Download");
                downloads.mkdir();
                try {
                    String name = new SimpleDateFormat("yyyy-MM-dd kk.mm.ss").format(new Date()) + "." +
                            type.substring(6).replace("e", ""); // jpeg -> jpg hack
                    ByteStreams.copy(getContentResolver().openInputStream((Uri) content),
                            new FileOutputStream(new File(downloads, name)));
                    makeText(this, getString(R.string.saved_image), LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    makeText(this, getString(R.string.error_saving), LENGTH_LONG).show();
                }
            }
        } else makeText(this, getString(R.string.no_content), LENGTH_LONG).show();
        finish();
    }
}

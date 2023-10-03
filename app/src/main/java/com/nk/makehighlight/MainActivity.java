package com.nk.makehighlight;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private final static int OPEN_FILE_ACTIVITY = 25517;

    private Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditor = new Editor(findViewById(R.id.videoView));

        Button openButton = (Button)findViewById(R.id.openButton);
        openButton.setOnClickListener((_1) -> {
            openFile();
        });
    }

    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");

        // Optionally, specify a URI for the file that should appear in the system file picker when it loads.
        //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, OPEN_FILE_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) { return; }
        if (resultCode != RESULT_OK) { return; }

        if (requestCode == OPEN_FILE_ACTIVITY) {
            mEditor.open(data.getData());
        }
    }
}

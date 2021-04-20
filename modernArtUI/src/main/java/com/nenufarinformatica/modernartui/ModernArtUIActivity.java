package com.nenufarinformatica.modernartui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.util.ArrayList;

public class ModernArtUIActivity extends ActionBarActivity implements OnSeekBarChangeListener {
    static final String STATE_COLORS = "COLORS";
    static final int RECTANGLE_QTD = 4;
    private SeekBar mSeekBar;
    private View[] mRectangles = new View[RECTANGLE_QTD];
    private RandomColor[] mColors = new RandomColor[RECTANGLE_QTD];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modern_art_ui);

        setClick();

        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(this);

        // find rectangles to be colored
        mRectangles[0] = findViewById(R.id.layout1);
        mRectangles[1] = findViewById(R.id.layout2);
        mRectangles[2] = findViewById(R.id.layout3);
        mRectangles[3] = findViewById(R.id.layout4);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore colors from saved state
            ArrayList<String> colorsFromSave = savedInstanceState.getStringArrayList(STATE_COLORS);
            for (int i = 0; i < RECTANGLE_QTD; i++) {
                mColors[i] = new RandomColor(this, colorsFromSave.get(i));
            }
        } else {
            // create colors for each colored square
            restartColors();
        }

        // initial color
        setRectanglesColors(mSeekBar.getProgress());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modern_art_ui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.dialog_more_information) {
            DialogInterface.OnClickListener onClick = new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.moma_site)));
                        startActivity(webIntent);
                    }
                }

            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            TextView message = new TextView(this);
            message.setText(R.string.dialog_message);
            message.setGravity(Gravity.CENTER);
            builder.setView(message);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.visit_moma, onClick);
            builder.setNegativeButton(R.string.not_now, onClick);
            AlertDialog dialog = builder.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save colors
        ArrayList<String> colorsToSave = new ArrayList<String>();
        for(int i=0; i<RECTANGLE_QTD; i++) {
            colorsToSave.add(mColors[i].serialized());
        }
        savedInstanceState.putStringArrayList(STATE_COLORS, colorsToSave);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        setRectanglesColors(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private void setRectanglesColors(int step) {
        for(int i=0; i<RECTANGLE_QTD; i++) {
            mRectangles[i].setBackgroundColor(mColors[i].actualColor(step));
        }
    }

    private void setClick() {
        View layoutMaster = findViewById(R.id.layoutMaster);
        layoutMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartColors();
                setRectanglesColors(mSeekBar.getProgress());
            }
        });
    }

    private void restartColors() {
        for (int i = 0; i < RECTANGLE_QTD; i++) {
            mColors[i] = new RandomColor(this);
        }
    }
}

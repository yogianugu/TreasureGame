package com.mobileapps.treasuregame;

import com.mobileapps.game.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Assignment #3 ResultActivity.java
 * 
 * @author Yogeshwar Reddy Anugu & Sri Harsha Maddela
 */
public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.resultLayout);
		relativeLayout.setBackgroundColor(Color.BLACK);

		double time = (double) getIntent().getExtras().getDouble(
				TreasureGameActivity.TIME_KEY);

		TextView msgTView = (TextView) findViewById(R.id.message_textView);
		msgTView.setTextColor(Color.LTGRAY);
		if (time <= 50.0) {
			msgTView.setText("Congratulations, you unlocked the treasure chest!");
		} else {
			msgTView.setText("Sorry! Try again ..");
			ImageView imageView = (ImageView) findViewById(R.id.result_imageView);
			imageView.setImageResource(R.drawable.lose);
		}

		msgTView = (TextView) findViewById(R.id.time_textView);
		msgTView.setTextColor(Color.LTGRAY);
		msgTView.setText("Time elapsed : " + time + " second");

		Button btn = (Button) findViewById(R.id.button_try_again);
		btn.setTextColor(Color.LTGRAY);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResultActivity.this,
						TreasureGameActivity.class);
				finish();
				startActivity(intent);
			}
		});

		btn = (Button) findViewById(R.id.button_exit);
		btn.setTextColor(Color.LTGRAY);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}

}

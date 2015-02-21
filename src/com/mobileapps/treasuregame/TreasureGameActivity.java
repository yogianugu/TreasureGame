package com.mobileapps.treasuregame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.mobileapps.game.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Assignment #3 TreasureGameActivity.java
 * 
 * @author Yogeshwar Reddy Anugu & Sri Harsha Maddela
 */
public class TreasureGameActivity extends Activity {

	public Integer[] iconIds = new Integer[] { R.drawable.diamond,
			R.drawable.garnet, R.drawable.gem, R.drawable.pearl,
			R.drawable.ruby, R.drawable.sapphire, R.drawable.swarovski,
			R.drawable.toppaz };
	private final Handler handler = new Handler();
	private boolean touchEnabled = true;
	private int count;
	private int size = 4;
	private long starts, ends;
	private ArrayList<Integer> focusIconsList;
	private ImageView imageView;
	public ArrayList<TreasureGameActivity.Card> Cards = new ArrayList<TreasureGameActivity.Card>();
	public Card c1, c2;
	public static final String TIME_KEY = "DurationOfGame";

	public class Card implements View.OnClickListener {
		public ImageView iv;
		public int imageId;
		public boolean isDone;
		public boolean isCovered;

		public Card(Context cx, int imageId, int h, int w) {
			this.imageId = imageId;
			this.iv = new ImageView(cx);
			this.iv.setLayoutParams(new TableRow.LayoutParams(h, w));
			this.iv.setOnClickListener(this);
			this.isDone = false;
			cover();
		}

		public void cover() {
			this.iv.setImageResource(R.drawable.cover);
			this.isCovered = true;
		}

		public void uncover() {
			this.iv.setImageResource(this.imageId);
			this.isCovered = false;
		}

		public boolean match(Card c) {
			return (this.imageId == c.imageId)
					&& (this.imageId == focusIconsList.get(0));
		}

		@Override
		public void onClick(View v) {
			if (touchEnabled) {
				if (isCovered) {
					if (c1 == null) { // first clicked
						c1 = this;
						uncover();
					} else { // second clicked
						if (isCovered) {
							if (match(c1)) {
								double durationInSeconds = durationOfGame();
								uncover();
								isDone = true;
								c1.isDone = true;
								c1 = null;
								count = count - 2;
								focusIconsList.remove(focusIconsList.get(0));
								if (count <= 0) {
									Intent intent = new Intent(
											TreasureGameActivity.this,
											ResultActivity.class);
									intent.putExtra(TIME_KEY, durationInSeconds);
									finish();
									startActivity(intent);
								}
								newFocusJewelImage();
							} else {
								touchEnabled = false;
								uncover();
								c2 = this;
								handler.postDelayed(new Runnable() {
									public void run() {
										c1.cover();
										c2.cover();
										c1 = null;
										touchEnabled = true;
									}
								}, 1000);
							}
						}
					}
				}
			}
		}
	}

	public double durationOfGame() {
		ends = System.currentTimeMillis();
		ends -= starts;
		return ends / 1000.0;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		LinearLayout root = (LinearLayout) findViewById(R.id.rootLayout);
		root.setBackgroundColor(Color.BLACK);

		TextView textView = (TextView) findViewById(R.id.find_textView);
		textView.setTextColor(Color.LTGRAY);

		newGameSetup();

		Button b = (Button) findViewById(R.id.button_uncover);
		b.setTextColor(Color.LTGRAY);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				for (Card card : Cards) {
					card.uncover();
				}
				handler.postDelayed(new Runnable() {
					public void run() {
						for (Card card : Cards) {
							if (focusIconsList.contains(card.imageId))
								card.cover();
						}
						if (c1 != null && c1.isCovered)
							c1.uncover();
					}
				}, 1000);
			}
		});

		b = (Button) findViewById(R.id.button_newgame);
		b.setTextColor(Color.LTGRAY);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				newGameSetup();
			}
		});
	}

	public void newGameSetup() {
		starts = System.currentTimeMillis();
		imageView = (ImageView) findViewById(R.id.focus_imageView);
		focusIconsList = new ArrayList<Integer>(Arrays.asList(iconIds));

		newFocusJewelImage();

		count = size * size;

		Cards.clear();
		for (int i : iconIds) {
			Cards.add(new Card(this, i, getResources().getDimensionPixelSize(
					R.dimen.imageHeight), getResources().getDimensionPixelSize(
					R.dimen.imageWidth)));
			Cards.add(new Card(this, i, getResources().getDimensionPixelSize(
					R.dimen.imageHeight), getResources().getDimensionPixelSize(
					R.dimen.imageWidth)));
		}
		Collections.shuffle(Cards);
		TableLayout tbl = (TableLayout) findViewById(R.id.tableLayout);
		tbl.removeAllViews();
		for (int i = 0; i < size; i++) {
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			for (int j = 0; j < size; j++) {
				tr.addView(Cards.get(i * size + j).iv);
			}
			tbl.addView(tr);
		}
	}

	private void newFocusJewelImage() {
		Collections.shuffle(focusIconsList);
		if (focusIconsList.size() != 0) {
			imageView.setImageResource(focusIconsList.get(0));
		}
	}

}

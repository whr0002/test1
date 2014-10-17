package com.rs.app;

import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class TipsValidator {
	private SherlockFragmentActivity mSfa;

	public TipsValidator(SherlockFragmentActivity sfa) {
		this.mSfa = sfa;

	}

	public void Validation() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(mSfa);
		SharedPreferences tipsPrefs = mSfa.getSharedPreferences("Tips", 0);
		SharedPreferences.Editor tipsEditor = tipsPrefs.edit();
		SharedPreferences.Editor prefsEditor = prefs.edit();
		boolean hasTips = prefs.getBoolean("hasTips", false);
		if (!hasTips) {
			// No Tips stored, storing the tips
			Map<String, String> tips = new HashMap<String, String>();
			tips.put(
					"1",
					"Copy your kitty");
			
			tips.put(
					"Copy your kitty",
					"Learn to do stretching exercises when you wake up. It boosts circulation and digestion, and eases back pain.");
			
			tips.put(
					"2",
					"Don¡¯t skip breakfast");
			
			tips.put(
					"Don¡¯t skip breakfast",
					"Studies show that eating a proper breakfast is one of the most positive things you can do if you are trying to lose weight. Breakfast skippers tend to gain weight. A balanced breakfast includes fresh fruit or fruit juice, a high-fibre breakfast cereal, low-fat milk or yoghurt, wholewheat toast, and a boiled egg.");
			
			for (String s : tips.keySet()) {
				tipsEditor.putString(s, tips.get(s));
			}

			// Commit all tips
			tipsEditor.commit();

			// Save the global boolean to true
			prefsEditor.putBoolean("hasTips", true);
			prefsEditor.putInt("numOfTips", tips.size());
			prefsEditor.commit();

		}
	}
}

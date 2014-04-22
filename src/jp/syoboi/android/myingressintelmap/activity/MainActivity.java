package jp.syoboi.android.myingressintelmap.activity;

import android.app.Activity;
import android.os.Bundle;

import jp.syoboi.android.myingressintelmap.R;
import jp.syoboi.android.myingressintelmap.fragment.PlaceholderFragment_;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.main_activity)
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, PlaceholderFragment_.builder().build())
					.commit();
		} 
	}
} 

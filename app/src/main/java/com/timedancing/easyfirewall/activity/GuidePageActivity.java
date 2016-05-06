package com.timedancing.easyfirewall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.timedancing.easyfirewall.R;
import com.timedancing.easyfirewall.cache.AppConfig;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by zengzheying on 16/1/21.
 */
public class GuidePageActivity extends AppCompatActivity {

	private static final String ARG_POSITION = "position";

	private static final int[] imgResources = new int[]{R.drawable.guide1, R.drawable.guide2, R.drawable.guide3};
	private ViewPager mVPGuide;
	private CircleIndicator mIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (AppConfig.isShouldShowGuidePage(this)) {
			setContentView(R.layout.activity_guide_page);
			mVPGuide = (ViewPager) findViewById(R.id.vp_guide);
			mIndicator = (CircleIndicator) findViewById(R.id.indicator);
			mVPGuide.setAdapter(new GuidePageFragmentStatePager(getSupportFragmentManager()));
			mIndicator.setViewPager(mVPGuide);
		} else {
			navigateToUnlockActivity();
			finish();
		}
	}

	private void navigateToUnlockActivity() {
		Intent intent = new Intent(this, UnlockActivity.class);
		startActivity(intent);
	}

	static class GuidePageFragmentStatePager extends FragmentStatePagerAdapter {
		public GuidePageFragmentStatePager(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return GuideFragment.newInstance(position);
		}

		@Override
		public int getCount() {
			return imgResources.length;
		}
	}

	static class GuideFragment extends Fragment {
		private int position;

		public static GuideFragment newInstance(int position) {

			Bundle args = new Bundle();
			args.putInt(ARG_POSITION, position);
			GuideFragment fragment = new GuideFragment();
			fragment.setArguments(args);
			return fragment;
		}

		@Nullable
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			position = getArguments().getInt(ARG_POSITION);
			View rootView = inflater.inflate(R.layout.fragment_guide, container, false);
			Button go = (Button) rootView.findViewById(R.id.btn_go);
			ImageView imageView = (ImageView) rootView.findViewById(R.id.img_guide);
			imageView.setImageResource(imgResources[position]);
			if (position == imgResources.length - 1) {
				go.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), UnlockActivity.class);
						startActivity(intent);
					}
				});
				go.setVisibility(View.VISIBLE);
			} else {
				go.setVisibility(View.GONE);
			}
			return rootView;
		}
	}

}

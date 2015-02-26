package com.da.apps.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.da.apps.fragments.FragmentImagePicker;
import com.da.apps.imagepicker.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImagePickerActvity extends FragmentActivity {
	ImageLoader mImageLoader;
	DisplayImageOptions options;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		buildOptions(R.drawable.ic_launcher);
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(this));
		setContentView(R.layout.imagepicker_activity);
		
		replaceFragment(new FragmentImagePicker(),false, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	}
	
	public void buildOptions(int resource) {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(resource)
		.showImageForEmptyUri(resource)
		.showImageOnFail(resource)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.ARGB_8888)
		.build();
	}
	
	public ImageLoader getmImageLoader() {
		return mImageLoader;
	}
	
	public DisplayImageOptions getOptions() {
		return options;
	}
	
	public void replaceFragment(Fragment fragment, boolean addToBackStack,
			int transition) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fl_content_area, fragment, fragment.getClass()
				.getName());
		ft.setTransition(transition);
		if (addToBackStack) {
			ft.addToBackStack(fragment.getClass().getName());
		}
		ft.commit();

	}
}

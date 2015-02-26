package com.da.apps.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.da.apps.activities.ImagePickerActvity;
import com.da.apps.imagepicker.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

@SuppressLint("InflateParams")
public class FragmentImagePicker extends Fragment implements OnClickListener {

	private View vi;
	private Button mButtonPickImage;
	private Button mButtonUploadImage;
	private ImageView mImageView;
	private static int PICK_IMAGE = 1;
	private String picturePath;
	private ImagePickerActvity mImagePickerActvity;
	private ProgressBar mProgressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImagePickerActvity = (ImagePickerActvity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		vi = inflater.inflate(R.layout.imagepicker_fragment, null);

		mImageView = (ImageView) vi.findViewById(R.id.imageview);

		mButtonPickImage = (Button) vi.findViewById(R.id.btn_pick_image);

		mButtonUploadImage = (Button) vi.findViewById(R.id.btn_upload_image);

		mProgressBar = (ProgressBar)vi.findViewById(R.id.progress);
		
		mButtonPickImage.setOnClickListener(this);
		mButtonUploadImage.setOnClickListener(this);

		return vi;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_pick_image:

			pickImage();

			break;
		case R.id.btn_upload_image:
			// upload code here using the file path
			// picturePath
			break;

		default:
			break;
		}
	}

	private void pickImage() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(i, PICK_IMAGE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PICK_IMAGE
				&& resultCode == FragmentActivity.RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getActivity().getContentResolver().query(
					selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();

			if (picturePath != null && !picturePath.equalsIgnoreCase("")) {
				String mTempFilePath = "";
				if (!picturePath.startsWith("file://")) {
					mTempFilePath = "file://" + picturePath;
				} else {
					mTempFilePath = picturePath;
				}
				// Show image using imageloader, to avoid oom issues,
				// on upload button you can upload your image
				if (mImagePickerActvity.getmImageLoader() != null) {
					mImagePickerActvity.getmImageLoader().displayImage(
							mTempFilePath, mImageView,
							mImagePickerActvity.getOptions(),
							new ImageLoadingListener() {

								@Override
								public void onLoadingStarted(String imageUri,
										View view) {
									toggleLoadingVisiblity(true);
								}

								@Override
								public void onLoadingFailed(String imageUri,
										View view, FailReason failReason) {
									toggleLoadingVisiblity(false);

								}

								@Override
								public void onLoadingComplete(String imageUri,
										View view, Bitmap loadedImage) {
									toggleLoadingVisiblity(false);

								}

								@Override
								public void onLoadingCancelled(String imageUri,
										View view) {
									toggleLoadingVisiblity(false);
								}
							});
				}
			}
		}
	}
	public void toggleLoadingVisiblity(boolean isVisible){
		mProgressBar.setVisibility(isVisible ? View.VISIBLE:View.INVISIBLE);
		mImageView.setVisibility(!isVisible ? View.VISIBLE:View.INVISIBLE);
	}

}

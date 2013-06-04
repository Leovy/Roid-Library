package net.rinc.library.util;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class IntentUtil {
	/**
	 * 
	 * @param outputFile
	 * @return
	 */
	public static Intent getCameraIntent(String outputFile){
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(outputFile)));
		return intent;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Intent getGalleryIntent(){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
	}
}
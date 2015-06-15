package com.shilangtech.diankan.message_send;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.os.Environment;
import android.util.Log;

/**
 * @author Google_acmer
 * 
 * 录音类
 *
 */
public  class RecordUtil implements OnErrorListener {


	private MediaRecorder mRecorder;

	String Filename=Environment.getExternalStorageDirectory().getAbsolutePath()+"/record.amr";
	private MediaPlayer mPlayer;
	public void start() {
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setOutputFile(Filename);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);	
			mRecorder.setOnErrorListener(this);
		}
		try {
			mRecorder.prepare();
			mRecorder.start();
		} catch (IOException e) {
			// Log.e(LOG_TAG, "prepare() failed");
		}


	}

	public void stop() {
		//	Log.e("TAG", "stop()");
		if (mRecorder != null) {
			//
			mRecorder.setOnErrorListener(null);
			mRecorder.stop();


			mRecorder.release();
			mRecorder = null;
			Log.e("TAG", "stop ok");
			mPlayer = new MediaPlayer(); 
 
				try {
					mPlayer.setDataSource(Filename);
				}
				 catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		}
	}

	public void pause() {
		if (mRecorder != null) {
			mRecorder.setOnErrorListener(null);
			mRecorder.stop();
		}
	}
	public void recordPlay(){
		if(!mPlayer.isPlaying()){
		try{  
			mPlayer.prepare();  
			mPlayer.start();  
		}catch(IOException e){  
			Log.e("TAG","播放失败");  
		}  
		
		}else{
			mPlayer.stop();
		}
		mPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO 自动生成的方法存根
				Log.e("TAG1", "player stop!");
				mPlayer.stop();				
			}
		});
	}
	public void stopRecordPlay(){
		if(mPlayer!=null){
			Log.e("TAG1", "player not null!");
			mPlayer.stop();
			//mPlayer.release();
		}else
		Log.e("TAG1", "player  null!");
	}
	public void release(){
		if(mPlayer.isPlaying())mPlayer.stop();
		mPlayer.release();
	}
	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
		// TODO 自动生成的方法存根

	}


}

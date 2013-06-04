package net.rinc.library.widget;

import android.view.View;

public abstract class OnClickListenerX implements View.OnClickListener{
	private boolean processFlag = true;
	private long delay=500;
	
	/**
	 * 
	 */
	public OnClickListenerX(){
		super();
	}
	
	/**
	 * 
	 * @param delay
	 */
	public OnClickListenerX(long delay){
		super();
		this.delay=delay;
	}
	
	/**
	 * 
	 * @param view
	 */
	public abstract void onClickX(View view);

	/**
	 * 
	 */
	public void onClick(View view){
		 if (processFlag) {
             setProcessFlag();
             onClickX(view);
             new DelayThread().start();
         }
	}
	
    private synchronized void setProcessFlag() {
        processFlag = false;
    }
    
    private class DelayThread extends Thread {
        public void run() {
            try {
                sleep(delay);
                processFlag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
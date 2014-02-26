package com.luisdelarosa.glass.stackrep;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;
import com.google.android.glass.timeline.TimelineManager;
import com.luisdelarosa.stackoverflow.MockStackOverflowUser;
import com.luisdelarosa.stackoverflow.StackOverflowUser;
import com.luisdelarosa.stackrep.R;

public class StackRepService extends Service {
	
	private static final String LIVE_CARD_TAG = "StackRepLiveCard";
	
	private static final int MENU_ACTIVITY_REQUEST_CODE = 1337;
	
	private LiveCard mLiveCard;
	private RemoteViews mRemoteViews;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		publishLiveCardToTimeline();
		
		StackOverflowUser profile = getStackOverflowProfile();
		updateLiveCardWithProfile(profile);
		
	    // Restart this service if it gets killed
	    return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		// The service is no longer used and is being destroyed
		if (mLiveCard != null) {
			// Unpublish the LiveCard
			mLiveCard.unpublish();
		}
	}

	private void updateLiveCardWithProfile(StackOverflowUser profile) {
		// GOTCHA: If you use setString instead of setCharSequence, you will see the "sad cloud" icon
		mRemoteViews.setCharSequence(R.id.text_view_username, "setText", profile.getUsername());
		mRemoteViews.setCharSequence(R.id.text_view_reputation, "setText", profile.getReputation());
		
		// GOTCHA: If this next line is commented out, it won't update
		mLiveCard.setViews(mRemoteViews);
	}

	private void publishLiveCardToTimeline() {
		// Don't publish the LiveCard twice
		if (mLiveCard != null) { return; }
		
		TimelineManager timelineManager = TimelineManager.from(this);
		mLiveCard = timelineManager.createLiveCard(LIVE_CARD_TAG);
		
		// Create remote views
		mRemoteViews = new RemoteViews(this.getPackageName(), R.layout.live_card_stack_overflow_profile);
		mLiveCard.setViews(mRemoteViews);
		
		// GOTCHA: If this section is commented out, LiveCard will not be published and no error will be shown in LogCat 
		// Create menu Activity and set as PendingIntent
		Intent menuActivityIntent = new Intent(this, MenuActivity.class);
		mLiveCard.setAction(PendingIntent.getActivity(this, MENU_ACTIVITY_REQUEST_CODE, menuActivityIntent, 0));
		
		// Publish and reveal the live card
		mLiveCard.publish(PublishMode.REVEAL);
	}

	private StackOverflowUser getStackOverflowProfile() {
		return new MockStackOverflowUser();
	}

}

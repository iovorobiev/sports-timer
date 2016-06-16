package com.ideas.sportscounter.timer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.NotificationCompat;

import com.ideas.sportscounter.R;


public class TimerService extends Service implements Timer {
    public static final int REQUEST_CODE = 234;
    private static final long MILLIS_IN_SECOND = 1000;
    public static final int NOTIFICATION_ID = 123;
    private final TimerBinder binder = new TimerBinder();
    private UiUpdateListener uiUpdateListener;
    private CountdownTimerZero timer;
    private Pronouncer pronouncer;
    private VibratorHelper vibrator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void initAnnouncers() {
        pronouncer.initEnabled();
        vibrator.initEnabled();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (pronouncer == null) {
            pronouncer = new Pronouncer(this, getString(R.string.go));
        }
        if (vibrator == null) {
            vibrator = new VibratorHelper(this);
        }
        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        uiUpdateListener = null;
        return super.onUnbind(intent);
    }

    @Override
    public void setUiUpdateListener(UiUpdateListener uiUpdateListener) {
        this.uiUpdateListener = uiUpdateListener;
    }

    @Override
    public void start(long millis, long interval) {
        initTimer(millis, interval);
        startForeground(NOTIFICATION_ID, createNotification());
        timer.start();
    }

    private Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setDefaults(Notification.DEFAULT_LIGHTS);
        builder.setSmallIcon(R.drawable.ic_av_timer_black_24dp);
        builder.setContentTitle(getString(R.string.timer));
        builder.setContentText(getString(R.string.timer_going));
        builder.setContentIntent(PendingIntent.getActivity(this, REQUEST_CODE, getIntent(),
                PendingIntent.FLAG_CANCEL_CURRENT));
        return builder.build();
    }

    //TODO: make a navigation system
    private Intent getIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    private void initTimer(final long millis, final long interval) {
        timer = new CountdownTimerZero(millis, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (uiUpdateListener != null) {
                    uiUpdateListener.onUiUpdate(millisUntilFinished);
                }
                float seconds = millisUntilFinished / MILLIS_IN_SECOND;
                vibrator.vibrate((int) Math.floor(seconds));
                pronouncer.speakSeconds((int) Math.floor(seconds));
            }

            @Override
            public void onFinish() {
                stopForeground(true);
                if (uiUpdateListener == null) {
                    stopSelf();
                } else {
                    uiUpdateListener.onFinish();
                }
            }
        };
    }

    @Override
    public void stop() {
        stopForeground(true);
        if (timer != null) {
            timer.cancel();
        }
    }

    public static Intent getServiceIntent(Context context) {
        return new Intent(context, TimerService.class);
    }

    public class TimerBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }

    public interface UiUpdateListener {
        @UiThread
        void onUiUpdate(long millis);

        @UiThread
        void onFinish();
    }
}

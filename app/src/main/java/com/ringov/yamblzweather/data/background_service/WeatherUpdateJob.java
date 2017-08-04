package com.ringov.yamblzweather.data.background_service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.ringov.yamblzweather.App;
import com.ringov.yamblzweather.data.db.DatabaseLegacy;
import com.ringov.yamblzweather.presentation.ui.main.MainActivity;
import com.ringov.yamblzweather.R;
import com.ringov.yamblzweather.domain.settings.SettingsRepository;
import com.ringov.yamblzweather.domain.weather.WeatherRepository;
import com.ringov.yamblzweather.presentation.Utils;

import javax.inject.Inject;

public class WeatherUpdateJob extends Job {

    public static final String TAG = "com.ringov.yamblzweather.weather_update_job";
    public static final int FLEX = 300000;

    private int notificationId = 0;

    @Inject
    WeatherRepository weatherRepository;
    @Inject
    SettingsRepository settings;

    public static void schedule() {
        long interval = DatabaseLegacy.getInstance().getUpdateInterval();
        JobRequest job = new JobRequest.Builder(WeatherUpdateJob.TAG)
                .setPeriodic(interval, FLEX)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .build();
        job.schedule();
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        App.getComponent().inject(this);
        weatherRepository.updateWeatherIfDataIsOld()
                .filter(w -> settings.isNotificationsEnabled())
                .subscribe(weatherInfo -> {
                    String formattedTemperature = Utils.getFormattedTemperature(getContext(), weatherInfo.getTemperature());
                    String condition = getContext().getString(weatherInfo.getConditionName());

                    PendingIntent pi = PendingIntent.getActivity(getContext(), 0,
                            new Intent(getContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                    Notification notification = new NotificationCompat.Builder(getContext())
                            .setContentTitle(getContext().getString(R.string.app_name))
                            .setContentIntent(pi)
                            .setContentText(getContext().getString(R.string.wthr_notification_message, formattedTemperature, condition))
                            .setAutoCancel(true)
                            .setSmallIcon(weatherInfo.getConditionImage())
                            .setShowWhen(true)
                            .setLocalOnly(true)
                            .build();
                    NotificationManagerCompat.from(getContext())
                            .notify(notificationId, notification);
                }, this::handleError);
        return Result.SUCCESS;
    }

    private void handleError(Throwable throwable) {
    }
}
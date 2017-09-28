package com.ringov.yamblzweather.data.background_service;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class WeatherUpdateJobCreator implements JobCreator {
    @Override
    public Job create(String tag) {
        switch (tag) {
            case WeatherUpdateJob.TAG:
                return new WeatherUpdateJob();
            default:
                return null;
        }
    }
}

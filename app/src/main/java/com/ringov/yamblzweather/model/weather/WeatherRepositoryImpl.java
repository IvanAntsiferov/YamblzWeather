package com.ringov.yamblzweather.model.weather;

import com.ringov.yamblzweather.db.Database;
import com.ringov.yamblzweather.db.model.DBWeather;
import com.ringov.yamblzweather.internet.APIFactory;
import com.ringov.yamblzweather.internet.Converter;
import com.ringov.yamblzweather.internet.WeatherService;
import com.ringov.yamblzweather.model.base.BaseRepositoryImpl;
import com.ringov.yamblzweather.viewmodel.model.WeatherInfo;

import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ringov on 12.07.17.
 */

@Singleton
public class WeatherRepositoryImpl extends BaseRepositoryImpl implements WeatherRepository {

    @Override
    public Observable<WeatherInfo> getWeatherInfo() {
        // todo move choosing city to the higher level
        int cityId = 524901;

        DBWeather dbWeather = getDatabase().getWeather(cityId);
        Observable<WeatherInfo> observable;
        if (dbWeather != null) {
            observable = Observable.just(Converter.getWeatherInfo(dbWeather))
                    .mergeWith(getService().getWeather(cityId).map(Converter::getWeatherInfo));
        } else {
            observable = getService().getWeather(cityId).map(Converter::getWeatherInfo);
        }
        return observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    private WeatherService getService() {
        return APIFactory.getRetrofitService(WeatherService.class);
    }

    private Database getDatabase() {
        return Database.getInstance();
    }
}

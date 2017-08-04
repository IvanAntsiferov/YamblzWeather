package com.ringov.yamblzweather.presentation.ui.main.weather;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;

import com.ringov.yamblzweather.App;
import com.ringov.yamblzweather.presentation.base.BaseLiveData;
import com.ringov.yamblzweather.presentation.base.BaseViewModel;

import javax.inject.Inject;

public class WeatherViewModel extends BaseViewModel {

    private BaseLiveData<Boolean> loadingData = new BaseLiveData<>();
    //private BaseLiveData<UIWeather> weatherData = new BaseLiveData<>();
    private BaseLiveData<Throwable> errorData = new BaseLiveData<>();
    private BaseLiveData<String> cityData = new BaseLiveData<>();

   /* @Inject
    WeatherRepository weatherRepository;

    @Inject
    LocationRepository locationRepository;*/

    public WeatherViewModel() {
        App.getComponent().inject(this);
        /*disposables.add(
                weatherRepository
                        .getLastWeatherInfo()
                        .concatWith(weatherRepository.updateWeather())
                        .doOnSubscribe(disposable -> loadingData.updateValue(true))
                        .doFinally(() -> loadingData.updateValue(false))
                        .subscribe(
                                uiWeather -> weatherData.updateValue(uiWeather),
                                throwable -> errorData.updateValue(throwable)
                        )
        );*/

        /*disposables.add(
                locationRepository
                        .getLocation()
                        .subscribe(s -> cityData.updateValue(s), e -> errorData.updateValue(e))
        );*/
    }

    void observe(
            LifecycleOwner owner,
            Observer<Boolean> loadingObserver,
            //Observer<UIWeather> weatherObserver,
            Observer<Throwable> errorObserver,
            Observer<String> cityObserver
    ) {
        loadingData.observe(owner, loadingObserver);
        //weatherData.observe(owner, weatherObserver);
        errorData.observe(owner, errorObserver);
        cityData.observe(owner, cityObserver);
    }

    // View callbacks
    void onRefresh() {
        /*disposables.add(
                weatherRepository.updateWeatherIfDataIsOld()
                        .doOnSubscribe(disposable -> loadingData.updateValue(true))
                        .doFinally(() -> loadingData.updateValue(false))
                        .subscribe(
                                uiWeather -> weatherData.updateValue(uiWeather),
                                throwable -> errorData.updateValue(throwable)
                        )
        );*/
    }
}

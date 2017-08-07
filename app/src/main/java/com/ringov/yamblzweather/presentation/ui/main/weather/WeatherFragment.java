package com.ringov.yamblzweather.presentation.ui.main.weather;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import com.ringov.yamblzweather.R;
import com.ringov.yamblzweather.presentation.base.BaseMvvmFragment;
import com.ringov.yamblzweather.presentation.entity.UIWeatherList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import timber.log.Timber;

public class WeatherFragment extends BaseMvvmFragment<WeatherViewModel> {

    public static final String TAG = "WeatherFragment";

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected Class<WeatherViewModel> getViewModelClass() {
        return WeatherViewModel.class;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_weather;
    }

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.rv_forecast)
    RecyclerView forecastRecycler;

    private WeatherAdapter weatherAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(R.string.title_weather);

        weatherAdapter = new WeatherAdapter(getContext(), new ArrayList<>());

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        forecastRecycler.addItemDecoration(itemDecoration);
        forecastRecycler.setHasFixedSize(true);
        forecastRecycler.setAdapter(weatherAdapter);
    }

    @Override
    protected void attachInputListeners() {
        getViewModel().observe(this, this::showLoading, this::showForecast, this::showError);

        // Listen for swipe to refresh
        disposables.add(
                RxSwipeRefreshLayout
                        .refreshes(swipeLayout)
                        .subscribe(o -> getViewModel().onRefresh())
        );

        // Listen for item clicks
        disposables.add(
                weatherAdapter.getOnItemClickObservable()
                        .subscribe(item -> viewModel.openWeatherDetails(item))
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        weatherAdapter.destroy();
    }

    @Override
    protected void onViewModelAttach() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass());
    }

    private void showForecast(List<UIWeatherList> forecast) {
        weatherAdapter.replace(forecast);
    }

    private void showLoading(boolean isLoading) {
        swipeLayout.setRefreshing(isLoading);
    }

    private void showError(Throwable error) {
        Timber.e(error);
        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }
}

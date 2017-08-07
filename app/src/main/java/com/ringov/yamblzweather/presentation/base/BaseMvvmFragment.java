package com.ringov.yamblzweather.presentation.base;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.ringov.yamblzweather.dagger.Injectable;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseMvvmFragment<VM extends BaseViewModel>
        extends BaseFragment implements LifecycleRegistryOwner, Injectable {

    protected VM viewModel;

    protected CompositeDisposable disposables = new CompositeDisposable();

    protected abstract Class<VM> getViewModelClass();

    private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }

    protected abstract void onViewModelAttach();

    // Subscribe for user input events in this method
    protected void attachInputListeners() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onViewModelAttach();
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        attachInputListeners();
    }

    @Override
    @CallSuper
    public void onStop() {
        super.onStop();
        disposables.clear();
    }

    protected VM getViewModel() {
        return viewModel;
    }
}

package com.example.cosa.arch.thingDetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.cosa.CosaApplication
import com.example.cosa.arch.base.BaseViewModel
import com.example.cosa.extension.backgroundWork
import com.example.cosa.models.ThingAdded
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class ThingDetailViewModel(application: Application) : BaseViewModel(application) {
    private val thingAddedDao = CosaApplication.dataBase.thingAddedDao()
    private val mThingAdded = MutableLiveData<ThingAdded>()
    var thingAdded: MutableLiveData<ThingAdded> = mThingAdded
    private val compositeDisposable = CompositeDisposable()

    fun getThingDetail(id: Long){
        if (id != -1L) {
            Single.just(mThingAdded)
                .backgroundWork()
                .doOnSuccess {
                    mThingAdded.value = thingAddedDao.getThingAddedBy(id)
                    thingAdded = mThingAdded
                }
                .subscribe()
                .addTo(compositeDisposable)

        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
package com.example.cosa.arch.thingsDetails

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.cosa.CosaApplication
import com.example.cosa.arch.base.BaseViewModel
import com.example.cosa.extension.backgroundWork
import com.example.cosa.models.Things
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class ThingsDetailViewModel(application: Application) : BaseViewModel(application) {
    private val thingAddedDao = CosaApplication.dataBase.thingAddedDao()
    private val mThingAdded = MutableLiveData<Things>()
    var things: MutableLiveData<Things> = mThingAdded
    private val compositeDisposable = CompositeDisposable()

    fun getThingDetail(id: Long){
        if (id != -1L) {
            Single.just(mThingAdded)
                .backgroundWork()
                .doOnSuccess {
                    mThingAdded.value = thingAddedDao.getThingAddedBy(id)
                    things = mThingAdded
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
package com.example.cosa.arch.things

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cosa.CosaApplication
import com.example.cosa.arch.base.BaseViewModel
import com.example.cosa.extension.backgroundWork
import com.example.cosa.models.DeletedThings
import com.example.cosa.models.Things
import com.example.cosa.repository.db.dao.DeletedThingsDao
import com.example.cosa.repository.db.dao.ThingsDao
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class ThingsViewModel(application: Application) : BaseViewModel(application) {

    private val mDotClicked = MutableLiveData<Pair<View, Things>>()
    val dotClicked: LiveData<Pair<View, Things>> get() = mDotClicked

    private val mWholeClicked = MutableLiveData<Things>()
    val wholeClicked: LiveData<Things> get() = mWholeClicked

    private val compositeDisposable = CompositeDisposable()
    private val thingsDao: ThingsDao = CosaApplication.dataBase.thingAddedDao()
    private val deletedThingsDao: DeletedThingsDao = CosaApplication.dataBase.deletedThingAddedDao()
    fun getThingAdded(): LiveData<MutableList<Things>> = thingsDao.getAll()


    fun onDotsClicked(view: View, things: Things) {
        mDotClicked.value = Pair(view, things)
    }

    fun onWholeItemClicked(things: Things) {
        mWholeClicked.value = things
    }

    fun insertThingAdded(things: Things) {
        Single.just(things)
            .backgroundWork()
            .doOnSuccess {
                thingsDao.insert(it)
            }
            .subscribe()
            .addTo(compositeDisposable)
    }

    fun deleteItem(things: Things, saveTrash: Boolean) {
        if (saveTrash) {
            val delThingAdded = DeletedThings()
            delThingAdded.cacheUri = things.cacheUri
            delThingAdded.place = things.place
            delThingAdded.thing = things.thing
            Single.just(things)
                .backgroundWork()
                .doOnSuccess {
                    thingsDao.deleteItem(things)
                    deletedThingsDao.insert(delThingAdded)
                }
                .subscribe()
                .addTo(compositeDisposable)
        } else {
            Single.just(things)
                .backgroundWork()
                .doOnSuccess {
                    thingsDao.deleteItem(things)
                }
                .subscribe()
                .addTo(compositeDisposable)
        }
    }

    fun updateThingInfo(name: String, place: String, cacheUri: String, id: Long) {
        Single.just(name)
            .backgroundWork()
            .doOnSuccess {
                thingsDao.updateThingName(id, name)
                thingsDao.updateThingPlace(id, place)
                thingsDao.updateThingImage(id, cacheUri)
            }
            .subscribe()
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
package com.cosa.arch.things

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cosa.CosaApplication
import com.cosa.arch.base.BaseViewModel
import com.cosa.extension.backgroundWork
import com.cosa.models.DeletedThings
import com.cosa.models.Things
import com.cosa.repository.db.dao.DeletedThingsDao
import com.cosa.repository.db.dao.ThingsDao
import com.cosa.util.Event
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class ThingsViewModel(application: Application) : BaseViewModel(application) {

    private val mDotClicked = MutableLiveData<Event<Pair<View, Things>>>()
    val dotClicked: LiveData<Event<Pair<View, Things>>> get() = mDotClicked

    private val mWholeClicked = MutableLiveData<Event<Things>>()
    val wholeClicked: LiveData<Event<Things>> get() = mWholeClicked

    private val compositeDisposable = CompositeDisposable()
    private val thingsDao: ThingsDao = CosaApplication.dataBase.thingAddedDao()
    private val deletedThingsDao: DeletedThingsDao = CosaApplication.dataBase.deletedThingAddedDao()
    fun getThingAdded(): LiveData<MutableList<Things>> = thingsDao.getAll()


    fun onDotsClicked(view: View, things: Things) {
        mDotClicked.value = Event(Pair(view, things))
    }

    fun onWholeItemClicked(things: Things) {
        mWholeClicked.value = Event(things)
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
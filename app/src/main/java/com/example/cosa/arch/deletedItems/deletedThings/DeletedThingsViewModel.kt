package com.example.cosa.arch.deletedItems.deletedThings

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cosa.CosaApplication
import com.example.cosa.extension.backgroundWork
import com.example.cosa.models.DeletedThings
import com.example.cosa.models.Things
import com.example.cosa.repository.db.dao.DeletedThingsDao
import com.example.cosa.repository.db.dao.ThingsDao
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class DeletedThingsViewModel(application: Application) : AndroidViewModel(application) {

    private val mItemClickedDelThing = MutableLiveData<Pair<View, DeletedThings>>()
    val itemClickedDelThing: LiveData<Pair<View, DeletedThings>> get() = mItemClickedDelThing

    private val compositeDisposable = CompositeDisposable()
    private val thingsDao: ThingsDao = CosaApplication.dataBase.thingAddedDao()
    private val deletedThingsDao: DeletedThingsDao = CosaApplication.dataBase.deletedThingAddedDao()
    fun getDeletedThingAdded(): LiveData<MutableList<DeletedThings>> = deletedThingsDao.getAll()

    fun onItemClickedDelThing(view: View, things: DeletedThings){
        mItemClickedDelThing.value = Pair(view, things)
    }

    fun completelyDeleteThing(things: DeletedThings) {
        Single.just(things)
            .backgroundWork()
            .doOnSuccess {
                deletedThingsDao.deleteItem(things)
            }
            .subscribe()
            .addTo(compositeDisposable)
    }

    fun recoverItem(things: DeletedThings) {
        val thing = Things()
        thing.cacheUri = things.cacheUri
        thing.place = things.place
        thing.thing = things.thing
        Single.just(things)
            .backgroundWork()
            .doOnSuccess {
                thingsDao.insert(thing)
                deletedThingsDao.deleteItem(things)
            }
            .subscribe()
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}
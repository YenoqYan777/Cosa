package com.example.cosa.arch.deletedItems.deletedThings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cosa.CosaApplication
import com.example.cosa.extension.backgroundWork
import com.example.cosa.models.DeletedThingAdded
import com.example.cosa.models.ThingAdded
import com.example.cosa.repository.db.dao.DeletedThingsDao
import com.example.cosa.repository.db.dao.ThingAddedDao
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class DeletedThingsViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val thingAddedDao: ThingAddedDao = CosaApplication.dataBase.thingAddedDao()
    private val deletedThingsDao: DeletedThingsDao = CosaApplication.dataBase.deletedThingAddedDao()
    fun getDeletedThingAdded(): LiveData<MutableList<DeletedThingAdded>> = deletedThingsDao.getAll()

    fun completelyDeleteThing(thingAdded: DeletedThingAdded) {
        Single.just(thingAdded)
            .backgroundWork()
            .doOnSuccess {
                deletedThingsDao.deleteItem(thingAdded)
            }
            .subscribe()
            .addTo(compositeDisposable)
    }

    fun recoverItem(thingAdded: ThingAdded) {
        val delThingAdded = DeletedThingAdded()
        delThingAdded.id = thingAdded.id
        delThingAdded.cacheUri = thingAdded.cacheUri
        delThingAdded.place = thingAdded.place
        delThingAdded.thing = thingAdded.thing
        Single.just(thingAdded)
            .backgroundWork()
            .doOnSuccess {
                thingAddedDao.insert(thingAdded)
                deletedThingsDao.deleteItem(delThingAdded)
            }
            .subscribe()
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}
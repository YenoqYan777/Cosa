package com.example.cosa.arch.thingAdded

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

class ThingAddedViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val thingForDetailPage: ThingAdded = ThingAdded()
    }

    private val compositeDisposable = CompositeDisposable()
    private val thingAddedDao: ThingAddedDao = CosaApplication.dataBase.thingAddedDao()
    private val deletedThingsDao: DeletedThingsDao = CosaApplication.dataBase.deletedThingAddedDao()
    fun getThingAdded(): LiveData<MutableList<ThingAdded>> = thingAddedDao.getAll()
    fun getDeletedThingAdded(): LiveData<MutableList<DeletedThingAdded>> = deletedThingsDao.getAll()
    fun getThingForDetail(): ThingAdded = thingForDetailPage

    fun insertThingAdded(thingAdded: ThingAdded) {
        Single.just(thingAdded)
            .backgroundWork()
            .doOnSuccess {
                thingAddedDao.insert(it)
            }
            .subscribe()
            .addTo(compositeDisposable)
    }

    fun deleteItem(thingAdded: ThingAdded, saveTrash: Boolean) {
        if (saveTrash) {
            val delThingAdded = DeletedThingAdded()
            delThingAdded.cacheUri = thingAdded.cacheUri
            delThingAdded.place = thingAdded.place
            delThingAdded.thing = thingAdded.thing
            Single.just(thingAdded)
                .backgroundWork()
                .doOnSuccess {
                    thingAddedDao.deleteItem(thingAdded)
                    deletedThingsDao.insert(delThingAdded)
                }
                .subscribe()
                .addTo(compositeDisposable)
        } else {
            Single.just(thingAdded)
                .backgroundWork()
                .doOnSuccess {
                    thingAddedDao.deleteItem(thingAdded)
                }
                .subscribe()
                .addTo(compositeDisposable)
        }
    }

    fun setThingForThingAdded(thingAdded: ThingAdded) {
        thingForDetailPage.cacheUri = thingAdded.cacheUri
        thingForDetailPage.place = thingAdded.place
        thingForDetailPage.thing = thingAdded.thing
    }

    fun updateThingInfo(name: String, place: String, cacheUri: String, id: Long) {
        Single.just(name)
            .backgroundWork()
            .doOnSuccess {
                thingAddedDao.updateThingName(id, name)
                thingAddedDao.updateThingPlace(id, place)
                thingAddedDao.updateThingImage(id, cacheUri)
            }
            .subscribe()
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
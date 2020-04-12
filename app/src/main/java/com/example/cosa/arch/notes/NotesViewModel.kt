package com.example.cosa.arch.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cosa.CosaApplication
import com.example.cosa.extension.backgroundWork
import com.example.cosa.models.Notes
import com.example.cosa.repository.db.dao.NotesDao
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val compositeDisposable = CompositeDisposable()
    private val notesDao: NotesDao = CosaApplication.thingAddedDB.notesDao()

    fun getNotes(): LiveData<MutableList<Notes>> = notesDao.getAll()



    fun insertNote(notes: Notes) {
        Single.just(notes)
            .backgroundWork()
            .doOnSuccess {
                notesDao.insert(it)
            }
            .subscribe()
            .addTo(compositeDisposable)
    }


}
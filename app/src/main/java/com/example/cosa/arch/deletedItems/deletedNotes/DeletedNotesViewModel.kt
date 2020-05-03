package com.example.cosa.arch.deletedItems.deletedNotes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cosa.CosaApplication
import com.example.cosa.extension.backgroundWork
import com.example.cosa.models.DeletedNotes
import com.example.cosa.models.Notes
import com.example.cosa.repository.db.dao.NotesDao
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class DeletedNotesViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val notesDao: NotesDao = CosaApplication.dataBase.notesDao()
    private val deletedNotesDao = CosaApplication.dataBase.deletedNotes()

    fun getDeletedData(): LiveData<MutableList<DeletedNotes>> = deletedNotesDao.getAll()

    fun completelyDeleteNote(notes: DeletedNotes) {
        Single.just(notes)
            .backgroundWork()
            .doOnSuccess {
                deletedNotesDao.deleteItem(notes)
            }
            .subscribe()
            .addTo(compositeDisposable)
    }

    fun recoverNote(notes: Notes) {
        val delNote = DeletedNotes()
        delNote.id = notes.id
        delNote.text = notes.text

        Single.just(notes)
            .backgroundWork()
            .doOnSuccess {
                notesDao.insert(notes)
                deletedNotesDao.deleteItem(delNote)
            }
            .subscribe()
            .addTo(compositeDisposable)
    }


    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}
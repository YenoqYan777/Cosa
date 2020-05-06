package com.example.cosa.arch.notes

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

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private var editTextMessage: String = ""
        private var itemId: Long = 0
    }

    private val compositeDisposable = CompositeDisposable()
    private val notesDao: NotesDao = CosaApplication.dataBase.notesDao()
    private val deletedNotesDao = CosaApplication.dataBase.deletedNotes()

    fun getNotes(): LiveData<MutableList<Notes>> = notesDao.getAll()

    fun deleteItem(notes: Notes, boolean: Boolean) {
        if (boolean) {
            val delNote = DeletedNotes()
            delNote.text = notes.text
            Single.just(notes)
                .backgroundWork()
                .doOnSuccess {
                    notesDao.deleteItem(notes)
                    deletedNotesDao.insert(delNote)
                }
                .subscribe()
                .addTo(compositeDisposable)
        } else {
            Single.just(notes)
                .backgroundWork()
                .doOnSuccess {
                    notesDao.deleteItem(notes)
                }
                .subscribe()
                .addTo(compositeDisposable)
        }
    }

    fun setItemId(id: Long) {
        itemId = id
    }

    fun getItemId(): Long = itemId

    fun setEditTextMessage(text: String) {
        editTextMessage = text
    }

    fun getEditTextMessage(): String = editTextMessage


    fun updateNoteInfo(text: String, id: Long) {
        Single.just(text)
            .backgroundWork()
            .doOnSuccess {
                notesDao.updateNote(id, text)
            }
            .subscribe()
            .addTo(compositeDisposable)
    }

    fun insertNote(notes: Notes) {
        Single.just(notes)
            .backgroundWork()
            .doOnSuccess {
                notesDao.insert(it)
            }
            .subscribe()
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}
package com.cosa.arch.deletedItems.deletedNotes

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cosa.CosaApplication
import com.cosa.extension.backgroundWork
import com.cosa.models.DeletedNotes
import com.cosa.models.Notes
import com.cosa.repository.db.dao.NotesDao
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class DeletedNotesViewModel(application: Application) : AndroidViewModel(application) {
    private val mItemClickedDelNotes = MutableLiveData<Pair<View, DeletedNotes>>()
    val itemClickedDelNotes: LiveData<Pair<View, DeletedNotes>> get() = mItemClickedDelNotes

    private val compositeDisposable = CompositeDisposable()
    private val notesDao: NotesDao = CosaApplication.dataBase.notesDao()
    private val deletedNotesDao = CosaApplication.dataBase.deletedNotes()

    fun getDeletedData(): LiveData<MutableList<DeletedNotes>> = deletedNotesDao.getAll()

    fun onItemClickedDelNotes(view: View, notes: DeletedNotes) {
        mItemClickedDelNotes.value = Pair(view, notes)
    }

    fun setTextForAnItem(notes: DeletedNotes): String {
        return if (!notes.title.isNullOrEmpty() && notes.title.trim().isNotEmpty()) {
            notes.title
        } else {
            notes.text
        }
    }

    fun completelyDeleteNote(notes: DeletedNotes) {
        Single.just(notes)
            .backgroundWork()
            .doOnSuccess {
                deletedNotesDao.deleteItem(notes)
            }
            .subscribe()
            .addTo(compositeDisposable)
    }

    fun recoverNote(notes: DeletedNotes) {
        val noteToRecover = Notes()
        noteToRecover.text = notes.text

        Single.just(notes)
            .backgroundWork()
            .doOnSuccess {
                notesDao.insert(noteToRecover)
                deletedNotesDao.deleteItem(notes)
            }
            .subscribe()
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}
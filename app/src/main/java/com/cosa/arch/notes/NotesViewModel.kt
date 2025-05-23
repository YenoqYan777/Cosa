package com.cosa.arch.notes

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cosa.CosaApplication
import com.cosa.arch.base.BaseViewModel
import com.cosa.extension.backgroundWork
import com.cosa.models.DeletedNotes
import com.cosa.models.Notes
import com.cosa.repository.db.dao.NotesDao
import com.cosa.util.Event
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class NotesViewModel(application: Application) : BaseViewModel(application) {

    companion object {
        private var editTextTitel: String = ""
        private var editTextMessage: String = ""
        private var itemId: Long = 0
    }

    private val mItemClicked = MutableLiveData<Event<Pair<View, Notes>>>()
    val itemClicked: LiveData<Event<Pair<View, Notes>>> get() = mItemClicked

    private val _noteItemCount = MutableLiveData(1)
    val noteItemCount get() = _noteItemCount

    private val compositeDisposable = CompositeDisposable()
    private val notesDao: NotesDao = CosaApplication.dataBase.notesDao()
    private val deletedNotesDao = CosaApplication.dataBase.deletedNotes()

    fun getNotes(): LiveData<MutableList<Notes>> = notesDao.getAll()

    fun onItemClicked(view: View, notes: Notes) {
        mItemClicked.value = Event(Pair(view, notes))
    }

    fun onNoteAdded(){
        _noteItemCount.value?.plus(1)
    }

    fun deleteItem(notes: Notes, boolean: Boolean) {
        if (boolean) {
            val delNote = DeletedNotes()
            delNote.title = notes.title
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

    fun setTextForAnItem(notes: Notes): String {
        return if (!notes.title.isNullOrEmpty() && notes.title.trim().isNotEmpty()){
            notes.title
        }else{
            notes.text
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

    fun setEditTextTitle(titel: String) {
        editTextTitel = titel
    }

    fun getEditTextTitle(): String = editTextTitel

    fun updateNoteInfo(text: String, id: Long) {
        Single.just(text)
            .backgroundWork()
            .doOnSuccess {
                notesDao.updateNote(id, text)
            }
            .subscribe()
            .addTo(compositeDisposable)
    }

    fun updateNoteInfo(titel: String, text: String, id: Long) {
        Single.just(text)
            .backgroundWork()
            .doOnSuccess {
                notesDao.updateNote(id, text)
                notesDao.updateTitle(id, titel)
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
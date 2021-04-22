package ba.ipvc.reportsapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ba.ipvc.reportsapp.db.NotesDB
import ba.ipvc.reportsapp.db.NotesRepository
import ba.ipvc.reportsapp.entities.Notes
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class NotasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotesRepository



   val allNotes: LiveData<List<Notes>>


    init {
        val notesDao = NotesDB.getDatabase(application, viewModelScope).notesDao()
        repository = notesDao?.let { NotesRepository(it) }!!
        allNotes = repository.allNotes
    }


    fun insertNote(note:Notes) = viewModelScope.launch(Dispatchers.IO){
            repository.insertNote(note)
    }
    fun deletenotebyid(uid: Int?) = viewModelScope.launch(Dispatchers.IO) {
        repository.deletenotebyid(uid)
    }
    fun updateDescriptionFromUid(uid:Int,title:String,description:String,dateMod:String)= viewModelScope.launch(Dispatchers.IO){
        repository.updateDescriptionFromUid(uid,title,description,dateMod)
    }

}
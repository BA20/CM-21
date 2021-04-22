package ba.ipvc.reportsapp.db

import androidx.lifecycle.LiveData
import ba.ipvc.reportsapp.dao.notesDao
import ba.ipvc.reportsapp.entities.Notes

class NotesRepository (private val noteDao: notesDao) {

    val allNotes: LiveData<List<Notes>> = noteDao.getAllNotes()

    suspend fun deleteAllNotes() {
        noteDao.deleteAllNotes()
    }

    suspend fun updateDescriptionFromUid(uid:Int,title:String,description:String,dateMod:String) {
        noteDao.updateDescriptionFromUid(uid,title,description,dateMod)
    }
    suspend fun insertNote(note:Notes){
        noteDao.insertNote(note)
    }
    suspend fun deletenotebyid(udi: Int?){
        noteDao.deletenotebyid(udi)
    }
}
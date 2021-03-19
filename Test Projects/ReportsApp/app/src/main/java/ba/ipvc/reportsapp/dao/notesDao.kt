package ba.ipvc.reportsapp.dao

import android.icu.text.CaseMap
import androidx.lifecycle.LiveData
import androidx.room.*
import ba.ipvc.reportsapp.entities.Notes
import java.util.*
import androidx.room.Update

@Dao
interface notesDao {
    @Query("Select * from notes_db Order by uid DESC")
    fun getAllNotes(): LiveData<List<Notes>>

    @Query("DELETE FROM notes_db")
    suspend fun deleteAllNotes()

    @Query("Delete From notes_db Where uid == :uid")
    suspend fun deletenotebyid(uid: Int?)

    @Query("UPDATE notes_db SET Description=:description, Title= :title, DateMod= :dateMod WHERE  uid == :uid")
    suspend fun updateDescriptionFromUid(uid:Int,title:String,description:String,dateMod:String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note:Notes)




}
package ba.ipvc.reportsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ba.ipvc.reportsapp.dao.notesDao
import ba.ipvc.reportsapp.entities.Notes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.reflect.KParameter

// Annotates class to be a Room Database with a table (entity) of the City class

// Note: When you modify the database schema, you'll need to update the version number and define a migration strategy
//For a sample, a destroy and re-create strategy can be sufficient. But, for a real app, you must implement a migration strategy.

@Database(entities = [Notes::class], version = 8, exportSchema = false)
public abstract class NotesDB : RoomDatabase() {
    abstract fun notesDao(): notesDao?


    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            var  mydate: Date =  Date();
            KParameter.Kind.INSTANCE?.let { database ->
            scope.launch {

              /*  val dao: notesDao? = INSTANCE?.notesDao();
                dao?.deleteAllNotes()

                var note3 = Notes(1,"Note 1","Teste Nota 1", mydate.toString(), mydate.toString())

                    // Delete all content here.


                dao?.insertNote(note3)*/

                    // Add sample cities.
                   /* var city = City(1, "Viana do Castelo", "Portugal")
                    cityDao.insert(city)
                    city = City(2, "Porto", "Portugal")
                    cityDao.insert(city)
                    city = City(3, "Aveiro", "Portugal")
                    cityDao.insert(city)*/

                }
            }
        }
    }

companion object {
    // Singleton prevents multiple instances of database opening at the
    // same time.
    @Volatile
    private var INSTANCE: NotesDB? = null

    fun getDatabase(context: Context, scope: CoroutineScope): NotesDB {
        val tempInstance = INSTANCE
        if (tempInstance != null) {
            return tempInstance
        }
        synchronized(this) {
            val instance = Room.databaseBuilder(context.applicationContext, NotesDB::class.java, "notes_db")
                //estratégia de destruição
          .fallbackToDestructiveMigration()
                .addCallback(WordDatabaseCallback(scope))
                .build()

            INSTANCE = instance
            return instance
        }
    }
}
}


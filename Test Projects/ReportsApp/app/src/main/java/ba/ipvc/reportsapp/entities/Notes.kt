package ba.ipvc.reportsapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes_db")
data class Notes(
        @PrimaryKey(autoGenerate = true) val uid: Int? = null,
        @ColumnInfo(name = "Title") val Title: String?,
        @ColumnInfo(name = "Description") val Description: String?,
        @ColumnInfo(name = "DateAdd") val DateAdd: String?,
        @ColumnInfo(name = "DateMod") val DateMod: String?

)
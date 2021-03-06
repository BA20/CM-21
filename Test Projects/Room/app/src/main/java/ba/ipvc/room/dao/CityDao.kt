package ba.ipvc.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ba.ipvc.room.entities.City

interface CityDao {


        @Query("SELECT * from city_table ORDER BY city ASC")
        fun getAllCities(): LiveData<List<City>>

        @Query("SELECT * FROM city_table WHERE country == :country")
        fun getCitiesByCountry(country: String): LiveData<List<City>>

        @Query("SELECT * FROM city_table WHERE city == :city")
        fun getCountryFromCity(city: String): LiveData<City>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insert(city: City)

        @Update
        suspend fun updateCity(city: City)

        @Query("DELETE FROM city_table")
        suspend fun deleteAll()

        @Query("DELETE FROM city_table where city == :city")
        suspend fun deleteByCity(city: String)

        @Query("UPDATE city_table SET country=:country WHERE city == :city")
        suspend fun updateCountryFromCity(city: String, country: String)
    }

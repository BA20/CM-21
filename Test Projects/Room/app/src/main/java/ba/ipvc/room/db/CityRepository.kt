package ba.ipvc.room.db

import androidx.lifecycle.LiveData
import ba.ipvc.room.dao.CityDao
import ba.ipvc.room.entities.City

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class CityRepository(private val CityDao: CityDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allCities: LiveData<List<City>> = CityDao.getAllCities()

    fun getCitiesByCountry(country: String): LiveData<List<City>> {
        return CityDao.getCitiesByCountry(country)
    }

    fun getCountryFromCity(city: String): LiveData<City> {
        return CityDao.getCountryFromCity(city)
    }

    suspend fun insert(city: City) {
        CityDao.insert(city)
    }

    suspend fun deleteAll(){
        CityDao.deleteAll()
    }

    suspend fun deleteByCity(city: String){
        CityDao.deleteByCity(city)
    }

    suspend fun updateCity(city: City) {
        CityDao.updateCity(city)
    }

    suspend fun updateCountryFromCity(city: String, country: String){
        CityDao.updateCountryFromCity(city, country)
    }
}
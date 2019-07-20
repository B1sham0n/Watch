package android.example.watch.Utils

class UtilCitiesList {
    enum class CitiesList(val cityName: String, val GMT: String){
        Moscow("Moscow","GMT+3"),
        Paris("Paris","GMT+2"),
        London("London","GMT+1"),
        Abu_Dhabi("Abu Dhabi","GMT+4"),
        Algeria("Algeria", "GMT+1"),
        Amsterdam("Amsterdam", "GMT+2"),
        Delhi("Delhi", "GMT+5:30"),
        Detroit("Detroit", "GMT-4"),
        Jakarta("Jakarta", "GMT+7"),
        Kiev("Kiev", "GMT+3"),
        Krakow("Krakow", "GMT+2")
     }
    companion object{
        fun getCitiesSize():Int{
            return (CitiesList.values().size - 1)
        }
    }
}
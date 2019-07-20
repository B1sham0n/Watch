package android.example.watch.Utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



class UtilWatch {
    companion object{
        fun getCountOnDB(context: Context):Int{
            val dbHelper: DBHelper = DBHelper(context)
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val c = db.query("watchTable", null, null, null, null, null, null)
            return c.count
        }
        fun addWatchToDB(city: String, gmt: String, context: Context){
            val cv: ContentValues = ContentValues()
            val dbHelper: DBHelper = DBHelper(context)
            val db: SQLiteDatabase = dbHelper.writableDatabase
            cv.put("city", city)
            cv.put("GMT", gmt)
            db.insert("watchTable", null, cv)
            println("city $city with $gmt added! ---------")
        }
        fun getCityFromDB(id: Int, context: Context):String{
            var city = ""
            val dbHelper: DBHelper = DBHelper(context)
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val c = db.query("watchTable", null, null, null, null, null, null)
            if(c.moveToFirst()){
                var i = 0
                do {
                    if(i == id)
                        city = c.getString(c.getColumnIndex("city"))
                    i++
                }while (c.moveToNext())
            }
            c.close()
            if(city == "")
                city = "error"
            println("get city $city ------")
            return city
        }
        fun getGMTFromDB(id: Int, context: Context):String{
            var gmt = ""
            val dbHelper: DBHelper = DBHelper(context)
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val c = db.query("watchTable", null, null, null, null, null, null)
            if(c.moveToFirst()){
                var i = 0
                do {
                    if(i == id)
                        gmt = c.getString(c.getColumnIndex("GMT"))
                    i++
                }while (c.moveToNext())
            }
            c.close()
            if(gmt == "")
                gmt = "error"
            println("get city $gmt ------")
            return gmt
        }
        fun removeIdFromDB(id: Int, context: Context){
            val dbHelper: DBHelper = DBHelper(context)
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val ID = id+1//т.к. id в базе не с 0, а с 1
            db.delete("watchTable", "id = $ID",null)
            val c = db.query("watchTable", null, null, null, null, null, null)
            var arrayCities: ArrayList<String> = ArrayList()
            var arrayGMT: ArrayList<String> = ArrayList()
            var i = 0
            if(c.moveToFirst()){
                do {
                    arrayCities.add(c.getString(c.getColumnIndex("city")))
                    arrayGMT.add(c.getString(c.getColumnIndex("GMT")))
                    i++
                }while (c.moveToNext())
            }
            removeAllDB(context)
            val cv: ContentValues = ContentValues()
            for (i in 0 until arrayCities.size){
                cv.put("city", arrayCities.get(i))
                cv.put("GMT", arrayGMT.get(i))
                db.insert("watchTable", null, cv)
                println("++++++++++++++++++++ $i")
            }
            c.close()
        }
        fun removeAllDB(context: Context){
            val dbHelper: DBHelper = DBHelper(context)
            val db: SQLiteDatabase = dbHelper.writableDatabase
            db.delete("watchTable", null, null)
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = 'watchTable'")
        }
    }
    private class DBHelper(context: Context) :SQLiteOpenHelper(context, "watchDB", null, 1) {
        override fun onCreate(db:SQLiteDatabase) {
        //Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL(
            "create table watchTable ("
            + "id integer primary key autoincrement,"
            + "city text,"
            + "GMT text"
            + ");" )
        }
    override fun onUpgrade(db:SQLiteDatabase, oldVersion:Int, newVersion:Int) {    }
    }
}
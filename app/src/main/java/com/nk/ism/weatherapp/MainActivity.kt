package com.nk.ism.weatherapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private var tempArr = ArrayList<Int>()
    private var tempMinArr = ArrayList<Int>()
    private var tempMaxArr = ArrayList<Int>()
    private var dateArr = ArrayList<String>()
    private var wMain = ArrayList<String>()
    private var extras = Bundle()
    private var imageArr = ArrayList<ImageView>()


    @RequiresApi(Build.VERSION_CODES.O)
    val localDate: LocalDate = LocalDate.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val dayOfWeek: DayOfWeek = localDate.dayOfWeek

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Collections.addAll(imageArr, image1, image2, image3, image4, image5)
        getURLonStart()
    }



    private fun getURLonStart() {
        val city = if(intent.getStringExtra("CityName")!=null){
            intent.getStringExtra("CityName")
        }else "Baku"
        val url = "https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=f85b5c06b7359cdd1aa27353796fa041&units=metric"
        MyAsyncTask().execute(url)
    }

    fun getURL(view: View) {
        val city = searchCountry.text.toString()
        val url = "https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=f85b5c06b7359cdd1aa27353796fa041&units=metric"
        MyAsyncTask().execute(url)
    }

    inner class MyAsyncTask: AsyncTask<String, String, String>(){
        override fun onPreExecute() {
            // Before task start

            tempArr.clear()
            tempMinArr.clear()
            tempMaxArr.clear()
            dateArr.clear()
            wMain.clear()
        }

        override fun doInBackground(vararg p0: String?): String {
            try{
                val url = URL(p0[0])
                val urlConnect = url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout = 7000
                val inString = convertStreamToString(urlConnect.inputStream)
                publishProgress(inString)
            }catch (ex:Exception){ }
            return " "
        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onProgressUpdate(vararg values: String?) {
            try{
                val json = JSONObject(values[0])
                    val list = json.getJSONArray("list")
                    for(i in 0 until list.length()) {
                        val array = list.getJSONObject(i)
                            val main = array.getJSONObject("main")
                                tempArr.add(main.getInt("temp"))
//                                var feels_like = main.getString("feels_like")
                                tempMinArr.add(main.getInt("temp_min"))
                                tempMaxArr.add(main.getInt("temp_max"))
//                                var pressure = main.getString("pressure")
//                                var sea_level = main.getString("sea_level")
//                                var grnd_level = main.getString("grnd_level")
//                                var humidity = main.getString("humidity")
//                                var temp_kf = main.getString("temp_kf")
                            val weather = array.getJSONArray("weather")
                                val wArr = weather.getJSONObject(0)
//                                    var id = wArr.getInt("id")
                                    wMain.add(wArr.getString("main"))
//                                    var description = wArr.getString("description")
//                                    var icon = wArr.getString("icon")
                            dateArr.add(array.getString("dt_txt"))
                    }
                    val city = json.getJSONObject("city")
                        val name = city.getString("name")
                        val country = city.getString("country")
                        val timezone = city.getInt("timezone")
                        val cityName = "$name, $country"
                current_city.text = cityName

                var temp1 = 0
                var temp2 = 0
                var temp3 = 0
                var temp4 = 0
                var temp5 = 0
                var tempM1 = 100
                var tempM2 = 100
                var tempM3 = 100
                var tempM4 = 100
                var tempM5 = 100
                for(i in 0 until 40){
                    if(dateArr[i].substring(0, 10) == localDate.format(DateTimeFormatter.ISO_DATE)) {
                        if(tempMaxArr[i] > temp1){
                            temp1 = tempMaxArr[i]
                        }
                        if(tempMinArr[i] < tempM1){
                            tempM1 = tempMinArr[i]
                        }
                    }
                    else if(dateArr[i].substring(0, 10) == localDate.plusDays(1).format(DateTimeFormatter.ISO_DATE)){
                        if(tempMaxArr[i] > temp2){
                            temp2 = tempMaxArr[i]
                        }
                        if(tempMinArr[i] < tempM2){
                            tempM2 = tempMinArr[i]
                        }
                    }
                    else if(dateArr[i].substring(0, 10) == localDate.plusDays(2).format(DateTimeFormatter.ISO_DATE)){
                        if(tempMaxArr[i] > temp3){
                            temp3 = tempMaxArr[i]
                        }
                        if(tempMinArr[i] < tempM3){
                            tempM3 = tempMinArr[i]
                        }
                    }
                    else if(dateArr[i].substring(0, 10) == localDate.plusDays(3).format(DateTimeFormatter.ISO_DATE)){
                        if(tempMaxArr[i] > temp4){
                            temp4 = tempMaxArr[i]
                        }
                        if(tempMinArr[i] < tempM4){
                            tempM4 = tempMinArr[i]
                        }
                    }
                    else if(dateArr[i].substring(0, 10) == localDate.plusDays(4).format(DateTimeFormatter.ISO_DATE)){
                        if(tempMaxArr[i] > temp5){
                            temp5 = tempMaxArr[i]
                        }
                        if(tempMinArr[i] < tempM5){
                            tempM5 = tempMinArr[i]
                        }
                    }
                }

                degrees1.text = "$temp1°C"
                degrees2.text = "$temp2°C"
                degrees3.text = "$temp3°C"
                degrees4.text = "$temp4°C"
                degrees5.text = "$temp5°C"
                degrees_min1.text = "$tempM1°C"
                degrees_min2.text = "$tempM2°C"
                degrees_min3.text = "$tempM3°C"
                degrees_min4.text = "$tempM4°C"
                degrees_min5.text = "$tempM5°C"
                day1.text = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                day2.text = dayOfWeek.plus(1).getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                day3.text = dayOfWeek.plus(2).getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                day4.text = dayOfWeek.plus(3).getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                day5.text = dayOfWeek.plus(4).getDisplayName(TextStyle.FULL, Locale.ENGLISH)

                for(i in 0 until imageArr.size) {
                    if (wMain[i*8] == "Thunderstorm") {
                        imageArr[i].setImageResource(R.drawable.d01)
                    } else if (wMain[i*8] == "Drizzle") {
                        imageArr[i].setImageResource(R.drawable.d10)
                    } else if (wMain[i*8] == "Rain") {
                        imageArr[i].setImageResource(R.drawable.d09)
                    } else if (wMain[i*8] == "Snow") {
                        imageArr[i].setImageResource(R.drawable.d13)
                    } else if (wMain[i*8] == "Mist" || wMain[i*8] == "Smoke" || wMain[i*8] == "Haze" || wMain[i*8] == "Dust" || wMain[i*8] == "Fog" || wMain[i*8] == "Sand" || wMain[i*8] == "Ash" || wMain[i*8] == "Squall" || wMain[i*8] == "Tornado") {
                        imageArr[i].setImageResource(R.drawable.d50)
                    } else if (wMain[i*8] == "Clear") {
                        imageArr[i].setImageResource(R.drawable.d01)
                    } else if (wMain[i*8] == "Clouds") {
                        imageArr[i].setImageResource(R.drawable.d02)
                    }
                }

                extras.putIntegerArrayList("TempArr", tempArr)
                extras.putStringArrayList("DateArr", dateArr)
                extras.putStringArrayList("WMain", wMain)
                extras.putString("City", cityName)
                extras.putInt("Timezone", timezone)

            }catch (ex:Exception){ }
        }

        override fun onPostExecute(result: String?) {
            // After task done
        }

    }

    fun convertStreamToString(inputStream: InputStream):String{
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))

        var line:String
        var allString= ""

        try{
            do{
                line = bufferedReader.readLine()
                if(line!=null){
                    allString += line
                }
            } while(line!=null)
            inputStream.close()
        }catch(ex:Exception){}

        return allString
    }

    fun moveToNextPage(view: View) {
        val i = Intent(this, DetailedActivity::class.java)
        i.putExtras(extras)
        startActivity(i)
    }

}


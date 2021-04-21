package com.nk.ism.weatherapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detailed.*
import java.time.ZoneOffset
import java.util.*
import kotlin.collections.ArrayList


class DetailedActivity : AppCompatActivity() {

    private var tempArr = ArrayList<Int>()
    private var dateArr = ArrayList<String?>()
    private var timeArr = ArrayList<String?>()
    private var wMain = ArrayList<String?>()
    private var timezoneOffset = 0
    private var imageDetailedArr = ArrayList<ImageView>()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)
        Collections.addAll(imageDetailedArr, imageViewDetailed1, imageViewDetailed2, imageViewDetailed3, imageViewDetailed4, imageViewDetailed5, imageViewDetailed6, imageViewDetailed7, imageViewDetailed8, imageViewDetailed9)
        showDetails()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun showDetails(){
        val intent = intent
        val extras = intent.extras
        if (extras!=null) {
            tempArr = extras.getIntegerArrayList("TempArr")!!
            dateArr = extras.getStringArrayList("DateArr")!!
            wMain = extras.getStringArrayList("WMain")!!
            timezoneOffset = extras.getInt("Timezone")
            current_city.text = extras.getString("City")
        }

        var test0 = 0
        var test1 = 0

        val offset1 = ZoneOffset.ofTotalSeconds(timezoneOffset)
        val timeZone = offset1.toString()

        for(i in 0 until 9) {
            if(timeZone[0] == '+') {
                test0 = 10 * (Character.getNumericValue(dateArr[i]!![11]) + Character.getNumericValue(timeZone[1])) + (Character.getNumericValue(dateArr[i]!![12]) + Character.getNumericValue(timeZone[2])) - 3
                if(test0>=24){
                    test0-=24
                }
            }else if(timeZone[0] == '-') {
                test0 = 10 * (Character.getNumericValue(dateArr[i]!![11]) - Character.getNumericValue(timeZone[1])) + (Character.getNumericValue(dateArr[i]!![12]) - Character.getNumericValue(timeZone[2])) - 3
                if(test0 <0){
                    test0 += 24
                }
            }
            test1 = 10 * (Character.getNumericValue(dateArr[i]!![14]) + Character.getNumericValue(timeZone[4])) + (Character.getNumericValue(dateArr[i]!![15]) + Character.getNumericValue(timeZone[5]))
            timeArr.add(test0.toString() + ":" + test1.toString()+ "0")
        }
        for(i in 0 until imageDetailedArr.size) {
            if (wMain[i] == "Thunderstorm") {
                imageDetailedArr[i].setImageResource(R.drawable.d11)
            } else if (wMain[i] == "Drizzle") {
                imageDetailedArr[i].setImageResource(R.drawable.d10)
            } else if (wMain[i] == "Rain") {
                imageDetailedArr[i].setImageResource(R.drawable.d09)
            } else if (wMain[i] == "Snow") {
                imageDetailedArr[i].setImageResource(R.drawable.d13)
            } else if (wMain[i] == "Mist" || wMain[i] == "Smoke" || wMain[i] == "Haze" || wMain[i] == "Dust" || wMain[i] == "Fog" || wMain[i] == "Sand" || wMain[i] == "Ash" || wMain[i] == "Squall" || wMain[i] == "Tornado") {
                imageDetailedArr[i].setImageResource(R.drawable.d50)
            } else if (wMain[i] == "Clear") {
                imageDetailedArr[i].setImageResource(R.drawable.d01)
            } else if (wMain[i] == "Clouds") {
                imageDetailedArr[i].setImageResource(R.drawable.d02)
            }
        }
        tmp1.text = tempArr[0].toString()+"°C"
        tmp2.text = tempArr[1].toString()+"°C"
        tmp3.text = tempArr[2].toString()+"°C"
        tmp4.text = tempArr[3].toString()+"°C"
        tmp5.text = tempArr[4].toString()+"°C"
        tmp6.text = tempArr[5].toString()+"°C"
        tmp7.text = tempArr[6].toString()+"°C"
        tmp8.text = tempArr[7].toString()+"°C"
        tmp9.text = tempArr[8].toString()+"°C"
        date1.text = timeArr[0]
        date2.text = timeArr[1]
        date3.text = timeArr[2]
        date4.text = timeArr[3]
        date5.text = timeArr[4]
        date6.text = timeArr[5]
        date7.text = timeArr[6]
        date8.text = timeArr[7]
        date9.text = timeArr[8]
        moveToMainPage()
    }

    private fun moveToMainPage() {
        val button = findViewById<Button>(R.id.buttonBack)
        button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("CityName", current_city.text)
            startActivity(intent)
        }
    }
}

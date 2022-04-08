package com.oliverClimbs.climbersAssistant.util

import android.hardware.SensorManager
import android.hardware.SensorManager.PRESSURE_STANDARD_ATMOSPHERE
import android.location.LocationManager
import android.speech.tts.TextToSpeech
import com.oliverClimbs.climbersAssistant.FeetInMeters
import com.oliverClimbs.climbersAssistant.INVALID_FILE_CHARS
import com.oliverClimbs.climbersAssistant.SharedViewModel
import com.oliverClimbs.climbersAssistant.kilogramsInPounds
import com.oliverClimbs.climbersAssistant.ui.main.MainActivity
import java.time.*
import java.time.temporal.ChronoUnit
import kotlin.Double.Companion.NEGATIVE_INFINITY
import kotlin.math.*

class Helpers
{
  companion object
  {
    lateinit var sharedViewModel: SharedViewModel
    lateinit var mainActivity: MainActivity

    var sensorManager: SensorManager? = null
    var locationManager: LocationManager? = null

    var tts: TextToSpeech? = null
    var narrateClimbMetric: Boolean = false
    var narrateRappelMetric: Boolean = true
    var narrateRestMetric: Boolean = false

    // ---------------------------------------------------------------------------------------------
    fun altDif(oldPressure: Double,
               newPressure: Double,
               seaLevelPressure: Double = PRESSURE_STANDARD_ATMOSPHERE.toDouble()) =
      hPaToMeters(newPressure, seaLevelPressure) - hPaToMeters(oldPressure, seaLevelPressure)
    // SensorManager.getAltitude(seaLevelPressure, newPressure) -
    // SensorManager.getAltitude(seaLevelPressure, oldPressure)

    // ---------------------------------------------------------------------------------------------
    fun metersToFeet(meters: Double): Double = meters * FeetInMeters

    // ---------------------------------------------------------------------------------------------
    fun feetToMeters(feet: Double): Double = feet / FeetInMeters

    // ---------------------------------------------------------------------------------------------
    fun kilogramsToPounds(kilograms: Double): Double = kilograms * kilogramsInPounds

    // ---------------------------------------------------------------------------------------------
    fun poundsToKilograms(pounds: Double): Double = pounds / kilogramsInPounds

    // ---------------------------------------------------------------------------------------------
    fun metersPerHour(meters: Double, duration: Duration): Double
    {
      var speed = meters * 3600000 / duration.toMillis()

      if (speed == NEGATIVE_INFINITY || speed.isNaN()) speed = 0.0

      return speed

    }

    // ---------------------------------------------------------------------------------------------
    fun metersPerMinute(meters: Double, duration: Duration): Double
    {
      var speed = meters * 60000 / duration.toMillis()

      if (speed == NEGATIVE_INFINITY || speed.isNaN()) speed = 0.0

      return speed

    }

    // ---------------------------------------------------------------------------------------------
    fun calcSlopeFromDistance(distance: Double, height: Double): Double =
      if (distance != 0.0)
        Math.toDegrees(atan(height / distance))
      else 90.0

    // ---------------------------------------------------------------------------------------------
    fun calcSlopeFromRoute(route: Double, height: Double): Double =
      if (route != 0.0 &&
          height <= route)
        Math.toDegrees(asin(height / route))
      else 90.0

    // ---------------------------------------------------------------------------------------------
    // Source: https://www.mide.com/air-pressure-at-altitude-calculator
    private const val Tb = 288
    private const val Lb = -0.0065
    private const val R = 8.31432
    private const val g0 = 9.80665
    private const val M = 0.028964

    fun hPaToMeters(hPa: Double, hPa0: Double = PRESSURE_STANDARD_ATMOSPHERE.toDouble()) =
      (Tb / Lb) * ((hPa / hPa0).pow((-R * Lb) / (g0 * M)) - 1)

    // ---------------------------------------------------------------------------------------------
    fun metersTohPa(m: Double, hPa0: Double = PRESSURE_STANDARD_ATMOSPHERE.toDouble()) =
      hPa0 * (1F + (Lb / Tb) * m).pow((-g0 * M) / (R * Lb))

    // ---------------------------------------------------------------------------------------------
    fun slopeToFactor(slope: Double) = 1 / sin(Math.toRadians(slope))

    // ---------------------------------------------------------------------------------------------
    fun timeToString(time: Instant) =
      LocalTime.from(ZonedDateTime.ofInstant(time, ZoneId.systemDefault())).toString()

    // ---------------------------------------------------------------------------------------------
    fun dateToString(time: Instant) =
      LocalDate.from(ZonedDateTime.ofInstant(time, ZoneId.systemDefault())).toString()

    // ---------------------------------------------------------------------------------------------
    fun nowToFilename() =
      ("${LocalDate.now()} ${LocalTime.now().truncatedTo(ChronoUnit.SECONDS)}")
        .replace(Regex("[$INVALID_FILE_CHARS]"), "-")

    // ---------------------------------------------------------------------------------------------

    fun durationToLongString(duration: Duration): String
    {
      val hours = duration.toHours()
      val minutes = (duration.seconds / 60.0 - hours * 60).roundToInt()
      var timeRemaining = ""

      if (minutes > 0) timeRemaining = "$minutes minutes"
      if (hours > 0) timeRemaining = "$hours hours $timeRemaining"

      timeRemaining = timeRemaining.trim()

      return timeRemaining

    }

    // ---------------------------------------------------------------------------------------------
    fun median(values: MutableList<Double>): Double
    {
      var value = 0.0

      if (values.isNotEmpty())
      {
        if (values.count() == 1)
        {
          value = values[0]

        }
        else
        {
          if (values.count() > 2)
          {
            values.sort()

          }

          value =
            if (values.count() % 2 == 0)
              (values[values.count() / 2] +
                  values[values.count() / 2 - 1]) / 2
            else
              values[values.count() / 2]

        }
      }

      return value

    }
  }
}
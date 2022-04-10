class Helpers
{
  companion object
  {
    fun metersToFeet(meters: Double): Double = meters * FeetInMeters

    fun feetToMeters(feet: Double): Double = feet / FeetInMeters

    fun metersPerHour(meters: Double, duration: Duration): Double
    {
      var speed = meters * 3600000 / duration.toMillis()
      if (speed == NEGATIVE_INFINITY || speed == POSITIVE_INFINITY || speed.isNaN()) speed = 0.0
      return speed

    }

    fun metersPerMinute(meters: Double, duration: Duration): Double
    {
      var speed = meters * 60000 / duration.toMillis()
      if (speed == NEGATIVE_INFINITY || speed == POSITIVE_INFINITY || speed.isNaN()) speed = 0.0
      return speed

    }
}

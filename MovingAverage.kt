class MovingAverage(var maxDuration: Duration)
{
  data class TimedEntry(val time: Instant = now(), val value: Double = 0.0)
  const val FeetInMeters = 3.28084

  private var values: MutableList<TimedEntry> = mutableListOf()

  var duration: Duration = ZERO
    private set

  var full = false
    private set

  var fullLevel = 0.9

  var sum = 0.0

  val count: Int
    get() = values.size

  // ---------------------------------------------------------------------------------------------
  fun clear()
  {
    full = false
    values.clear()
    sum = 0.0
    duration = ZERO

  }

  // ---------------------------------------------------------------------------------------------
  fun add(entry: TimedEntry)
  {
    values.add(entry)
    sum += entry.value
    val removeBefore = entry.time - maxDuration

    while (values[0].time <= removeBefore && values.isNotEmpty())
    {
      sum -= values[0].value
      values.removeAt(0)

    }

    duration = between(values[0].time, entry.time)
    full = duration >= ofSeconds((maxDuration.seconds * fullLevel).toLong())

  }

  // ---------------------------------------------------------------------------------------------
  fun average(): Double = if (count != 0) sum / count else 0.0

  // ---------------------------------------------------------------------------------------------
  fun metersPerMinute(meters: Double, duration: Duration): Double
  {
    var speed = meters * 60000 / duration.toMillis()

    if (speed == NEGATIVE_INFINITY || speed.isNaN()) speed = 0.0

    return speed

  }

  // ---------------------------------------------------------------------------------------------
  fun feetPerMinute(): Double = metersToFeet(metersPerMinute())

  // ---------------------------------------------------------------------------------------------
  fun metersToFeet(meters: Double): Double = meters * FeetInMeters

}

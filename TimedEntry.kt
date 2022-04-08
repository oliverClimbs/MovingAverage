package com.oliverClimbs.climbersAssistant.updown

import java.time.Instant
import java.time.Instant.now

data class TimedEntry(val time: Instant = now(), val value: Double = 0.0)

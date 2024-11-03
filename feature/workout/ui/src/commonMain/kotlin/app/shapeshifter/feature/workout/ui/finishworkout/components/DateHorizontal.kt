package app.shapeshifter.feature.workout.ui.finishworkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.resources.Dimens
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarLayoutInfo
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusDays
import java.time.DayOfWeek
import java.util.Locale
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.until

@Composable
fun DateSelector(
    modifier: Modifier = Modifier,
) {
    val todayDate by remember { mutableStateOf(LocalDate.now()) }

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val scope = rememberCoroutineScope()

    val weekCalendarState = rememberWeekCalendarState(
        startDate = todayDate.minus(DatePeriod(years = 1)),
        endDate = todayDate.plus(DatePeriod(months = 6)),
        firstDayOfWeek = DayOfWeek.MONDAY,
    )

    val layoutInfo = {
        weekCalendarState.layoutInfo
    }

//    val selectDate: (date: LocalDate) -> Unit = { date ->
//        scope.launch {
//            // handle scrolling
//            val dayOfWeek = date.adjustedDayOfWeek(DayOfWeek.MONDAY).value
//            val weekLayoutInfo = weekCalendarState.layoutInfo
//                .visibleWeeksInfo.find { it.week.days.any { weekDay -> weekDay.date == date } }
//            val viewportSize = weekCalendarState.layoutInfo.viewportSize.width
//
//            // this means week is visible
//            if (weekLayoutInfo != null) {
//                val weekSize = weekLayoutInfo.size
//                val daySize = weekSize / 7
//                // we need to adjust the size because
//                val offset = weekLayoutInfo.offset - daySize
//                val positionOffsetInWeek = dayOfWeek * daySize
//                val positionToSettle = (viewportSize - daySize) / 2
//                val offsetFromViewPortStart = offset + positionOffsetInWeek
//                val positionDiff = positionToSettle - offsetFromViewPortStart
//                weekCalendarState.animateScrollBy(value = -positionDiff.toFloat())
//            }
//        }
//
//        selectedDate = date
//    }


    val selectDate: (date: LocalDate) -> Unit = { date ->
        scope.launch {
            // Determine the day of the week for the selected date relative to the start of the week (zero-based)
            val dayOfWeek = date.dayOfWeek.isoDayNumber - 1
            val visibleWeek = layoutInfo()
                .visibleWeeksInfo.find { it.week.days.any { weekDay -> weekDay.date == date } }
            val viewportWidth = weekCalendarState.layoutInfo.viewportSize.width

            if (visibleWeek == null) {
                // Selected date is outside the viewport - determine direction
                val firstVisibleDate =
                    weekCalendarState.layoutInfo.visibleWeeksInfo.first().week.days.first().date
                val isScrollingLeft = date < firstVisibleDate

                // Adjust target week based on direction
                val targetDate = if (isScrollingLeft) {
                    // If scrolling left, go to the start of the week before the target
                    date.plus(DatePeriod(days = 7))
                } else {
                    // If scrolling right, go to the start of the week after the target
                    date
                }

                // Scroll to the adjusted target week by date
                weekCalendarState.animateScrollToWeek(targetDate)
                weekCalendarState.animateScrollBy(-5f)
            }

            // After scrolling, find the updated layout info for the week with the selected date
            val updatedWeekLayoutInfo = layoutInfo()
                .visibleWeeksInfo.find { it.week.days.any { weekDay -> weekDay.date == date } }

            if (updatedWeekLayoutInfo != null) {
                val weekOffset = updatedWeekLayoutInfo.offset
                val weekWidth = updatedWeekLayoutInfo.size
                val dayWidth = weekWidth / 7

                // Calculate the offset for the selected day within the week
                val dayOffsetWithinWeek = dayOfWeek * dayWidth
                val targetOffset = weekOffset + dayOffsetWithinWeek

                // Calculate the center position
                val centerOffset = (viewportWidth - dayWidth) / 2

                // Calculate scroll distance to center the selected day
                val scrollDistance = targetOffset - centerOffset

                // Animate scrolling by the computed distance
                weekCalendarState.animateScrollBy(value = scrollDistance.toFloat())
            }
        }

        selectedDate = date
    }



    LaunchedEffect(Unit) {
        selectDate(todayDate)
    }

    Column(
        modifier = modifier,
    ) {
        WeekCalendar(
            state = weekCalendarState,
            calendarScrollPaged = false,
            userScrollEnabled = true,
            reverseLayout = false,
            contentPadding = PaddingValues(),
            dayContent = { weekDay ->
                Day(
                    weekDay = weekDay,
                    selectedDate = selectedDate,
                    onSelectedDate = {
                        selectDate(it)
                    },
                    modifier = Modifier,
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(
                onClick = {
                    selectDate(todayDate)
                },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier,
            ) {
                Text(text = "Today")
            }
        }
    }
}

@Composable
private fun Day(
    selectedDate: LocalDate?,
    weekDay: WeekDay,
    onSelectedDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSelected = selectedDate == weekDay.date
    Column(
        modifier = modifier
            .padding(horizontal = Dimens.Padding.ExtraSmall)
            .width(60.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary else
                    MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small,
            )
            .clickable {
                if (isSelected.not()) {
                    onSelectedDate(weekDay.date)
                }
            }
            .padding(
                vertical = Dimens.Padding.Medium,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val contentColor =
            if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        Text(
            text = weekDay.date.dayOfWeek.getDisplayName(
                java.time.format.TextStyle.SHORT,
                Locale.getDefault(),
            ),
            color = contentColor,
            style = MaterialTheme.typography.bodySmall,
        )

        Text(
            text = weekDay.date.dayOfMonth.toString(),
            color = contentColor,
        )
    }
}


fun LocalDate.adjustedDayOfWeek(startOfWeek: DayOfWeek): DayOfWeek {
    // Get the day of the week for the current date
    val dayOfWeek = this.dayOfWeek

    // Calculate the shift to adjust for the user's start of the week
    val shift = (dayOfWeek.ordinal - startOfWeek.ordinal + 7) % 7

    // Return the adjusted day of the week
    return DayOfWeek.of((startOfWeek.ordinal + shift) % 7 + 1)
}

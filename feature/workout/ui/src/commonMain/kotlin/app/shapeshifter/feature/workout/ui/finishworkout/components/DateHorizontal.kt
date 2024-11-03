package app.shapeshifter.feature.workout.ui.finishworkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.resources.Dimens
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarItemInfo
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.now
import java.time.DayOfWeek
import java.util.Locale
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus

@Composable
fun DateSelector(
    modifier: Modifier = Modifier,
) {
    val todayDate by remember { mutableStateOf(LocalDate.now()) }

    var selectedDate by remember { mutableStateOf(todayDate) }

    val scope = rememberCoroutineScope()

    val weekCalendarState = rememberWeekCalendarState(
        startDate = todayDate.minus(DatePeriod(years = 1)),
        endDate = todayDate.plus(DatePeriod(months = 6)),
        firstDayOfWeek = DayOfWeek.MONDAY,
    )

    val selectAndScrollToDate: (date: LocalDate) -> Unit = { date ->
        scope.launch {
            weekCalendarState.animateScrollToDate(date)
        }
        selectedDate = date
    }

    // for the first time we need.to scroll manually
    LaunchedEffect(Unit) {
        scope.launch {
            weekCalendarState.animateScrollToDate(selectedDate)
        }
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
                        selectAndScrollToDate(it)
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
                    selectAndScrollToDate(todayDate)
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

private suspend fun WeekCalendarState.animateScrollToDate(date: LocalDate) {
    // Determine the day of the week for the selected date relative to the start of the week (zero-based)
    val dayOfWeek = date.dayOfWeek.isoDayNumber - 1
    val viewportWidth = layoutInfo.viewportSize.width

    var weekInfoForDay: WeekCalendarItemInfo?

    val visibleWeek = layoutInfo
        .visibleWeeksInfo.find { it.week.days.any { weekDay -> weekDay.date == date } }

    weekInfoForDay = visibleWeek

    while (weekInfoForDay == null) {
        val firstVisibleDate =
            layoutInfo.visibleWeeksInfo.first().week.days.first().date

        val isScrollingLeft = date < firstVisibleDate

        // check if we can do this without using a constant
        scrollBy((if (isScrollingLeft) -1f else 1f) * 100)

        val weekInView: WeekCalendarItemInfo? = layoutInfo
            .visibleWeeksInfo.find { it.week.days.any { weekDay -> weekDay.date == date } }

        weekInfoForDay = weekInView
    }

    val weekOffset = weekInfoForDay.offset
    val weekWidth = weekInfoForDay.size
    val dayWidth = weekWidth / 7

    // Calculate the offset for the selected day within the week
    val dayOffsetWithinWeek = dayOfWeek * dayWidth
    val targetOffset = weekOffset + dayOffsetWithinWeek

    // Calculate the center position
    val centerOffset = (viewportWidth - dayWidth) / 2

    // Calculate scroll distance to center the selected day
    val scrollDistance = targetOffset - centerOffset

    // Animate scrolling by the computed distance
    animateScrollBy(value = scrollDistance.toFloat())
}

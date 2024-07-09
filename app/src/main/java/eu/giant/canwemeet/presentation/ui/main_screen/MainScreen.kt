package eu.giant.canwemeet.presentation.ui.main_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import eu.giant.canwemeet.data.DayGenerator
import eu.giant.canwemeet.data.Hour
import eu.giant.canwemeet.data.Section
import eu.giant.canwemeet.data.Type
import eu.giant.canwemeet.presentation.ui.main_screen.components.TopBar
import kotlin.math.absoluteValue

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    val dayGenerator = DayGenerator()
    val timeDifference  = 4
    val hours = dayGenerator.homeHours
    val sections = dayGenerator.homeSections

    val otherHours = dayGenerator.calculateTargetHours(timeDifference)
    val otherSections = dayGenerator.removeDuplicate()

    val currentTimeHere = 12
    val currentTimeThere = 15

    Scaffold(
        topBar = {
            TopBar()
        },
        content = {
            BoxWithConstraints {
                val boxWithConstraintsScope = this
                //You can use this scope to get the minWidth, maxWidth, minHeight, maxHeight in dp and constraints
                val rowHeight = (boxWithConstraintsScope.maxHeight).div(26)
                Column {
                    TimeZoneHeaders()
                    Row(modifier = Modifier.fillMaxSize()) {
                        Day(
                            hours = hours,
                            rowHeight = rowHeight,
                            isHere = true,
                            currentHour = currentTimeHere,
                            sections = sections
                        )
                        Day(
                            hours = otherHours,
                            rowHeight = rowHeight,
                            isHere = false,
                            currentHour = currentTimeThere,
                            sections = otherSections,
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun RowScope.Day(hours: List<Hour>, rowHeight: Dp, isHere: Boolean, currentHour: Int, sections: List<Section>) {
    Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.TopCenter) {
        Column() {
            hours.forEach { hour ->
                HourRow(
                    hour = hour,
                    isHere = isHere,
                    color = hour.color,
                    height = rowHeight
                )
            }
        }

        Column {


            sections.forEach {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.height(it.count.times(rowHeight))){
                    if (it.type != Type.Hidden){

                        Icon(
                            it.type.icon,
                            "menu",
                            tint = Color.White,
                            modifier = Modifier
                                .width(56.dp)
                                .height(56.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HourRow(hour: Hour, isHere: Boolean, color: Color, height: Dp) {
    Box(
        contentAlignment = if (isHere) Alignment.CenterEnd else Alignment.CenterStart,
        modifier = Modifier
            .height(height)
            .background(color)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = hour.hour.toString(),
        )
    }
}

@Composable
fun TimeZoneHeaders() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(Color(0xFF009688))
    ) {
        Text(
            text = "Here",
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "London",
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold
        )
    }
}
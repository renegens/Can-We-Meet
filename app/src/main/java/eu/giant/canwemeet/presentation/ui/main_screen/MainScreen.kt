package eu.giant.canwemeet.presentation.ui.main_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Can We Meet", color = Color.White) },
                backgroundColor = Color(0xFF009688),
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = {
            BoxWithConstraints {
                val boxWithConstraintsScope = this
                //You can use this scope to get the minWidth, maxWidth, minHeight, maxHeight in dp and constraints
                val rowHeight = (boxWithConstraintsScope.maxHeight).div(24)
                Column {
                    for (i in 1..24) {

                        if (i == 3 || i== 9 || i == 13 || i == 19) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.height(rowHeight.times(3))){
                                Icon(Icons.Filled.Menu, "menu")
                                Column {
                                    Text(text = i.toString(), modifier = Modifier.height(rowHeight))
                                    Text(text = i.plus(1).toString(), modifier = Modifier.height(rowHeight))
                                    Text(text = i.plus(2).toString(), modifier = Modifier.height(rowHeight))
                                }
                            }
                        }else {
                            if (i > 3){
                                Text(text = i.plus(1).toString(), modifier = Modifier.height(rowHeight))
                            }else{
                                Text(text = i.toString(), modifier = Modifier.height(rowHeight))

                            }
                        }
                    }
                }
                val timedifference = 2

//                Row(modifier = Modifier.fillMaxWidth()) {
//                    Column(modifier = Modifier.weight(0.5f)) {
//                        Spacer(modifier = Modifier.height(rowHeight.times(3)))
//                        Icon(Icons.Filled.Menu, "menu")
//
//                        Spacer(modifier = Modifier.height(rowHeight.times(5)))
//                        Icon(Icons.Filled.Menu, "menu")
//
//                        Spacer(modifier = Modifier.height(rowHeight.times(5)))
//                        Icon(Icons.Filled.Menu, "menu")
//
//                        Spacer(modifier = Modifier.height(rowHeight.times(5)))
//                        Icon(Icons.Filled.Menu, "menu")
//                    }
//
//                    Column(modifier = Modifier
//                        .weight(0.5f)
//                        .offset(y = rowHeight.times(timedifference))) {
//                        if (timedifference > 5) {
//                            Spacer(modifier = Modifier.height(rowHeight.times(5)))
//                            Icon(Icons.Filled.Menu, "menu")
//                        }
//
//                        Spacer(modifier = Modifier.height(rowHeight.times(3)))
//                        Icon(Icons.Filled.Menu, "menu")
//
//                        Spacer(modifier = Modifier.height(rowHeight.times(5)))
//                        Icon(Icons.Filled.Menu, "menu")
//
//                        Spacer(modifier = Modifier.height(rowHeight.times(5)))
//                        Icon(Icons.Filled.Menu, "menu")
//
//                        Spacer(modifier = Modifier.height(rowHeight.times(5)))
//                        Icon(Icons.Filled.Menu, "menu")
//                    }
//                }
//


//                Column {
//                    if (boxWithConstraintsScope.maxHeight >= 200.dp) {
//
//                    }
//                    Text("minHeight: ${boxWithConstraintsScope.minHeight}, maxHeight: ${boxWithConstraintsScope.maxHeight},  minWidth: ${boxWithConstraintsScope.minWidth} maxWidth: ${boxWithConstraintsScope.maxWidth}")
//                }
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                ) {
//                    TimeZoneHeaders()
//                    TimeZoneRows()
//                }
            }

        }
    )
}

@Composable
fun TimeZoneHeaders() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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

@Composable
fun TimeZoneRows() {
    val timeBlocks = listOf(
        TimeBlock(1, 3, Red),
        TimeBlock(8, 10, Gray),
        TimeBlock(11, 13, Blue),
        TimeBlock(18, 20, Green)
    )

    val times = (1..24).toList()

    Column {
        times.forEach { hour ->
            val block = timeBlocks.find { hour in it.start until it.end }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(block?.color ?: Color.Transparent)
                    .padding(vertical = 6.dp)
            ) {
                Text(
                    text = hour.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    fontSize = 16.sp,
                    color = if (block != null) Color.White else Color.Black
                )
                Text(
                    text = (hour + 1).toString(),
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    fontSize = 16.sp,
                    color = if (block != null) Color.White else Color.Black
                )
            }
        }
    }
}

data class TimeBlock(val start: Int, val end: Int, val color: Color)
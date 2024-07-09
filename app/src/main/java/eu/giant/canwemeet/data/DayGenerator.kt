package eu.giant.canwemeet.data

import eu.giant.canwemeet.presentation.ui.theme.BlueGrey300
import eu.giant.canwemeet.presentation.ui.theme.Brown200
import eu.giant.canwemeet.presentation.ui.theme.Red300
import eu.giant.canwemeet.presentation.ui.theme.Teal200
import kotlin.math.absoluteValue

class DayGenerator {

//    val homeHours = listOf<Hour>(
//        Hour(hour = 1, Item.Row, Red300),
//        Hour(hour = 2, Item.Row, Red300),
//        Hour(hour = 3, Item.Box, Red300),
//        Hour(hour = 6, Item.Row, Red300),
//        Hour(hour = 7, Item.Row, Red300),
//        Hour(hour = 8, Item.Box, Brown200),
//        Hour(hour = 11, Item.Row, Teal200),
//        Hour(hour = 12, Item.Row, Teal200),
//        Hour(hour = 13, Item.Box, Teal200),
//        Hour(hour = 16, Item.Row, Teal200),
//        Hour(hour = 17, Item.Row, BlueGrey300),
//        Hour(hour = 18, Item.Row, BlueGrey300),
//        Hour(hour = 19, Item.Row, BlueGrey300),
//        Hour(hour = 20, Item.Box, BlueGrey300),
//        Hour(hour = 23, Item.Row, BlueGrey300),
//        Hour(hour = 24, Item.Row, BlueGrey300),
//    )

    val homeHours = listOf<Hour>(
        Hour(hour = 1, Type.Sleep, Red300),
        Hour(hour = 2, Type.Sleep, Red300),
        Hour(hour = 3, Type.Sleep, Red300),
        Hour(hour = 4, Type.Sleep, Red300),
        Hour(hour = 5, Type.Sleep, Red300),
        Hour(hour = 6, Type.Sleep, Red300),
        Hour(hour = 7, Type.Sleep, Red300),
        Hour(hour = 8, Type.Coffee, Brown200),
        Hour(hour = 9, Type.Coffee, Brown200),
        Hour(hour = 10, Type.Coffee, Brown200),
        Hour(hour = 11, Type.Work, Teal200),
        Hour(hour = 12, Type.Work, Teal200),
        Hour(hour = 13, Type.Work, Teal200),
        Hour(hour = 14, Type.Work, Teal200),
        Hour(hour = 15, Type.Work, Teal200),
        Hour(hour = 16, Type.Work, Teal200),
        Hour(hour = 17, Type.Home, BlueGrey300),
        Hour(hour = 18, Type.Home, BlueGrey300),
        Hour(hour = 19, Type.Home, BlueGrey300),
        Hour(hour = 20, Type.Home, BlueGrey300),
        Hour(hour = 21, Type.Home, BlueGrey300),
        Hour(hour = 22, Type.Home, BlueGrey300),
        Hour(hour = 23, Type.Home, BlueGrey300),
        Hour(hour = 24, Type.Home, BlueGrey300),
    )

    val homeSections = listOf(
        Section(type = Type.Sleep, count = 7),
        Section(type = Type.Coffee, count = 3),
        Section(type = Type.Work, count = 6),
        Section(type = Type.Home, count = 8),
    )


    var targetSections = mutableListOf<Section>()

    fun removeDuplicate(): List<Section> {

        val isFirstBigger = targetSections.first().count > targetSections.last().count

        if (isFirstBigger) {
            targetSections.last().type = Type.Hidden
        } else {
            targetSections.first().type = Type.Hidden
        }

        return targetSections

    }


    fun calculateTargetHours(timeDifference: Int): List<Hour> {
        if (timeDifference == 0) {
            return homeHours
        }

        if (timeDifference > 0) {
            val itemsToMove = homeHours.subList(0, timeDifference)
            val firstThreeCopy = itemsToMove.toList() // Copy to avoid concurrent modification
            val otherHours = homeHours.toMutableList()
            otherHours.removeAll(itemsToMove)

            val boxes = otherHours.groupBy { it.type }
            val boxesCount = mutableListOf<Section>()
            boxes.forEach {
                boxesCount.add(Section(it.key, it.value.count()))
            }
            boxesCount.add(Section(firstThreeCopy.first().type, firstThreeCopy.count()))
            targetSections = boxesCount
            otherHours.addAll(firstThreeCopy)

            return otherHours
        } else {
            val startIndex = 24 - timeDifference.absoluteValue

            val itemsToMove = homeHours.slice(startIndex..23)
            val remainingItems = homeHours.slice(0..<startIndex)
            val lastItems = itemsToMove.toList() // Copy to avoid concurrent modification
            val newList = mutableListOf<Hour>()

            val boxes = remainingItems.groupBy { it.type }
            val boxesCount = mutableListOf<Section>()
            boxesCount.add(Section(itemsToMove.first().type, itemsToMove.count()))
            boxes.forEach {
                boxesCount.add(Section(it.key, it.value.count()))
            }
            targetSections = boxesCount

            newList.addAll(lastItems)
            newList.addAll(remainingItems)
            return newList
        }

    }
}
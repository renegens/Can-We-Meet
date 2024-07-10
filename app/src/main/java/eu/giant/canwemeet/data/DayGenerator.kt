package eu.giant.canwemeet.data

import eu.giant.canwemeet.presentation.ui.theme.BlueGrey300
import eu.giant.canwemeet.presentation.ui.theme.Brown200
import eu.giant.canwemeet.presentation.ui.theme.Red300
import eu.giant.canwemeet.presentation.ui.theme.Teal200
import kotlin.math.absoluteValue

class DayGenerator {

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

    private fun List<Section>.removeDuplicate() {
        if (first().type != last().type) {
            return
        }

        val isFirstBigger = first().count > last().count

        if (isFirstBigger) {
            last().type = Type.Hidden
        } else {
            first().type = Type.Hidden
        }
    }

    fun calculateTargets(timeDifference: Int): Target {
        if (timeDifference == 0) {
            return Target(
                hours = homeHours,
                sections = homeSections
            )
        }

        if (timeDifference > 0) {
            val itemsToMove = homeHours.slice(0..timeDifference).toList()
            val allItems = homeHours.toMutableList()
            allItems.removeAll(itemsToMove)

            val sections = mutableListOf<Section>()
            allItems.groupBy { it.type }.forEach {
                sections.add(Section(it.key, it.value.count()))
            }
            itemsToMove.groupBy { it.type }.forEach {
                sections.add(Section(it.key, it.value.count()))
            }

            sections.removeDuplicate()
            allItems.addAll(itemsToMove)

            return Target(
                hours = allItems,
                sections = sections
            )
        } else {
            val startIndex = 24 - timeDifference.absoluteValue

            val itemsToMove = homeHours.slice(startIndex..23)
            val remainingItems = homeHours.slice(0..<startIndex)
            val sections = mutableListOf<Section>()

            itemsToMove.groupBy { it.type }.forEach {
                sections.add(Section(it.key, it.value.count()))
            }
            remainingItems.groupBy { it.type }.forEach {
                sections.add(Section(it.key, it.value.count()))
            }
            sections.removeDuplicate()

            return Target(
                hours = (itemsToMove + remainingItems).toMutableList(),
                sections = sections
            )
        }
    }
}
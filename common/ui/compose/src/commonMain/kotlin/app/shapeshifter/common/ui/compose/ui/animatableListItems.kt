package app.shapeshifter.common.ui.compose.ui

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList

/**
 * Utility to enable animating additions/deletions of list items.
 *
 * This works by converting the given list of [items] into a [SnapshotStateList] of the items, giving each one a
 * [MutableTransitionState] that can be used as the `visibleState` parameter to [AnimatedVisibility].
 * When items in the underlying [items] list are deleted, they will not be removed from the [SnapshotStateList]
 * until their deletion animation has completed. When new items are added to the underlying list, they will be added
 * to the [SnapshotStateList] in a state that will trigger the add animation.
 *
 * In order to convert the list items into animatable list items, each item needs a unique identifier. This way, when a
 * list item changes, but maintains the same ID, it will not trigger an animation of the old version being removed and
 * the new version being added. The [id] function you provide will be used for getting this unique identifier from an item.
 *
 * Adapted from Google's [AnimatedVisibilityLazyColumnDemo.kt](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/animation/animation/integration-tests/animation-demos/src/main/java/androidx/compose/animation/demos/layoutanimation/AnimatedVisiblilityLazyColumnDemo.kt)
 */
@Composable
fun <K, V> animatableListItems(
    items: List<V>,
    id: (item: V) -> K,
): SnapshotStateList<AnimatableListItem<K, V>> {
    val animatableItems = remember {
        items.map { item ->
            AnimatableListItem(
                id(item),
                item,
                visibilityState = VisibilityAnimationState.Visible,
            )
        }.toMutableStateList()
    }
    val updatedItems = rememberUpdatedState(items.associateBy(id))

    LaunchedEffect(updatedItems) {
        snapshotFlow { updatedItems.value }.collect {
            val newAnimatableItems = mutableListOf<AnimatableListItem<K, V>>()
            val unaddedItems = updatedItems.value.toMutableMap()

            animatableItems.forEach { animatableItem ->
                if (animatableItem.visibilityState != VisibilityAnimationState.Invisible) {
                    val updatedItem = updatedItems.value[animatableItem.id]
                    val newAnimatableItem: AnimatableListItem<K, V> =
                        if (updatedItem == null) {
                            AnimatableListItem(
                                animatableItem.id,
                                animatableItem.item,
                                visibilityState = VisibilityAnimationState.Exiting,
                            )
                        } else {
                            unaddedItems.remove(animatableItem.id)
                            AnimatableListItem(
                                animatableItem.id,
                                updatedItem,
                                animatableItem.visibilityState,
                            )
                        }
                    newAnimatableItems.add(newAnimatableItem)
                }
            }
            val itemsToAnimateIn = unaddedItems.map { (id, item) ->
                AnimatableListItem(id, item, visibilityState = VisibilityAnimationState.Entering)
            }
            newAnimatableItems.addAll(itemsToAnimateIn)

            // we completely clear and re-add all items as to minimize the number of times we
            // mutate the SnapshotStateList, to avoid triggering unnecessary recompositions
            animatableItems.clear()
            animatableItems.addAll(newAnimatableItems)
        }
    }
    return animatableItems
}

/**
 * A wrapper for a list item which provides a [transitionState] that can be used to animate the item
 * being removed from or added to an underlying list. Do not instantiate this class directly,
 * but rather use the utility function [animatableListItems] to convert your list into an animatable list.
 */
class AnimatableListItem<K, V>(val id: K, val item: V, visibilityState: VisibilityAnimationState) {

    val transitionState = when (visibilityState) {
        VisibilityAnimationState.Entering -> MutableTransitionState(initialState = false).apply {
            targetState = true
        }

        VisibilityAnimationState.Visible -> MutableTransitionState(initialState = true)
        VisibilityAnimationState.Exiting -> MutableTransitionState(initialState = true).apply {
            targetState = false
        }

        VisibilityAnimationState.Invisible -> MutableTransitionState(initialState = false)
    }

    /** Describes the current animation state in a more readable way than referencing [transitionState] */
    val visibilityState
        get() = with(transitionState) {
            when {
                isIdle && targetState -> VisibilityAnimationState.Visible
                isIdle && !targetState -> VisibilityAnimationState.Invisible
                !isIdle && targetState -> VisibilityAnimationState.Entering
                else -> VisibilityAnimationState.Exiting
            }
        }
}

enum class VisibilityAnimationState {
    Entering,
    Visible,
    Exiting,
    Invisible
}

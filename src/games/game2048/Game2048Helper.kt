package games.game2048

import java.util.function.Function

/*
 * This function moves all the non-null elements to the beginning of the list
 * (by removing nulls) and merges equal elements.
 * The parameter 'merge' specifies the way how to merge equal elements:
 * it returns a new element that should be present in the resulting list
 * instead of two merged elements.
 *
 * If the function 'merge("a")' returns "aa",
 * then the function 'moveAndMergeEqual' transforms the input in the following way:
 *   a, a, b -> aa, b
 *   a, null -> a
 *   b, null, a, a -> b, aa
 *   a, a, null, a -> aa, a
 *   a, null, a, a -> aa, a
 *
 * You can find more examples in 'TestGame2048Helper'.
*/
fun <T : Any> List<T?>.moveAndMergeEqual(merge: (T) -> T): List<T> {
    val notNullList = this.filterNotNull()
    if (notNullList.isEmpty() || notNullList.size == 1) return notNullList

    val mergeIfNextIsSame = { list: List<T> ->
        when {
            list.size == 1 -> listOf(list[0])
            list[0] == list[1] -> listOf(merge(list[0]))
            else -> list.toList()
        }
    }

    val firstPass = notNullList.chunked(2, mergeIfNextIsSame).flatten()
    val secondPass = firstPass.subList(1, firstPass.size).chunked(2, mergeIfNextIsSame).flatten()
    return listOf(listOf(firstPass[0]), secondPass).flatten()
}

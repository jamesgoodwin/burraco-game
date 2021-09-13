class HandCombinations {

    fun printCombinations() {
        val combinations = mutableSetOf<String>()

        for (i in 3..13) { // number of cards in a run
            for (j in 0..14 - i) {
                // get 0 .. 2 cards
                val cards = PlayingCard.Value.values().toMutableList()
                    .minus(PlayingCard.Value.JOKER)
                    .plus(PlayingCard.Value.ACE)
                    .subList(j, j + i)

                printAndAddToCombinations(cards, combinations, false)

                for (card in cards) {
                    val copy = cards.toMutableList()
                    val index = copy.indexOf(card)
                    copy[index] = PlayingCard.Value.TWO
                    printAndAddToCombinations(copy, combinations, true)

                    if(!cards.contains(PlayingCard.Value.JOKER)) {
                        copy[index] = PlayingCard.Value.JOKER
                        printAndAddToCombinations(copy, combinations, true)
                    }
                }
            }
        }

        println("No. of combinations: ${combinations.size}")
    }

    private fun printAndAddToCombinations(cards: List<PlayingCard.Value>, set: MutableSet<String>, wildcards: Boolean) {
        val burraco = cards.size >= 7

        val burracoType = if(burraco) if(!wildcards) "clean" else "dirty" else "no burraco"

        println(cards.joinToString(separator = "", transform = { it.symbol }) + ", burraco: $burracoType")

        val copy = cards.toMutableList()
        copy.sort()

        set.add(copy.joinToString(separator = "", transform = { it.symbol }))
    }




}
class ExistingMeldMove(
    val meldValidator: MeldValidator,
    val meldToExistingSummary: MeldAttempt,
    val state: State
) : Move {

    override fun performMove(): Boolean {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        val handCardsUsed = meldToExistingSummary.handCardsUsed.joinToString(",")
        val existingMeld = meldToExistingSummary.existingMeld.joinToString(",")
        return "Add $handCardsUsed to existing meld: $existingMeld"
    }

}

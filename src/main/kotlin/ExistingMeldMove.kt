class ExistingMeldMove(
    val meldValidator: MeldValidator,
    private val meldToExistingSummary: MeldAttempt,
    val state: State
) : MeldMove {

    override fun performMove(): Boolean {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        val handCardsUsed = meldToExistingSummary.handCardsUsed.joinToString(",")
        val existingMeld = meldToExistingSummary.existingMeld.joinToString(",")
        return "Add $handCardsUsed to existing meld: $existingMeld"
    }

}

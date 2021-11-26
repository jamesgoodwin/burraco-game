import PlayingCard.Suit.SPADE
import PlayingCard.Value.ACE
import org.junit.Ignore
import org.junit.Test
import player.HumanPlayer
import kotlin.test.assertTrue

internal class StateTest {

    @Test
    fun shouldHaveBurracoWhenPlayerHasSevenCardMeld() {
        val bill = HumanPlayer("bill")
        val state = State(listOf(bill, HumanPlayer("bob")))
        val meld = mutableListOf<PlayingCard>()
        repeat(7) {
            meld.add(PlayingCard(ACE, SPADE))
        }

        state.melds[bill]?.add(meld)
        assertTrue(state.hasBurraco(bill))
    }

    @Test
    fun shouldHaveBurraco() {
        val bill = HumanPlayer("bill")
        val state = State(listOf(bill, HumanPlayer("bob")))
        val meld = mutableListOf<PlayingCard>()
        repeat(7) {
            meld.add(PlayingCard(ACE, SPADE))
        }

        state.melds[bill]?.add(meld)
        assertTrue(state.burracos(bill).isNotEmpty())
    }

}
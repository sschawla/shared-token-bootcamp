package bootcamp;

import net.corda.core.contracts.ContractState;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.testing.core.TestIdentity;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StateTests {
    private final Party jane = new TestIdentity(new CordaX500Name("Jane", "", "GB")).getParty();
    private final Party john = new TestIdentity(new CordaX500Name("John", "", "GB")).getParty();
    private final Party boom = new TestIdentity(new CordaX500Name("Boom", "Bangkok", "TH")).getParty();
    private final Party bob = new TestIdentity(new CordaX500Name("Bob", "", "GB")).getParty();
    private final Party ern = new TestIdentity(new CordaX500Name("Ern", "Bangkok", "TH")).getParty();
    private final Party nai = new TestIdentity(new CordaX500Name("Nai", "Bangkok", "TH")).getParty();

    private final Party ice = new TestIdentity(new CordaX500Name("Ice", "Bangkok", "TH")).getParty();
//    @Test
//    public void tokenStateHasIssuerOwnerAndAmountParamsOfCorrectTypeInConstructor() {
//        new TokenState(alice, bob, 1);
//    }
//
//    @Test
//    public void tokenStateHasGettersForIssuerOwnerAndAmount() {
//        TokenState tokenState = new TokenState(alice, bob, 1);
//        assertEquals(alice, tokenState.getIssuer());
//        assertEquals(bob, tokenState.getOwner());
//        assertEquals(1, tokenState.getAmount());
//    }
//
//    @Test
//    public void tokenStateImplementsContractState() {
//        assertTrue(new TokenState(alice, bob, 1) instanceof ContractState);
//    }
//
//    @Test
//    public void tokenStateHasTwoParticipantsTheIssuerAndTheOwner() {
//        TokenState tokenState = new TokenState(alice, bob, 1);
//        assertEquals(2, tokenState.getParticipants().size());
//        assertTrue(tokenState.getParticipants().contains(alice));
//        assertTrue(tokenState.getParticipants().contains(bob));
//    }
}
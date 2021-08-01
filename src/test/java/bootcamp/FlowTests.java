package bootcamp;

import net.corda.core.concurrent.CordaFuture;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.TransactionState;
import net.corda.core.transactions.SignedTransaction;
import net.corda.testing.node.MockNetwork;
import net.corda.testing.node.MockNetworkParameters;
import net.corda.testing.node.StartedMockNode;
import net.corda.testing.node.TestCordapp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class FlowTests {
    private MockNetwork network;
    private StartedMockNode nodeA;
    private StartedMockNode nodeB;

    @Before
    public void setup() {
        network = new MockNetwork(
                new MockNetworkParameters(
                        Collections.singletonList(TestCordapp.findCordapp("bootcamp"))
                )
        );
        nodeA = network.createPartyNode(null);
        nodeB = network.createPartyNode(null);
        network.runNetwork();
    }

    @After
    public void tearDown() {
        network.stopNodes();
    }

    @Test //Q1
    public void transactionConstructedByFlowUsesTheCorrectNotary() throws Exception {
        TokenIssueFlowInitiator flow = new TokenIssueFlowInitiator(nodeB.getInfo().getLegalIdentities().get(0), 99); //owner and amount
        CordaFuture<SignedTransaction> future = nodeA.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

        //making sure theres exactly one output state
        assertEquals(1, signedTransaction.getTx().getOutputStates().size());
        //make an output state object of type TransactionState and assign output of the signedtransaction to it
        TransactionState output = signedTransaction.getTx().getOutputs().get(0);
        //making sure that the notary used is the same as the first notary (probably by default)
        assertEquals(network.getNotaryNodes().get(0).getInfo().getLegalIdentities().get(0), output.getNotary());
    }

    @Test //Q2
    public void transactionConstructedByFlowHasOneTokenStateOutputWithTheCorrectAmountAndOwner() throws Exception {
        TokenIssueFlowInitiator flow = new TokenIssueFlowInitiator(nodeB.getInfo().getLegalIdentities().get(0), 99); //owner and amount
        CordaFuture<SignedTransaction> future = nodeA.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

        //making sure that theres exactly one output state
        assertEquals(1, signedTransaction.getTx().getOutputStates().size());
        //output state of type TokenState assigned with the value of .....
        TokenState output = signedTransaction.getTx().outputsOfType(TokenState.class).get(0);
        //making sure that the owner in the output state is the same as nodeB identity whos supposed to be the owner
        assertEquals(nodeB.getInfo().getLegalIdentities().get(0), output.getOwner());
        //making sure the amount in the output state is the same as what was given to the initiator
        assertEquals(99, output.getAmount());
    }

    @Test //Q3
    public void transactionConstructedByFlowHasOneOutputUsingTheCorrectContract() throws Exception {
        TokenIssueFlowInitiator flow = new TokenIssueFlowInitiator(nodeB.getInfo().getLegalIdentities().get(0), 99);
        CordaFuture<SignedTransaction> future = nodeA.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

        //making sure that theres only 1 output state
        assertEquals(1, signedTransaction.getTx().getOutputStates().size());
        //making an instant output object for the next line
        TransactionState output = signedTransaction.getTx().getOutputs().get(0);
        //making sure the contract in the output is the same as that one
        assertEquals("bootcamp.TokenContract", output.getContract());
    }
//
    @Test //Q4
    public void transactionConstructedByFlowHasOneIssueCommand() throws Exception {
        TokenIssueFlowInitiator flow = new TokenIssueFlowInitiator(nodeB.getInfo().getLegalIdentities().get(0), 99);
        CordaFuture<SignedTransaction> future = nodeA.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

        //expecting only 1 command in the signedtransaction
        assertEquals(1, signedTransaction.getTx().getCommands().size());
        //making an instant object command for the next line
        Command command = signedTransaction.getTx().getCommands().get(0);
        //making sure the the command is an issue command (but why getValue()?
        assert (command.getValue() instanceof TokenContract.Commands.Issue);
    }
//
    @Test //Q5
    public void transactionConstructedByFlowHasOneCommandWithTheIssuerAndTheOwnerAsASigners() throws Exception {
        TokenIssueFlowInitiator flow = new TokenIssueFlowInitiator(nodeB.getInfo().getLegalIdentities().get(0), 99);
        CordaFuture<SignedTransaction> future = nodeA.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

        assertEquals(1, signedTransaction.getTx().getCommands().size()); //expects only 1 command
        Command command = signedTransaction.getTx().getCommands().get(0); //get that command and assign to "command"

        assertEquals(2, command.getSigners().size()); //making sure there are two signers
        assertTrue(command.getSigners().contains(nodeA.getInfo().getLegalIdentities().get(0).getOwningKey()));
        //making sure that nodeA key is in the command signers list
        assertTrue(command.getSigners().contains(nodeB.getInfo().getLegalIdentities().get(0).getOwningKey()));
        //making sure that nodeB key is in the command signers list
    }

    @Test
    public void transactionConstructedByFlowHasNoInputsAttachmentsOrTimeWindows() throws Exception {
        TokenIssueFlowInitiator flow = new TokenIssueFlowInitiator(nodeB.getInfo().getLegalIdentities().get(0), 99);
        CordaFuture<SignedTransaction> future = nodeA.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

        //0 input
        assertEquals(0, signedTransaction.getTx().getInputs().size());
        //The single attachment is the contract attachment
        assertEquals(1, signedTransaction.getTx().getAttachments().size());
        //no time window
        assertNull(signedTransaction.getTx().getTimeWindow());
    }
}
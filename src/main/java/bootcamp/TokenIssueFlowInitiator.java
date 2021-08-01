package bootcamp;

import co.paralleluniverse.fibers.Suspendable;
import com.google.common.collect.ImmutableList;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import net.corda.core.contracts.CommandData;

import java.security.PublicKey;
import java.util.List;

import static java.util.Collections.singletonList;

@InitiatingFlow
@StartableByRPC
public class TokenIssueFlowInitiator extends FlowLogic<SignedTransaction> {
    private final Party owner;
    private final int amount;

    public TokenIssueFlowInitiator(Party owner, int amount) { //Q2
        this.owner = owner;
        this.amount = amount;
    }

    private final ProgressTracker progressTracker = new ProgressTracker();

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // We choose our transaction's notary (the notary prevents double-spends).
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
        // We get a reference to our own identity.
        Party issuer = getOurIdentity();

        /* ============================================================================
         *         TODO 1 - Create our TokenState to represent on-ledger tokens!
         * ===========================================================================*/
        // We create our new TokenState.
        TokenState tokenState = new TokenState(issuer, owner, amount);


        /* ============================================================================
         *      TODO 3 - Build our token issuance transaction to update the ledger!
         * ===========================================================================*/
        // We build our transaction.
        TransactionBuilder txBuilder = new TransactionBuilder(notary); //Q1
        txBuilder.addOutputState(tokenState); //tokenState is added to transaction builder as the outputstate
        //in this case theres no input

        TokenContract.Commands.Issue commandData = new TokenContract.Commands.Issue(); //instantiating an issue command called commandData //Q4
        List<PublicKey> requiredSigners = ImmutableList.of(issuer.getOwningKey(), owner.getOwningKey()); //make a list of type "publickey" called requiredsigners //Q5
        //to declare required signers being both the issuer and signers and the list is immutable
        txBuilder.addCommand(commandData,requiredSigners);//adding command to the transaction builder where the inputs are data and keys required
        //this line also make Q3 pass because it assigns issue command that belongs to TokenContract to txbuilder

        /* ============================================================================
         *          TODO 2 - Write our TokenContract to control token issuance!
         * ===========================================================================*/
        // We check our transaction is valid based on its contracts.
        txBuilder.verify(getServiceHub()); //this verify() goes into the tokencontract class input is of type "ledgertransaction" being the "tx" in the method
        //getServiceHub becomes tx inside the verify method

        FlowSession session = initiateFlow(owner);

        // We sign the transaction with our private key, making it immutable.
        SignedTransaction signedTransaction = getServiceHub().signInitialTransaction(txBuilder);

        // The counterparty signs the transaction
        SignedTransaction fullySignedTransaction = subFlow(new CollectSignaturesFlow(signedTransaction, singletonList(session)));

        // We get the transaction notarised and recorded automatically by the platform.
        return subFlow(new FinalityFlow(fullySignedTransaction, singletonList(session)));
    }
}

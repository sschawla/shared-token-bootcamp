package bootcamp;

import examples.ArtContract;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;
import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;    
import static net.corda.core.contracts.ContractsDSL.requireThat;

import java.util.List;

/* Our contract, governing how our state will evolve over time.
 * See src/main/java/examples/ArtContract.java for an example. */
public class TokenContract implements Contract {

    public interface Commands extends CommandData {
        class Issue implements Commands { }
        class Settle implements Commands { }
    }

    public static String ID = "bootcamp.TokenContract";


        public void verify(LedgerTransaction tx) throws IllegalArgumentException { //verify transaction just how contract works
            CommandWithParties<Commands> command = requireSingleCommand(tx.getCommands(), Commands.class);

            if (command.getValue() instanceof Commands.Issue) { //checking condition #1
//                TokenState instaState = (TokenState)tx.getOutputStates().get(0);


                // Issue transaction rules...
                requireThat(req -> {
                    req.using("No inputs should be consumed when issuing Token State.", tx.getInputStates().size() == 0); //size checking for input state
                    req.using("Only one output state is allowed.", tx.getOutputStates().size() == 1); //size checking for output state
                    req.using("Not a token state", tx.getOutputStates().get(0) instanceof TokenState); //will be redundant if outputTokenState is used here

                    final TokenState outputTokenState = tx.outputsOfType(TokenState.class).get(0);

                    req.using("Output Negative/Zero",outputTokenState.getAmount() > 0);
                    req.using("Required signer is not an issuer",command.getSigners().contains(outputTokenState.getIssuer().getOwningKey()));
                    return null;

                });


            }else if (command.getValue() instanceof Commands.Settle){ //not required according to the test form

            }else{

            }

        }

}

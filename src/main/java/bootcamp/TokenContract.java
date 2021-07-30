package bootcamp;

import examples.ArtContract;
import examples.ArtState;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;
import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;    
import static net.corda.core.contracts.ContractsDSL.requireThat;

import java.security.PublicKey;
import java.util.List;

/* Our contract, governing how our state will evolve over time.
 * See src/main/java/examples/ArtContract.java for an example. */
public class TokenContract implements Contract {

    public static String ID = "bootcamp.TokenContract";

// Implement command
    public interface Commands extends CommandData {
        class Issue implements Commands { }
        class Settle implements Commands { }
    }

        public void verify(LedgerTransaction tx) throws IllegalArgumentException {
            CommandWithParties<Commands> command = requireSingleCommand(tx.getCommands(), Commands.class);

            if (command.getValue() instanceof TokenContract.Commands.Issue) {


                requireThat (require -> {
                    require.using("No inputs should be consumed when issuing Token State.",tx.getInputStates().size()==0);
                    require.using("Only one outputs state is allowed.",tx.getOutputStates().size()==1);
                    //require.using("Wrong Output Type", outputTokenState instanceof TokenState);
                    require.using("Wrong Output Type", tx.getOutputStates().get(0) instanceof TokenState );

                    final TokenState outputTokenState =  tx.outputsOfType(TokenState.class).get(0);
                    final List<PublicKey> requiredSigners = command.getSigners();

                    require.using("Output Negative or Zero", outputTokenState.getAmount() > 0);
                    //require.using("Required signer is not an issuer",command.getSigners().contains(outputTokenState.getIssuer().getOwningKey()));
                    require.using("Required signer is not an issuer",requiredSigners.contains(outputTokenState.getIssuer().getOwningKey()));




//              return everything follow method because public void
                    return  null;
                });

 //             if (tx.getInputStates().size() != 0) throw new IllegalArgumentException("Art transfer should have one inputs.");


            } else if (command.getValue() instanceof Commands.Settle) {

            }else{
                throw new IllegalArgumentException("Invalid command");
            }
        }


}

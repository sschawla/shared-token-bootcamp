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


        public void verify(LedgerTransaction tx) throws IllegalArgumentException {
            CommandWithParties<Commands> command = requireSingleCommand(tx.getCommands(), Commands.class);

            if (command.getValue() instanceof Commands.Issue) {
                // Issue transaction rules...
                requireThat(req -> {
                    req.using("No inputs should be consumed when issuing Token State.", tx.getInputStates().size() == 0);
                    req.using("Only one output state is allowed.", tx.getOutputs().size() == 1);
                    return null;
                });


            }else if (command.getValue() instanceof Commands.Settle){
                ///sadsadsadsad

            }else{

            }

        }

}

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

    public static String ID = "bootcamp.TokenContract";

// Implement command
    public interface Commands extends CommandData {
        class Issue implements Commands { }
        class Settle implements Commands { }
    }

        public void verify(LedgerTransaction tx) throws IllegalArgumentException {
            CommandWithParties<TokenContract.Commands> command = requireSingleCommand(tx.getCommands(), Commands.class);

            if (command.getValue() instanceof ArtContract.Commands.Issue) {

                requireThat (require -> {
                    require.using("No inputs should be consumed when issuing Token State.",tx.getInputStates().size()==0);
                    require.using("Only one outputs state is allowed.",tx.getOutputs().size()==1);
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

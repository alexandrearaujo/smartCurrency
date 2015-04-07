package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

import net.sf.ofx4j.domain.data.MessageSetType;
import net.sf.ofx4j.domain.data.ResponseEnvelope;
import net.sf.ofx4j.domain.data.ResponseMessageSet;
import net.sf.ofx4j.domain.data.banking.BankStatementResponseTransaction;
import net.sf.ofx4j.domain.data.banking.BankingResponseMessageSet;
import net.sf.ofx4j.domain.data.common.Transaction;
import net.sf.ofx4j.domain.data.signon.SignonResponse;
import net.sf.ofx4j.io.AggregateUnmarshaller;
import net.sf.ofx4j.io.OFXParseException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TesteApplication {

    public static void main(String[] args) {
        SpringApplication.run(TesteApplication.class, args);
        
        AggregateUnmarshaller<ResponseEnvelope> a = new AggregateUnmarshaller<ResponseEnvelope>(ResponseEnvelope.class);
        ResponseEnvelope re = null;
		try {
			FileInputStream inputStream = new FileInputStream(new File(System.getProperty("user.home") + "/Downloads/extrato.ofx"));
			Reader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
			re = (ResponseEnvelope) a.unmarshal(reader);
		} catch (IOException | OFXParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //objeto contendo informações como instituição financeira, idioma, data da conta.
        SignonResponse sr = re.getSignonResponse();
        System.out.println("Banco: " + sr.getFinancialInstitution().getOrganization());

        //como não existe esse get "BankStatementResponse bsr = re.getBankStatementResponse();"
        //fiz esse codigo para capturar a lista de transações
        MessageSetType type = MessageSetType.banking;
        ResponseMessageSet message = re.getMessageSet(type);

        if (message != null) {
            List<BankStatementResponseTransaction> bank = ((BankingResponseMessageSet) message).getStatementResponses();
            for (BankStatementResponseTransaction b : bank) {
                System.out.println("cc: " + b.getMessage().getAccount().getAccountNumber());
                System.out.println("ag: " + b.getMessage().getAccount().getBranchId());
                System.out.println("balanço final: " + b.getMessage().getLedgerBalance().getAmount());
                System.out.println("dataDoArquivo: " + b.getMessage().getLedgerBalance().getAsOfDate());
                List<Transaction> list = b.getMessage().getTransactionList().getTransactions();
                System.out.println("TRANSAÇÕES\n");
                for (Transaction transaction : list) {
                    System.out.println("tipo: " + transaction.getTransactionType().name());
                    System.out.println("id: " + transaction.getId());
                    System.out.println("data: " + transaction.getDatePosted());
                    System.out.println("valor: " + transaction.getAmount());
					System.out.println("descricao: " + transaction.getMemo());
                    System.out.println("");
                }
            }
        }
    }
}

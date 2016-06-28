package demo.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.model.CreditCard;
import demo.model.CreditCardBill;
import demo.model.Entry;
import demo.repository.CreditCardBillRepository;
import lombok.RequiredArgsConstructor;
import net.sf.ofx4j.domain.data.MessageSetType;
import net.sf.ofx4j.domain.data.ResponseEnvelope;
import net.sf.ofx4j.domain.data.ResponseMessageSet;
import net.sf.ofx4j.domain.data.banking.BankStatementResponseTransaction;
import net.sf.ofx4j.domain.data.banking.BankingResponseMessageSet;
import net.sf.ofx4j.domain.data.common.Transaction;
import net.sf.ofx4j.domain.data.creditcard.CreditCardResponseMessageSet;
import net.sf.ofx4j.domain.data.creditcard.CreditCardStatementResponseTransaction;
import net.sf.ofx4j.domain.data.signon.SignonResponse;
import net.sf.ofx4j.io.AggregateUnmarshaller;
import net.sf.ofx4j.io.OFXParseException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreditCardBillService {
	
	private final CreditCardBillRepository creditCardBillRepository;
	private CreditCardBill creditCardBill;
	
	@Transactional
	public CreditCardBill teste() {
		creditCardBill = creditCardBillRepository.findOne(1L);
		
		if (creditCardBill != null) {
			return creditCardBill;
		}
		
		AggregateUnmarshaller<ResponseEnvelope> a = new AggregateUnmarshaller<ResponseEnvelope>(ResponseEnvelope.class);
        ResponseEnvelope re = null;
		try {
			FileInputStream inputStream = new FileInputStream(new File(System.getProperty("user.home") + "/Downloads/SARAIVA-Jul-16.ofx"));
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
        } else {
        	type = MessageSetType.creditcard;
        	message = re.getMessageSet(type);
        	
        	if (message != null) {
        		List<CreditCardStatementResponseTransaction> bank = ((CreditCardResponseMessageSet) message).getStatementResponses();
            	for (CreditCardStatementResponseTransaction b : bank) {
            		CreditCardBill creditCardBill = new CreditCardBill();
            		creditCardBill.setEntries(new ArrayList<Entry>());
            		CreditCard creditCard = new CreditCard();
            		creditCard.setNumber(b.getMessage().getAccount().getAccountNumber());
            		creditCardBill.setCreditCard(creditCard);
            		System.out.println("cartao de credito: " + b.getMessage().getAccount().getAccountNumber());
            		creditCardBill.setAmount(BigDecimal.valueOf(b.getMessage().getLedgerBalance().getAmount()));
            		System.out.println("balanço final: " + b.getMessage().getLedgerBalance().getAmount());
            		LocalDate dataVencimento = LocalDateTime.ofInstant(Instant.ofEpochMilli(b.getMessage().getLedgerBalance().getAsOfDate().getTime()), ZoneId.systemDefault()).toLocalDate();
            		dataVencimento = dataVencimento.plusDays(1);
            		creditCardBill.setClosingDate(dataVencimento);
            		System.out.println("dataDoVencimento: " + dataVencimento);
            		List<Transaction> list = b.getMessage().getTransactionList().getTransactions();
            		System.out.println("TRANSAÇÕES\n");
            		for (Transaction transaction : list) {
            			Entry entry = new Entry();
            			entry.setType(transaction.getTransactionType().name());
            			System.out.println("tipo: " + transaction.getTransactionType().name());
            			entry.setIdent(transaction.getId());
            			System.out.println("id: " + transaction.getId());
            			entry.setDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(transaction.getDatePosted().getTime()), ZoneId.systemDefault()).toLocalDate());
            			System.out.println("data: " + transaction.getDatePosted());
            			entry.setValue(BigDecimal.valueOf(transaction.getAmount()));
            			System.out.println("valor: " + transaction.getAmount());
            			if (transaction.getMemo().contains("PARC")) {
            				entry.setDescription(transaction.getMemo().substring(0, 14));
            				String[] parcelamento = transaction.getMemo().substring(18, 25).split("/");
            				entry.setPortion(Integer.parseInt(parcelamento[0].trim()));
            				entry.setPortionQuant(Integer.parseInt(parcelamento[1].trim()));
            				entry.setLocation(transaction.getMemo().substring(25, transaction.getMemo().length()).trim());
            			} else if ("PAYMENT".equals(transaction.getTransactionType().name())) {
            				entry.setDescription(transaction.getMemo().substring(0, 23));
            				entry.setLocation(transaction.getMemo().substring(23, transaction.getMemo().length()).trim());
            			} else {
            				entry.setDescription(transaction.getMemo());
            			}
            			System.out.println("descricao: " + transaction.getMemo());
            			creditCardBill.getEntries().add(entry);
            			System.out.println("");
            		}
            		
            		return creditCardBillRepository.save(creditCardBill);
            	}
            	
        	}
        }
        return null;
	}

}

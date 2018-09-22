package vinted;

import vinted.delivery.entities.Transaction;
import vinted.delivery.service.BillService;
import vinted.delivery.service.input.TransactionParser;
import vinted.delivery.service.output.TransactionWriter;

import java.nio.file.Paths;
import java.util.List;

public class Application {

    // TODO: Handle Exceptions
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ApplicationContext();
        TransactionParser parser = context.getBean(TransactionParser.class);
        List<Transaction> transactions = parser.parse(Paths.get(Application.class.getResource("/input.txt").toURI()));
        BillService billService = context.getBean(BillService.class);
        billService.apply(transactions);
        TransactionWriter writer = context.getBean(TransactionWriter.class);
        writer.write(transactions);
    }
}

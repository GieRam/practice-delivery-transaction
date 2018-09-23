package vinted;

import vinted.common.ApplicationContext;
import vinted.delivery.transaction.entities.Transaction;
import vinted.delivery.transaction.service.BillService;
import vinted.delivery.transaction.service.input.Parser;
import vinted.delivery.transaction.service.output.TransactionWriter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        ApplicationContext context = ApplicationContext.getInstance();
        try {
            Parser parser = context.getBean(Parser.class);
            List<Transaction> transactions = parser.parse(getInputPath(args));

            BillService billService = context.getBean(BillService.class);
            billService.apply(transactions);

            TransactionWriter writer = context.getBean(TransactionWriter.class);
            writer.write(transactions);
        } catch (URISyntaxException e) {
            System.out.println(String.format("Malformed URI Syntax: %s", e.getMessage()));
        } catch (IOException e) {
            System.out.println(String.format("Failed parsing input file: %s", e.getMessage()));
        }
    }

    private static Path getInputPath(String args[]) throws URISyntaxException {
        return args.length == 1 ?
            Paths.get(args[0])
            : Paths.get(Application.class.getResource("/input.txt").toURI());
    }
}

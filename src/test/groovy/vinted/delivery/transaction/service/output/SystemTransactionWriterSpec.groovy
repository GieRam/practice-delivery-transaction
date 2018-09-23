package vinted.delivery.transaction.service.output

import spock.lang.Specification
import vinted.delivery.transaction.entities.Transaction

class SystemTransactionWriterSpec extends Specification {

    def transactionWriter

    def formatter

    def outContent = new ByteArrayOutputStream()

    def originalOut = System.out

    def 'setup'() {
        formatter = Mock(TransactionFormatter)
        formatter.format(_) >> 'formatted transaction'
        transactionWriter = new SystemTransactionWriter(formatter)
        System.setOut(new PrintStream(outContent))
    }

    def 'cleanup'() {
        System.setOut(originalOut)
    }

    def 'should write transactions to system output'() {
        given:
            def transactions = [
                    new Transaction('first'),
                    new Transaction('second'),
                    new Transaction('third')
            ]
        when:
            transactionWriter.write(transactions)
        then:
            outContent.toString() == 'formatted transaction\nformatted transaction\nformatted transaction\n'
    }
}

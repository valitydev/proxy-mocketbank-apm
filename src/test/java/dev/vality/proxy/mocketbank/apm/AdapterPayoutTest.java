package dev.vality.proxy.mocketbank.apm;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.adapter.bank.payout.spring.boot.starter.model.AdapterState;
import dev.vality.adapter.bank.payout.spring.boot.starter.model.TransactionInfo;
import dev.vality.adapter.bank.payout.spring.boot.starter.state.serializer.AdapterStateSerializer;
import dev.vality.damsel.domain.BankCard;
import dev.vality.damsel.withdrawals.domain.Destination;
import dev.vality.damsel.withdrawals.provider_adapter.AdapterSrv;
import dev.vality.damsel.withdrawals.provider_adapter.ProcessResult;
import dev.vality.damsel.withdrawals.provider_adapter.Withdrawal;
import dev.vality.swag.wallets.model.wallets.DestinationResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static dev.vality.damsel.msgpack.Value.bin;
import static dev.vality.proxy.mocketbank.apm.client.MocketbankClient.FAILURE_ID;
import static dev.vality.proxy.mocketbank.apm.client.MocketbankClient.SUCCESS_ID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AdapterPayoutTest {

    @Autowired
    protected AdapterSrv.Iface payoutAdapterService;

    @Autowired
    private AdapterStateSerializer adapterStateSerializer;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void failPayoutWithWrongPaymentTool() {
        Withdrawal withdrawal = TestObjectFactory.testWithdrawalWithoutBody();
        withdrawal.setDestination(new Destination(Destination.bank_card(new BankCard())));
        AdapterState adapterState = new AdapterState();
        adapterState.setTrxInfo(new TransactionInfo());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                payoutAdapterService.processWithdrawal(
                        withdrawal,
                        bin(adapterStateSerializer.writeByte(adapterState)),
                        Collections.emptyMap()));

        assertEquals("Not applicable destination for adapter", exception.getMessage());
    }

    @Test
    void failPayoutWithEmptyBody() {
        Withdrawal withdrawal = TestObjectFactory.testWithdrawalWithoutBody();
        AdapterState adapterState = new AdapterState();
        adapterState.setTrxInfo(new TransactionInfo());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                payoutAdapterService.processWithdrawal(
                        withdrawal,
                        bin(adapterStateSerializer.writeByte(adapterState)),
                        Collections.emptyMap()));

        assertEquals("PaymentTool is empty", exception.getMessage());
    }

    @Test
    void failPayoutWithFailStatus() throws Exception {

        DestinationResource bankTransfer = TestObjectFactory.testBankTransferResource(FAILURE_ID);
        Withdrawal withdrawal = TestObjectFactory.testWithdrawal(mapper.writeValueAsBytes(bankTransfer));
        AdapterState adapterState = new AdapterState();
        adapterState.setTrxInfo(new TransactionInfo());

        ProcessResult processResult =
                payoutAdapterService.processWithdrawal(
                        withdrawal,
                        bin(adapterStateSerializer.writeByte(adapterState)),
                        Collections.emptyMap());

        assertTrue(processResult.getIntent().getFinish().getStatus().isSetFailure());
    }

    @Test
    void successPayoutForBankTransferInd() throws Exception {
        DestinationResource bankTransfer = TestObjectFactory.testBankTransferResource(SUCCESS_ID);
        Withdrawal withdrawal = TestObjectFactory.testWithdrawal(mapper.writeValueAsBytes(bankTransfer));
        AdapterState adapterState = new AdapterState();
        adapterState.setTrxInfo(new TransactionInfo());
        adapterState.setPollingInfo(null);

        ProcessResult payoutResult =
                payoutAdapterService.processWithdrawal(
                        withdrawal,
                        bin(adapterStateSerializer.writeByte(adapterState)),
                        Collections.emptyMap());

        assertTrue(payoutResult.getIntent().getFinish().getStatus().isSetSuccess());
    }

    @Test
    void successPayoutForDigitalWallet() throws Exception {
        DestinationResource resource = TestObjectFactory.testDigitalWalletDestinationResource(SUCCESS_ID);
        resource.setType(DestinationResource.TypeEnum.DIGITALWALLETDESTINATIONRESOURCE);
        Withdrawal withdrawal = TestObjectFactory.testWithdrawal(mapper.writeValueAsBytes(resource));
        AdapterState adapterState = new AdapterState();
        adapterState.setTrxInfo(new TransactionInfo());
        adapterState.setPollingInfo(null);

        ProcessResult payoutResult =
                payoutAdapterService.processWithdrawal(
                        withdrawal,
                        bin(adapterStateSerializer.writeByte(adapterState)),
                        Collections.emptyMap());

        assertTrue(payoutResult.getIntent().getFinish().getStatus().isSetSuccess());
    }
}

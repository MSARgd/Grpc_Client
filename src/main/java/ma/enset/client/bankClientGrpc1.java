package ma.enset.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ma.enset.subs.Bank;
import ma.enset.subs.BankServiceGrpc;
public class bankClientGrpc1 {
    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",2023)
                .usePlaintext()
                .build();
        BankServiceGrpc.BankServiceBlockingStub blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("USD")
                .setCurrencyTo("EUR")
                .setAmount(2000)
                .build();
        Bank.ConvertCurrencyResponse response = blockingStub.convert(request);
        System.out.println(response);

//        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",2023)
//                .usePlaintext()
//                .build();
//        BankServiceGrpc.BankServiceBlockingStub blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
//        Bank.ConvertCurrencyRequest request =  Bank.ConvertCurrencyRequest.newBuilder()
//                .setCurrencyFrom("MRO")
//                .setCurrencyTo("DAH")
//                .setAmount(200)
//                .build();
//        Bank.ConvertCurrencyResponse currencyResponse = blockingStub.convert(request);
//        System.out.println(currencyResponse);
    }
}
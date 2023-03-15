package ma.enset.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.internal.JsonUtil;
import io.grpc.stub.StreamObserver;
import ma.enset.subs.Bank;
import ma.enset.subs.BankServiceGrpc;

import java.io.IOException;

public class bankClientGrpc3 {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 2023)
                .usePlaintext()
                .build();
        BankServiceGrpc.BankServiceStub asyncStub = BankServiceGrpc.newStub(managedChannel);
        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("MRO")
                .setCurrencyTo("DAH")
                .setAmount(200)
                .build();

        asyncStub.getCurrencyStream(request, new StreamObserver<Bank.ConvertCurrencyResponse>() {
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("============================");
                System.out.println(convertCurrencyResponse);

            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("END.....");

            }

        });
        System.out.println("waiting for message ");
        System.in.read();
    }
}

package ma.enset.client;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.enset.subs.Bank;
import ma.enset.subs.BankServiceGrpc;
import java.util.Timer;
import java.util.TimerTask;
public class BankClientGrpcMFulldiplex {
    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",2023)
                .usePlaintext()
                .build();
        BankServiceGrpc.BankServiceStub asyncStub = BankServiceGrpc.newStub(managedChannel);
        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("MRO")
                .setCurrencyTo("DH")
                .setAmount(100)
                .build();
        StreamObserver<Bank.ConvertCurrencyRequest> performStream = asyncStub.fullCurrencyStream(new StreamObserver<Bank.ConvertCurrencyResponse>() {
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("=======================");
                System.out.println(convertCurrencyResponse.getResult());
                System.out.println("=================================");
            }
            @Override
            public void onError(Throwable throwable) {
            }
            @Override
            public void onCompleted() {
                System.out.println(".....END");
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int counter =0;
            @Override
            public void run() {
                ++counter;
                Bank.ConvertCurrencyRequest  request1 = Bank.ConvertCurrencyRequest.newBuilder()
                                .setCurrencyFrom("USD")
                                .setCurrencyTo("EUR")
                                .setAmount(1000)
                                .build();
                performStream.onNext(request1);
                System.out.println(request1);
                if (counter==10){
                    performStream.onCompleted();
                    timer.cancel();
                }
            }
        },1000, 1000);
    }
}
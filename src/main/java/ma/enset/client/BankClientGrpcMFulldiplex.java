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
//        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
//                .setCurrencyFrom("MRO")
//                .setCurrencyTo("DH")
//                .setAmount(1)
//                .build();
        StreamObserver<Bank.ConvertCurrencyRequest> performStream = asyncStub.fullCurrencyStream(new StreamObserver<Bank.ConvertCurrencyResponse>() {
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {

                System.out.println("Server :  "+"\033[38;2;0q;0;255m  Resultat : "+convertCurrencyResponse.getResult()+  "\033[0m");

            }
            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
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
                Bank.ConvertCurrencyRequest  request = Bank.ConvertCurrencyRequest.newBuilder()
                                .setCurrencyFrom("USD")
                                .setCurrencyTo("EUR")
                                .setAmount(Math.random()*100)
                                .build();
                System.out.println( "Client :   "+
                        "CurrencyFrom : "+ request.getCurrencyFrom()
                        +"   CurrencyTo : "+ request.getCurrencyTo()
                        +"   Amount : "+request.getAmount());
                performStream.onNext(request);
                if (counter==10){
                    performStream.onCompleted();
                    timer.cancel();
                }
            }
        },1000, 1000);
    }
}
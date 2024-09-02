package xyz.kindit.runa.grpc.services;

import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Component;
import xyz.kindit.runa.Message;
import xyz.kindit.runa.Response;
import xyz.kindit.runa.Runa;
import xyz.kindit.runa.SayGrpc;

@Component
public class SayServiceImpl extends SayGrpc.SayImplBase {
    @Override
    public void say(Message request, StreamObserver<Response> responseObserver) {
        Response response = Response.newBuilder()
                .setResponse("Message received: " + request.getMessage())
                .build();

        Runa.getJda()
                .getGuildById("000000000000000000")
                .getTextChannelById("0000000000000000000")
                .sendMessage(request.getMessage())
                .queue();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

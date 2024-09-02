package xyz.kindit.runa.grpc;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class GrpcServer {

    @SneakyThrows
    public GrpcServer(@Value("${gRPC.port}") int port, BindableService... services) {
        Server server = ServerBuilder
                .forPort(port)
                .addServices(
                        Arrays.stream(services)
                                .map(BindableService::bindService)
                                .toList()
                )
                .build();

        server.start();
        server.awaitTermination();
    }

}

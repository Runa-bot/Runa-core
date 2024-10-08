package xyz.kindit.runa.grpc.services;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Component;
import xyz.kindit.runa.ChangeNicknameRequest;
import xyz.kindit.runa.ChangeNicknameResponse;
import xyz.kindit.runa.ChangeNicknameServiceGrpc;
import xyz.kindit.runa.Runa;
import xyz.kindit.runa.grpc.bean.ChangeNicknameRequestBean;
import xyz.kindit.runa.grpc.handlers.RequestHandler;

import java.util.Objects;

@Component
public class ChangeNicknameServiceImpl extends ChangeNicknameServiceGrpc.ChangeNicknameServiceImplBase {

    @Override
    public void changeNickname(ChangeNicknameRequest request, StreamObserver<ChangeNicknameResponse> responseObserver) {

        RequestHandler<ChangeNicknameRequestBean> betterRequest = getBetterRequest(request);

        if (!betterRequest.status.isOk()) {
            responseObserver.onError(betterRequest.status.asException());
        }

        try {
            assert betterRequest.bean.member != null;
            betterRequest.bean.member.modifyNickname(request.getNewNickname()).queue();
        } catch (Exception exception) {
            responseObserver.onError(Status.CANCELLED.withDescription(exception.getMessage()).asException());
        }

        responseObserver.onNext(
                ChangeNicknameResponse.newBuilder()
                        .setResponse(
                                betterRequest.bean.member.getId() + ": "
                                        + betterRequest.bean.newNickname
                        )
                        .build()
        );

        responseObserver.onCompleted();
    }

    private RequestHandler<ChangeNicknameRequestBean> getBetterRequest(ChangeNicknameRequest request) {
        return new RequestHandler<>(new ChangeNicknameRequestBean(request)) {
            @Override
            protected Status checkRequest() {
                if (request.getNewNickname().isEmpty()) {
                    return Status.INVALID_ARGUMENT.withDescription("New nickname is empty");
                }

                Guild guild = Runa.getJda().getGuildById(request.getGuildId());
                if (guild == null) {
                    return Status.NOT_FOUND.withDescription("Guild not found");
                }

                Member member = guild.getMemberById(request.getMemberId());
                if (member == null) {
                    return Status.NOT_FOUND.withDescription("Member not found");
                }

                if (Objects.equals(member.getNickname(), request.getNewNickname())) {
                    return Status.ALREADY_EXISTS.withDescription("Current and new nicknames are the same");
                }

                return Status.OK;
            }
        };
    }

}

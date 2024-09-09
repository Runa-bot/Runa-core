package xyz.kindit.runa.grpc.commands;

import io.grpc.stub.StreamObserver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Component;
import xyz.kindit.runa.ChangeNicknameRequest;
import xyz.kindit.runa.ChangeNicknameResponse;
import xyz.kindit.runa.ChangeNicknameServiceGrpc;
import xyz.kindit.runa.Runa;

import java.util.Objects;

@Component
public class ChangeNicknameServiceImpl extends ChangeNicknameServiceGrpc.ChangeNicknameServiceImplBase {

    @Override
    public void changeNickname(ChangeNicknameRequest request, StreamObserver<ChangeNicknameResponse> responseObserver) {
        if (request.getNewNickname().isEmpty()) {
            sendResponse(responseObserver, response(false, "New nickname is empty"));
            return;
        }

        Guild guild = Runa.getJda().getGuildById(request.getGuildId());
        if (guild == null) {
            sendResponse(responseObserver, response(false, "Guild not found"));
            return;
        }

        Member member = guild.getMemberById(request.getMemberId());
        if (member == null) {
            sendResponse(responseObserver, response(false, "Member not found"));
            return;
        }

        if (Objects.equals(member.getNickname(), request.getNewNickname())) {
            sendResponse(responseObserver, response(true, "Current and new nicknames are the same"));
            return;
        }

        try {
            member.modifyNickname(request.getNewNickname()).queue();
        } catch (Exception exception) {
            sendResponse(responseObserver, response(false, exception.getMessage()));
        }

        sendResponse(responseObserver, response(true, "Nickname changed"));
    }

    private void sendResponse(StreamObserver<ChangeNicknameResponse> responseObserver, ChangeNicknameResponse response) {
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    private ChangeNicknameResponse response(boolean isSuccessfully, String message) {
        return ChangeNicknameResponse.newBuilder()
                .setIsSuccessfully(isSuccessfully)
                .setResponseMessage(message)
                .build();
    }

}

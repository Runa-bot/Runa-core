package xyz.kindit.runa.grpc.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import xyz.kindit.runa.ChangeNicknameRequest;
import xyz.kindit.runa.Runa;

@Getter
@AllArgsConstructor
public class ChangeNicknameRequestBean {

    public final Guild guild;
    public final Member member;
    public final String newNickname;

    public ChangeNicknameRequestBean(ChangeNicknameRequest request) {
        guild = Runa.getJda().getGuildById(request.getGuildId());
        assert guild != null;
        member = guild.getMemberById(request.getMemberId());
        newNickname = request.getNewNickname();
    }
}

package com.kindit.bot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Objects;

public class ChangeNickname extends Command {
    private ChangeNickname() {
        super("change-nickname", "Changes another user's nickname");
    }

    public static ChangeNickname createCommand() {
        return new ChangeNickname();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(this.userName, this.description)
                .addOption(OptionType.USER, "user", "User", true)
                .addOption(OptionType.STRING, "nickname", "New nickname", true);
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        if (!event.getName().equals(userName)) { return; }

        event.deferReply(true).queue();
        boolean callerCanChangeNickname = Objects.requireNonNull(event.getMember()).hasPermission(Permission.NICKNAME_CHANGE);
        Member member = Objects.requireNonNull(event.getOption("user")).getAsMember();
        boolean isBot = Objects.requireNonNull(member).getUser().equals(event.getJDA().getSelfUser());
        String newNickname = Objects.requireNonNull(event.getOption("nickname")).getAsString();

        if (isBot) {
            event.getHook().sendMessageEmbeds(Command.notSuccessfullyReplyEmbed("I won't do this.")).queue();
            return;
        } else if (!callerCanChangeNickname) {
            event.getHook().sendMessageEmbeds(Command.notSuccessfullyReplyEmbed("You do not have rights to change nicknames.")).queue();
            return;
        }

        try {
            member.modifyNickname(newNickname).queue();
        } catch (HierarchyException exception) {
            event.getHook().sendMessageEmbeds(Command.notSuccessfullyReplyEmbed("Can't modify a member with higher or equal highest role than bot.")).queue();
            return;
        } catch (InsufficientPermissionException exception) {
            event.getHook().sendMessageEmbeds(Command.notSuccessfullyReplyEmbed("Bot do not have rights to change nicknames.")).queue();
            return;
        }

        event.getHook().sendMessageEmbeds(Command.successfullyReplyEmbed()).setEphemeral(true).queue();
    }
}

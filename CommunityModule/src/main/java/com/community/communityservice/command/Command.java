package com.community.communityservice.command;

/**
 * Command：抽象命令接口，声明 execute / undo
 */
public interface Command {
    void execute();
    void undo();
    String getCommandName();
}

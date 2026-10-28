package com.community.communityservice.invoker;

import com.community.communityservice.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Stack;

/**
 * Invoker：命令调用者
 * 触发命令、记录日志、维护历史栈支持撤销
 */
@Component
public class CommandInvoker {

    private static final Logger logger = LoggerFactory.getLogger(CommandInvoker.class);

    private final Stack<Command> history = new Stack<>();

    public void executeCommand(Command command) {
        logger.info("执行命令: {}", command.getCommandName());
        command.execute();
        history.push(command);
        logger.info("命令执行完成，当前历史栈深度: {}", history.size());
    }

    public boolean undoLast() {
        if (history.isEmpty()) {
            logger.info("没有可撤销的命令");
            return false;
        }
        Command command = history.pop();
        logger.info("撤销命令: {}", command.getCommandName());
        command.undo();
        logger.info("撤销完成，剩余历史栈深度: {}", history.size());
        return true;
    }

    public int getHistorySize() {
        return history.size();
    }
}

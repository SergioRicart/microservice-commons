package com.sergioricart.commons.application;

public interface CommandHandler<T extends Command<R>, R> {

    R handle(T command);

    Class<T> getCommandType();
}

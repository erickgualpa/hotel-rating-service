package org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.command.simple;

import org.egualpam.services.hotelmanagement.shared.application.command.Command;
import org.egualpam.services.hotelmanagement.shared.application.command.CommandBus;

import java.util.Map;
import java.util.Optional;

public final class SimpleCommandBus implements CommandBus {

    private final Map<Class<? extends Command>, CommandHandler> handlers;

    public SimpleCommandBus(Map<Class<? extends Command>, CommandHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void publish(Command command) {
        Optional.ofNullable(handlers.get(command.getClass()))
                .orElseThrow(CommandHandlerNotFound::new)
                .handle(command);
    }
}

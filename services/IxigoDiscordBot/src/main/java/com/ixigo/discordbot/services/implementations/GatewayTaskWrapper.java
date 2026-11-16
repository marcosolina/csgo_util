package com.ixigo.discordbot.services.implementations;

import java.util.function.Consumer;

import net.dv8tion.jda.api.utils.concurrent.Task;
import reactor.core.publisher.Mono;

public class GatewayTaskWrapper<T> {
	private final Task<T> gatewayTask;

	public GatewayTaskWrapper(Task<T> gatewayTask) {
		this.gatewayTask = gatewayTask;
	}
	
	public Mono<T> onSuccess(Consumer<? super T> callback) {
        return Mono.create(sink -> {
            gatewayTask.onSuccess(result -> {
                try {
                    callback.accept(result);
                    sink.success(result);
                } catch (Throwable error) {
                    sink.error(error);
                }
            });
        });
    }
}

package com.example;

import com.example.env.ContainerEnvHelper;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;

public class ApplicationContextFuture implements ApplicationListener<ContextStartedEvent> {
    @Override
    public void onApplicationEvent(ContextStartedEvent contextStartedEvent) {
        ContainerEnvHelper.setContext(contextStartedEvent.getApplicationContext());
    }
}

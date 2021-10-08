package com.example.env;

import org.springframework.context.ApplicationContext;

public class ContainerEnvHelper {
    public static ApplicationContext context;


    public static void setContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }
}

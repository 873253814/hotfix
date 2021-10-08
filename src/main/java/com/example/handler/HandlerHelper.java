package com.example.handler;

import com.example.env.ContainerEnvHelper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

public class HandlerHelper implements ApplicationContextAware {
    private static HashMap<Object, HandlerInfo> handlerMap = new HashMap<>();

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public static <H, Type> H getSingletonHandler(Type type) {
        HandlerInfo handlerInfo = handlerMap.get(type);
        if (handlerInfo == null) {
            throw new RuntimeException();
        }
        return handlerInfo.getSingleton();
    }

    public static <H, Type> H createHandler(Type type) throws InstantiationException, IllegalAccessException {
        HandlerInfo handlerInfo = handlerMap.get(type);
        if (handlerInfo == null) {
            throw new RuntimeException();
        }
        return handlerInfo.createInstance();
    }


    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface HandlerReg {

    }

    public interface IHandlerGetter<Type> {
        Type type();
    }


    public static class HandlerInfo {
        Class<?> handlerClz;
        volatile Object singleton;

        HandlerInfo(Class<?> handlerClz) {
            this.handlerClz = handlerClz;
        }

        HandlerInfo(Class<?> handlerClz, Object singleton) {
            this.handlerClz = handlerClz;
            this.singleton = singleton;
        }

        <H> H getSingleton() {
            if (singleton == null) {
                synchronized (this) {
                    singleton = ContainerEnvHelper.getContext().getBean(handlerClz);
                }
            }
            return (H) singleton;
        }

        <H> H createInstance() throws IllegalAccessException, InstantiationException {
            if (singleton != null) {
                throw new RuntimeException();
            }
            H h = (H) handlerClz.newInstance();
            ContainerEnvHelper.getContext().getAutowireCapableBeanFactory().autowireBean(h);
            return h;
        }

    }
}

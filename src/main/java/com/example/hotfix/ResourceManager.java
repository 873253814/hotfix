package com.example.hotfix;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

@Component
public class ResourceManager extends InstantiationAwareBeanPostProcessorAdapter implements ApplicationContextAware {

    private ApplicationContext applicationContext;



    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Resource.class)) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                System.out.println(field.getType());
            }
            try {
                load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.postProcessAfterInstantiation(bean, beanName);
    }

    public void load() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/a.txt");
        InputStream inputStream = classPathResource.getInputStream();

        System.out.println(inputStream.toString());

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

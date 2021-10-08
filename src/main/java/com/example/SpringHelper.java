package com.example;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SpringHelper {

    private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private static MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

    public static <T> List<Class<? extends T>> search2Class(String packetName, Predicate<MetadataReader> predicate) throws IOException, ClassNotFoundException {
        Set<String> classNames = searchClass(packetName, predicate);
        if (classNames.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<Class<? extends T>> results = new ArrayList<>(classNames.size());
        for (String className : classNames) {
            results.add((Class<T>) Class.forName(className));
        }
        return results;
    }


    public static Set<String> searchClass(String packetName, Predicate<MetadataReader> predicate) throws IOException {
        Set<String> result = new HashSet<>();
        walkClass(packetName, metadataReader -> {
            if (!predicate.test(metadataReader)) {
                return;
            }
            ClassMetadata classMetadata = metadataReader.getClassMetadata();
            result.add(classMetadata.getClassName());
        });
        return result;
    }

    public static void walkClass(String packetName, Consumer<MetadataReader> consumer) throws IOException {
        String[] packetNames = StringUtils.tokenizeToStringArray(packetName, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        for (String pack : packetNames) {
            String packetPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(pack))
                     + "/" + "**/*.class";
            Resource[] resources = resourcePatternResolver.getResources(packetPath);
            for (Resource resource : resources) {
                if (!resource.isReadable()) {
                    continue;
                }
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                consumer.accept(metadataReader);

            }
        }
    }
}

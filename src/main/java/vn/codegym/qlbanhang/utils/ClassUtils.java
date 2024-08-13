package vn.codegym.qlbanhang.utils;

import vn.codegym.qlbanhang.annotation.Column;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassUtils {
    public static List<Field> getAllFields(Object obj) {
        Class cl = obj.getClass();
        List<Field> thisFields = Arrays.asList(cl.getDeclaredFields());
        List<Field> superFields = Arrays.asList(cl.getSuperclass().getDeclaredFields());
        List<Field> lstFields = new ArrayList<>();
        lstFields.addAll(thisFields);
        lstFields.addAll(superFields);
        return lstFields;
    }

    public static List<String> getAllColumnName(Object obj) {
        List<String> lstColumn = new ArrayList<>();
        List<Field> fieldList = getAllFields(obj);
        for (Field field : fieldList) {
            field.setAccessible(true);
            Column annotation = field.getAnnotation(Column.class);
            if (annotation != null) {
                String colName = annotation.name();
                lstColumn.add(colName);
            }
        }
        return lstColumn;
    }

    public static Object getValueFromColumnAnnotation(Object ob, String colName) {
        try {
            List<Field> fieldList = getAllFields(ob);
            for (Field field : fieldList) {
                field.setAccessible(true);
                Column annotation = field.getAnnotation(Column.class);
                if (annotation != null) {
                    if (colName.equals(annotation.name())) {
                        return field.get(ob);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Class<?>> getClasses(String packageName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            URL resource = classLoader.getResource(path);
            File directory = new File(resource.getFile());

            List<Class<?>> classes = new ArrayList<>();
            if (directory.exists()) {
                for (String file : directory.list()) {
                    if (file.endsWith(".class")) {
                        String className = packageName + '.' + file.substring(0, file.length() - 6);
                        classes.add(Class.forName(className));
                    }
                }
            }
            return classes;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

}

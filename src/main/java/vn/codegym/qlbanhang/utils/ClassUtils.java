package vn.codegym.qlbanhang.utils;

import javax.persistence.Column;
import java.lang.reflect.Field;
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


}

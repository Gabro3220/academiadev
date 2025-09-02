import java.lang.reflect.Field;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class GenericCsvExporter {
    public static <T> String exportToCsv(List<T> data, String... selectedFields) {
        if (data.isEmpty()) {
            return "Nenhum dado para exportar";
        }
        
        StringBuilder csv = new StringBuilder();
        Class<?> clazz = data.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        
        if (selectedFields.length > 0) {
            csv.append(String.join(",", selectedFields)).append("\n");
        } else {
            csv.append(Arrays.stream(fields)
                    .map(Field::getName)
                    .collect(Collectors.joining(","))).append("\n");
        }
        
        for (T item : data) {
            if (selectedFields.length > 0) {
                csv.append(getSelectedFieldValues(item, selectedFields));
            } else {
                csv.append(getAllFieldValues(item, fields));
            }
            csv.append("\n");
        }
        
        return csv.toString();
    }
    
    private static <T> String getSelectedFieldValues(T item, String[] selectedFields) {
        return Arrays.stream(selectedFields)
                .map(fieldName -> getFieldValue(item, fieldName))
                .collect(Collectors.joining(","));
    }
    
    private static <T> String getAllFieldValues(T item, Field[] fields) {
        return Arrays.stream(fields)
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(item);
                        return value != null ? value.toString() : "";
                    } catch (IllegalAccessException e) {
                        return "";
                    }
                })
                .collect(Collectors.joining(","));
    }
    
    private static <T> String getFieldValue(T item, String fieldName) {
        try {
            Field field = item.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(item);
            return value != null ? value.toString() : "";
        } catch (Exception e) {
            return "";
        }
    }
}

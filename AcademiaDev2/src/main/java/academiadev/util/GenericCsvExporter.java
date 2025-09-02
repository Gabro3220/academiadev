package academiadev.util;

import java.lang.reflect.Field;
import java.util.List;

public class GenericCsvExporter {
    
    public static String exportToCsv(List<?> data, List<String> selectedFields) {
        if (data == null || data.isEmpty()) {
            return "Nenhum dado para exportar";
        }
        
        StringBuilder csv = new StringBuilder();
        
        // Cabeçalho
        csv.append(String.join(",", selectedFields)).append("\n");
        
        // Dados
        for (Object item : data) {
            StringBuilder row = new StringBuilder();
            for (String fieldName : selectedFields) {
                try {
                    Field field = item.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object value = field.get(item);
                    row.append("\"").append(value != null ? value.toString() : "").append("\",");
                } catch (Exception e) {
                    row.append("\"\",");
                }
            }
            // Remove a última vírgula
            if (row.length() > 0) {
                row.setLength(row.length() - 1);
            }
            csv.append(row).append("\n");
        }
        
        return csv.toString();
    }
}

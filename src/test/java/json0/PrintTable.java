package json0;

import java.util.ArrayList;
import java.util.List;

public class PrintTable {

    public static void printTable(List<String[]> data) {
        String[] headers = {"Module", "Function", "Test Count", "Test Time"};

        int[] columnWidths = getColumnWidths(data, headers);

        printHorizontalLine(columnWidths);
        printRow(columnWidths, headers);
        printHorizontalLine(columnWidths);

        for (String[] row : data) {
            printRow(columnWidths, row);
        }

        printHorizontalLine(columnWidths);
    }

    public static void printRow(int[] columnWidths, String... values) {
        StringBuilder sb = new StringBuilder("| ");

        for (int i = 0; i < columnWidths.length; i++) {
            String value = values[i];
            sb.append(String.format("%-" + columnWidths[i] + "s", value));
            sb.append(" | ");
        }

        System.out.println(sb.toString());
    }

    public static void printHorizontalLine(int[] columnWidths) {
        StringBuilder sb = new StringBuilder("+");

        for (int width : columnWidths) {
            for (int i = 0; i < (width + 2); i++) {
                sb.append("-");
            }
            sb.append("+");
        }

        System.out.println(sb.toString());
    }

    public static int[] getColumnWidths(List<String[]> data, String[] headers) {
        int columnCount = headers.length;
        int[] columnWidths = new int[columnCount];

        for (String[] row : data) {
            for (int i = 0; i < columnCount; i++) {
                columnWidths[i] = Math.max(columnWidths[i], Math.max(headers[i].length(), row[i].length()));
            }
        }

        return columnWidths;
    }

}

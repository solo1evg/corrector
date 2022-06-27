import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class XLSToCSV_Utils {
    private XLSToCSV_Utils() {
    }

    public static void writeToCSV(Workbook wb) {
        StringBuilder stringBuilder = new StringBuilder();

        if (wb != null) {
            Sheet sheet = wb.getSheet(App.SHEET_PVP);
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                stringBuilder.append(convertRowContentToCSV(sheet.getRow(i))).append("\n");
            }
        }

        try (OutputStreamWriter fileOut = new OutputStreamWriter(new FileOutputStream(App.NEW_BOOK_RESULT_CSV), StandardCharsets.UTF_8)) {
            fileOut.write('\ufeff');
            fileOut.write(stringBuilder.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static String convertRowContentToCSV(Row row) {
        Iterator<Cell> cellIterator = row.cellIterator();
        StringBuilder data = new StringBuilder();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            switch (cell.getCellType()) {
                case BOOLEAN:
                    data.append(cell.getBooleanCellValue()).append(";");
                    break;

                case NUMERIC:
                    data.append(cell.getNumericCellValue()).append(";");
                    break;

                case STRING:
                    data.append(cell.getStringCellValue()).append(";");
                    break;

                case BLANK:
                    data.append(";");
                    break;

                case FORMULA:
                case _NONE:
                case ERROR:
                    break;

                default:
                    data.append(cell).append(";");
            }
        }
        return data.toString().replaceAll("<--end line-->;", "<--end line-->");
    }
}

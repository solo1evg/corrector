import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelUtils {

    private ExcelUtils() {
    }

    /**
     * Чтение книги из файла.
     *
     * @param filename название файла
     * @return книгу
     */
    public static Workbook readWorkbook(String filename) throws IOException {
        try (InputStream fs = new FileInputStream(filename)) {
            Workbook wb = WorkbookFactory.create(fs);
            return wb;
        } catch (Exception e) {
            System.out.println(e);
            throw new IOException();
        }
    }

    /**
     * Запись книги в файл.
     *
     * @param wb       книгу, которую записывать
     * @param fileName название файла куда записывать книгу
     */
    public static void writeWorkbook(Workbook wb, String fileName) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            wb.write(fileOut);
        } catch (Exception e) {
            System.out.println(e);
            throw new IOException();
        }
    }

    /**
     * Собирает ненулевые значения столбца в коллекцию.
     *
     * @param sheet     страница в файле Excel
     * @param rowNum    номер строки с которой начинать сбор данных
     * @param columnNum номер столбца из которого будут собираться данные
     * @return List строк
     */
    public static List<String> collectStrings(Sheet sheet, int rowNum, int columnNum) {
        List<String> result = new ArrayList<>();
//      DataFormatter formatter = new DataFormatter();

        for (int i = rowNum; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(columnNum);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                result.add(cell.getStringCellValue());
//                result.add(formatter.formatCellValue(cell));
            }
        }
        return result;
    }

    /**
     * Метод производит поиск подразделений оргструктуры по списку идентификаторов SAP.
     *
     * @param sheet     страница из которой берутся построчно номера подразделений
     * @param rowNum    номер строки с которой начинать поиск
     * @param columnNum номер столбца в котором происходит поиск
     * @param SAP_IDs   коллекция идентификаторов SAP HR организаций
     */
    public static void findMatches(Sheet sheet, int rowNum, int columnNum, List<String> SAP_IDs) throws IOException {
        List<String> listNotFindSAP = new ArrayList<>();
        boolean find = false;
        for (int i = rowNum; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(columnNum);
            if (cell.getCellType() != CellType.BLANK) {
                List<String> departments = Arrays.stream(cell.getStringCellValue().split("#@#")).collect(Collectors.toList());
                for (String department : departments) {
                    if (department.isEmpty()) {
                        continue;
                    }
                    for (String SAP_ID : SAP_IDs) {
                        find = false;
                        if (department.equals(SAP_ID)) {
                            find = true;
                            break;
                        }
                    }
                    if (!find) {
                        String correct;
                        if (cell.getStringCellValue().startsWith(department)) {
                            if (cell.getStringCellValue().endsWith(department)) {
                                correct = cell.getStringCellValue().replace(department, "");
                            } else {
                                correct = cell.getStringCellValue().replaceAll((department + "#@#"), "");
                            }
                        } else {
                            correct = cell.getStringCellValue().replaceAll(("#@#" + department), "");
                        }

                        // записываем в ячейку новое значение
                        cell.setCellValue(correct);
                        // лист отсутствующих департаментов
                        listNotFindSAP.add(department);

//                        System.out.println("Not found " + department + " - " + (cell.getRowIndex() + 1)); // прибавляем +1 к индексу строки, чтобы увидеть номер строки в файле Excel
//                        System.out.println(cell.getStringCellValue());

                    }
                }
            }
        }
        createNewExcelList(listNotFindSAP);
    }

    /**
     * Создаем новую книгу, заполняем и сохраняем на внешний источник.
     *
     * @param departments коллекция не найденных SAP в "Выгрузке подразделений"
     */
    public static void createNewExcelList(List<String> departments) throws IOException {
        App.countMismatch = departments.size();
        if (App.countMismatch > 0) {
            HSSFWorkbook newBook = new HSSFWorkbook();
            HSSFSheet sheet = newBook.createSheet("Результат");
            sheet.autoSizeColumn(33);
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Не найденные SAP");

            for (int i = 0; i < departments.size(); i++) {
                row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(departments.get(i));
            }
            writeWorkbook(newBook, App.NEW_BOOK_WITH_MISSING_SAP);
        }
    }

    /**
     * Метод производит поиск заданного значения по ячейкам
     *
     * @param sheet страница в которой будет происходить поиск искомых значений
     * @param name  содержание искомой ячейки
     * @return координаты искомой ячейки
     */
    public static CellCoordinates getStartIndexes(Sheet sheet, String name) {
        CellCoordinates result = new CellCoordinates();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            for (Iterator<Cell> cellIterator = row.cellIterator(); cellIterator.hasNext(); ) {
                Cell cell = cellIterator.next();
                assert cell != null;
                if (name.equals(cell.getStringCellValue())) {
                    result.setRow(cell.getRowIndex());
                    result.setColumn(cell.getColumnIndex());
                }
            }
        }
        return result;
    }

    /**
     * Координаты ячейки в листе (строка и колонка)
     */
    static class CellCoordinates {
        int row;
        int column;

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }
    }
}

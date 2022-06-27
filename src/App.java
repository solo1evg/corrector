import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;

public class App {
    protected static Integer countMismatch;

    /**
     * входящие данные - файл "выгрузка подразделений", название страницы файла "выгрузки подразделений", название столбца в SHEET_SAP_ID,
     */
    protected static final String SAP_ID = "Выгрузка подразделений.xlsx";

    protected static final String SHEET_SAP_ID = "Отчет 1";

    protected static final String CELL_SHEET_SAP_ID = "Идентификатор SAP HR организации";

    /**
     * входящие данные - файл "правила видимости полномочий(ПВП)", название страницы файла "правила видимости полномочий", название столбца в SHEET_PVP
     */
    protected static final String PVP = "ПВП.xls";

    protected static final String SHEET_PVP = "Выгрузка правил видимости полно";

    protected static final String CELL_SHEET_PVP = "Подразделения оргструктуры";

    /**
     * название файла, который будет создан при условии нахождения отсутст. идентификаторов SAP
     */
    protected static final String NEW_BOOK_WITH_MISSING_SAP = "Недействующие подразделения.xls";

    /**
     * название файла, после удаления неактивных подразделений и конвертации в формат .csv UTF8 c BOM
     */
    protected static final String NEW_BOOK_RESULT_CSV = "ПВП .csv";


    public static void main(String[] args) throws Exception {
        System.out.println(String.format("Обрабатываем входящие файлы: %s и %s", SAP_ID, PVP));
        Workbook sourceWorkbook = ExcelUtils.readWorkbook(SAP_ID);
        List<String> SAP_IDs = new ArrayList<>();
        if (sourceWorkbook != null) {
            Sheet sourceWorkbookSheet = sourceWorkbook.getSheet(SHEET_SAP_ID);

            /**
             * ищем по "Идентификатор SAP HR организации"
             */
            ExcelUtils.CellCoordinates coordinates = ExcelUtils.getStartIndexes(sourceWorkbookSheet, CELL_SHEET_SAP_ID);
            SAP_IDs = ExcelUtils.collectStrings(sourceWorkbookSheet, coordinates.getRow() + 1, coordinates.getColumn());  //строка 2, колонка 4

            /**
             * или берем нужный столбец по координатам (строка 2, колонка 4)
             * */
            // SAP_IDs = ExcelUtils.collectStrings(sourceWorkbookSheet, 2, 4);
        }

        Workbook compareWorkbook = ExcelUtils.readWorkbook(PVP);
        if (compareWorkbook != null) {
            Sheet compareWorkbookSheet = compareWorkbook.getSheet(SHEET_PVP);

            /** ищем по "Подразделения оргструктуры" */
            ExcelUtils.CellCoordinates coordinates = ExcelUtils.getStartIndexes(compareWorkbookSheet, CELL_SHEET_PVP);
            ExcelUtils.findMatches(compareWorkbookSheet, coordinates.getRow() + 1, coordinates.getColumn(), SAP_IDs);  //строка 1, колонка 10

            /** или берем нужный столбец по координатам */
            // ExcelUtils.findMatches(compareWorkbookSheet, 1, 10, SAP_IDs);  //строка 1, колонка 10

            ExcelUtils.writeWorkbook(compareWorkbook, PVP);
            XLSToCSV_Utils.writeToCSV(compareWorkbook);
        }
        System.out.println("Обработка завершена, найденно несоответствий: " + countMismatch + "." + (countMismatch > 0 ? " Cоздали файл: " + NEW_BOOK_WITH_MISSING_SAP : ""));
    }
}

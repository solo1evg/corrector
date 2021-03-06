На данный момент, при возникновении ошибок после загрузки сформированного файла, SAP ID недействующих подразделений очищаются сотрудниками РГ «SberUser. Группа управления шаблонами на доступ» вручную, после чего производится повторная загрузка файла, что значительно увеличивает время обработки запросов на модификацию шаблонов.

 

Правила видимости стенда (4 лист «Правила видимости шаблона»)

 

Как должно работать.

 ++ 

1.Сотрудник РГ «SberUser. Группа управления шаблонами на доступ» берет в работу ЗНР на модификацию шаблона, в котором требуется внести корректировки в правила видимости шаблона (ПВС – правила видимости стенда).

2.Выгружает данные из SAP BO по КЭ  (файл ПВС.xls)

3.Добавляет SAP ID подразделений (с 4 вкладки запроса на модификацию шаблона) через разделитель #@# в столбец «I» (Подразделения оргструктуры) (файл ПВС.xls).

4.Добавляет в созданный инструмент по проверке подразделений файл «Выгрузка подразделений (SAP ID)» (существует отчет, который ежедневно выгружает из SM актуальные подразделения и формирует данный файл) + выгруженный файл с внесенными изменениями.

5.Запускает механизм проверки.

6.Инструмент сравнивает SAP ID подразделений, указанных в столбце «I» (Подразделения оргструктуры) (файл ПВС.xls). со столбцом «E» (Идентификатор SAP HR организации) в файле «Выгрузка подразделений (SAP ID)». Если номер SAP ID не найден в файле «Выгрузка подразделений (SAP ID), он удаляется из столбца  «I» (Подразделения оргструктуры) (файл ПВС.xls). При разработке помимо удаления неактивных SAP ID необходимо учесть корректное удаление разделителей.

7.На выходе получаем очищенный от недействующих подразделений файл ПВС.xls в формате .csv (пример – файл ПВС.csv) + список удаленных (недействующих) подразделений.

8.Сотрудник загружает сформированный файл .csv через загрузчик и направляет администраторам АС список недействующих подразделений для последующей корректировки файла на модификацию шаблона.

 

 

Правила видимости полномочий (7 лист «Критичность_доступность ролей»)

 

Как должно работать.

 ++ 

1.Сотрудник РГ «SberUser. Группа управления шаблонами на доступ» берет в работу ЗНР на модификацию шаблона, в котором требуется внести корректировки в правила видимости полномочий (ПВП).

2.Выгружает данные из SAP BO по КЭ  (файл ПВП.xls)

3.Ищет в столбце «D» (Наименование правила) (файл ПВП.xls) правила, которые требуется изменить. Лишние строки удаляет.

4.Добавляет SAP ID подразделений (с 7 вкладки запроса на модификацию шаблона) через разделитель #@# в столбец «К» (Подразделения оргструктуры) (файл ПВП.xls). Для каждого правила (каждой строки) в столбце «К» указан свой набор SAP ID подразделений.

5.Добавляет в созданный инструмент по проверке подразделений файл «Выгрузка подразделений (SAP ID)» (существует отчет, который ежедневно выгружает из SM актуальные подразделения и формирует данный файл) + выгруженный файл с внесенными изменениями.

6.Запускает механизм проверки.

7.Инструмент сравнивает SAP ID подразделений, указанных в столбце «К» (Подразделения оргструктуры) каждой строки (файл ПВП.xls) со столбцом «E» (Идентификатор SAP HR организации) в файле «Выгрузка подразделений (SAP ID)». Если номер SAP ID не найден в файле «Выгрузка подразделений (SAP ID), он удаляется из столбца «К» (файл ПВП.xls). При разработке помимо удаления неактивных SAP ID необходимо учесть корректное удаление разделителей.

8.На выходе получаем очищенный от недействующих подразделений файл ПВП.xls в формате .csv (пример – файл ПВП.csv) с разным набором SAP ID в столбце «К» (Подразделения оргструктуры) (файл ПВП.xls) для каждой строки столбца «D» (Наименование правила) (файл ПВП.xls) + список удаленных (недействующих) подразделений.

9.Сотрудник загружает сформированный файл .csv через загрузчик и направляет администраторам АС список недействующих подразделений для последующей корректировки файла на модификацию шаблона.




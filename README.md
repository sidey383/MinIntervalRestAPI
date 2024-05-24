# Описание
Простое Rest API приложение, которое принимает интервалы чисел или букв и возвращает минимальное их пересечение.
Под минимальным интервалом понимается интервал, начало и конец которого минимальны. <br/>
Интервалы переданные разными вызовами также объединяются. Данные храняться в базе данных H2. 
# Взаимодействие
| | Добавление интервалов | Запрос минимального интервала | Запрос всех интервалов |
| --- | --- | --- | --- |
| Числа | POST /api/v1/intervals/merge?kind=digits | GET /api/v1/intervals/min?kind=digits | GET /api/v1/intervals/stored?kind=digits |
| Буквы | POST /api/v1/intervals/merge?kind=letters | GET /api/v1/intervals/min?kind=letters | GET /api/v1/intervals/stored?kind=letters |

# Описание методов

## POST /api/v1/intervals/merge?kind=digits 
Добавляет и объединяет переданные интервалы чисел с сохраненными.
### Принимает 
Объект long[][] - массив из пар чисел
### Возвращаемые коды состояния
406 - Неправильный объект или неправильное значение kind <br/>
202 - Значение принято и записано

## GET /api/v1/intervals/min?kind=digits
Возвращает минимальный интервал чисел.
### Возвращаемые значения
Объект long[] - пара чисел<br/>
Если интервалов нет - возвращается пустой массив
### Возвращаемые коды состояния
406 - Неправильное значение kind <br/>
204 - Нет записанных значений <br/>
200 - Значение возвращено

## GET /api/v1/intervals/stored?kind=digits
Возвращает все сохраненные интервалы чисел.
### Возвращаемые значения
Объект long[][] - массив из пар чисел <br/>
Если интервалов нет - возвращается пустой массив
### Возвращаемые коды состояния
406 - Неправильное значение kind <br/>
200 - Значение возвращено



## POST /api/v1/intervals/merge?kind=letters
Добавляет и объединяет переданные интервалы символов с сохраненными.
### Принимает 
Объект char[][] - набор интервалов символов
### Возвращаемые коды состояния
406 - Неправильное значения <br/>
202 - Значение принято и записано

## GET /api/v1/intervals/min?kind=letters
Возвращает минимальный интервал символов.
### Возвращаемые значения
Объект char[] - пара символов, минимальный интервал символов <br/>
Если интервалов нет - возвращается пустой массив
### Возвращаемые коды состояния
406 - Неправильное значение kind <br/>
204 - Нет записанных значений <br/>
200 - Значение возвращено

## GET /api/v1/intervals/stored?kind=letters
Возвращает все сохраненные интервалы символов.
### Возвращаемые значения
Объект long[][] - массив из пар символов, все сохраненные интервалы символов <br/>
Если интервалов нет - возвращается пустой массив
### Возвращаемые коды состояния
406 - Неправильное значение kind <br/>
200 - Значение возвращено

# Инструкция по сборке
Проект использует систему сборки maven
Для сборки нужно прописать
``` mvn package ```
В директории проекта, после чего соберется .jar файл проекта по пути
```./target/ShiftLabTestTask-1.0.jar```
# Инструкция по развёртыванию и запуску
Для запуска требуется java не ниже 17 версии. <br/>
Команда для запуска:
```java -jar  ShiftLabTestTask-1.0.jar```

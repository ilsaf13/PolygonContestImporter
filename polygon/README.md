Утилита для создания problem.xml и challenge.xml для PCMS2, используя архив Polygon.

`groups.txt` &mdash; информация по задачам типа IOI
Каждая строка файла содержит информацию по одной группе. В PCMS2 все группы должны быть непрерывные и должны идти в том же порядке, что и в `groups.txt`.

Параметры группы в `groups.txt`, значения всех параметров это строка:

 - group &mdash; пока это название группы в полигоне
 - feedback &mdash; это информация, показываемая участникам во время тура. Самые популярные это `group-score`, `outcome`, `group-score-and-test`
 - scoring &mdash; правило, по которому производится тестирование. Возможные значения: `sum` &mdash; на всех тестах, `group` &mdash; до первого непройденного.
 - group-bonus &mdash; целое число: бонусные баллы, которые начисляются, если все тесты группы прошли
 - require-groups &mdash; это список номеров групп (обычно, это название группы в полигоне), от которых зависима группа. Писать через пробел.
 
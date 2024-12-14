**Предметная область**

Сотрудники компаний и хранение информации о них. 
База данных (далее БД) содержит данные о работниках, такие как идентификационный номер (тип int), имя (тип String), компания (тип String), электронный адрес (тип String).
БД используется для выполнения различных операций - добавление, удаление, поиск, редактирование существующих записей о работниках и т.д.
БД работает с разными файлами - бинарный, csv.
Ключевое значение - ID каждого работника.

**Время выполнения**

1) Добавление записей в БД - **О(1)** ввиду использования HashMap для ID.
2) Удаление записей из БД по ключевому значению - **О(1)** ввиду использования HashMap для ID, удаление записи на экране за **О(n)** (1 цикл).
3) Удаление записей из БД по не ключевому значению - **О(n)** цикл while проходит по всем records - имеют длину n.
4) Поиск записей в БД по ключевому значению - **О(1)** ввиду использования HashMap для ID.
5) Поиск записей в БД по не ключевому значению - **O(n)** цикл for проходится по всем records.

**Возможные улучшения**

Использование деревьев для хранения данных, например, **Btree**, так как сложность всех операций тогда будет не больше чем **O(log n)**.

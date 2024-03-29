package org.example.service;

public interface CategoryService {




        /**
         * Добавляет новую корневую категорию с указанным именем.
         *
         * @param root   Название новой корневой категории.
         * @param chatId Идентификатор чата.
         * @return Сообщение о результате операции.
         */
        String addRoot(String root, long chatId);

        /**
         * Добавляет новую дочернюю категорию с указанным именем к существующей родительской категории.
         *
         * @param child  Название новой дочерней категории.
         * @param parent Название родительской категории, к которой следует добавить дочернюю.
         * @param chatId Идентификатор чата.
         * @return Сообщение о результате операции.
         */
        String add(String child, String parent, long chatId);

        /**
         * Возвращает структурированный вид дерева категорий.
         *
         * @return Строка, представляющая дерево категорий.
         */
        String view(long chatId);

        /**
         * Удаляет указанную категорию и все её дочерние категории.
         *
         * @param category Название категории, которую следует удалить.
         * @param chatId   Идентификатор чата.
         * @return Сообщение о результате операции.
         */
        String remove(String category, long chatId);
}


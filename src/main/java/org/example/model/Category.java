package org.example.model;

import lombok.Data;


import javax.persistence.*;
@Data
@Entity
@Table(name = "category")
public class Category {
    /**
     * Уникальный идентификатор категории.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Идентификатор родительской категории.
     */
    @Column(name = "parent_id")
    private Long parentId;
    /**
     * Название категории.
     * <p>
     * Название категории является обязательным полем и имеет ограничение по длине
     * в 30 символов.
     */
    @Column(nullable = false, length = 30)
    private String name;

    /**
     * Идентификатор чата.
     */
    @Column(name = "chat_id")
    private long chatId;

    /**
     * Конструктор класса Category.
     */
    public Category(Long id, String child, long chatId) {
        this.parentId = parentId;
        this.name = name;
        this.chatId = this.chatId;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }
}
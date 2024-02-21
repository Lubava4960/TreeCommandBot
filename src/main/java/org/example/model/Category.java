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
    @Column( length = 30)
    private String title;

    /**
     * Идентификатор чата.
     */
    @Column(name = "chat_id")
    private long chatId;

    /**
     * Конструктор класса Category.
     */

    public Category(Long id, Long parentId, String title, long chatId) {
        this.id = id;
        this.parentId = parentId;
        this.title = title;
        this.chatId = chatId;
    }

    public Category() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setName() {
        this.title = title;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }
}

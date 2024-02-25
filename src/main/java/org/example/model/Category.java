package org.example.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
@Data
@Getter
@Setter
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
    private String name;

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
        this.name = getName();
        this.chatId = chatId;
    }

    public Category(long l, String root, long chatId) {

    }


}

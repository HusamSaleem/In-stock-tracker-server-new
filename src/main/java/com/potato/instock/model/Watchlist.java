package com.potato.instock.model;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Watchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String uid;
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private List<Item> items;
}

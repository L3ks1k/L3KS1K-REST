package com.example.l3ks1krestapi.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "onetimeprekeys", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class OnetimePrekey {
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;
    private String oneTimePrekey;
}

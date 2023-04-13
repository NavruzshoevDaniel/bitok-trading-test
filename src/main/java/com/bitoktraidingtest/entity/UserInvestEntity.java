package com.bitoktraidingtest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "user_invest")
public class UserInvestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_invest_gen")
    @SequenceGenerator(name = "user_invest_gen", sequenceName = "user_invest_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "amount", nullable = false)
    private Double amount;
}
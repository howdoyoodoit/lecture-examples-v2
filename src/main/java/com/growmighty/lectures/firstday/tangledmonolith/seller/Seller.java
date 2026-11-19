package com.growmighty.lectures.firstday.tangledmonolith.seller;

//import com.growmighty.lectures.firstday.tangledmonolith.product.Product;
//import com.growmighty.lectures.firstday.tangledmonolith.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sellers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    @OneToMany(mappedBy = "seller")
    private List<Product> products = new ArrayList<>();
    */

    @Column(name = "user_id")
    private Long userId;

    // Seller 생성 시 User 객체 대신 사용자 ID를 전달
    public static Seller create(Long userId) {
        Seller seller = new Seller();
        seller.userId = userId;
        
        return seller;
    }
}

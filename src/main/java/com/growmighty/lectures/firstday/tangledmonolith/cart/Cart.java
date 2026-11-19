package com.growmighty.lectures.firstday.tangledmonolith.cart;

//import com.growmighty.lectures.firstday.tangledmonolith.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    // User 객체 직접 참조
    /*
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    */

    // User의 ID 값만
    @Column(name = "user_id")
    private Long userId;


    // User의 객체를 전달받아서 Cart에 저장, 이제 Cart에 User 객체가 없으므로 변경
    /*
    public static Cart create(User user) {
        Cart cart = new Cart();
        cart.user = user;
        return cart;
    }
     */

    public static Cart create(Long userId) {
        Cart cart = new Cart();
        cart.userId = userId;
        return cart;
    }



    public void addItem(CartItem item) {
        this.items.add(item);

        if (item.getCart() != this) {
            item.assignCart(this);
        }
    }

    public void clear() {
        this.items.clear();
    }
}

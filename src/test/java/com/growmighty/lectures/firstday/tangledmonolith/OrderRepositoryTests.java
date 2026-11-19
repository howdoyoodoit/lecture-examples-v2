package com.growmighty.lectures.firstday.tangledmonolith;

import com.growmighty.lectures.firstday.tangledmonolith.order.Order;
import com.growmighty.lectures.firstday.tangledmonolith.order.OrderItem;
import com.growmighty.lectures.firstday.tangledmonolith.order.OrderRepository;
import com.growmighty.lectures.firstday.tangledmonolith.product.Product;
import com.growmighty.lectures.firstday.tangledmonolith.seller.Seller;
import com.growmighty.lectures.firstday.tangledmonolith.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("주문 저장 및 조회 테스트")
    void saveAndFindOrderTest() {
        User user = User.register("dannyseo@growmighty", "aaa33242", "Danny", "010-0000-0000");
        Seller seller = Seller.create(user.getId());
        Product product = Product.create(seller.getId(), "Dofia 이동식 접이식 식탁 의자 4개 세트 가정용 소형주택 신축식", BigDecimal.valueOf(179000), 10, "test");
        entityManager.persist(user);
        entityManager.persist(seller);
        entityManager.persist(product);

        List<OrderItem> items = new ArrayList<>();
        items.add(OrderItem.create(product.getId(), 1));
        Order order = Order.create(user.getId(), items);

        Order saved = orderRepository.save(order);
        entityManager.flush();
        entityManager.clear();

        Order found = orderRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getItems()).hasSize(1);
        OrderItem foundItem = found.getItems().get(0);
        assertThat(foundItem.getQuantity()).isEqualTo(1);

        /*
        assertThat(foundItem.getProduct().getId()).isEqualTo(product.getId());
        assertThat(foundItem.getProduct().getName()).isEqualTo(product.getName());
        assertThat(foundItem.getProduct().getPrice()).isEqualByComparingTo(product.getPrice());
        */

        // OrderItem에 저장된 상품 ID를 이용해 Product 객체를 별도로 조회한다.
        // 기존에는 foundItem.getProduct()로 상품 객체에 직접 접근했지만,
        // 도메인 간 객체 연관관계를 제거했기 때문에 ID를 통해 조회해야 한다.
        Product foundProduct = entityManager.find(Product.class, foundItem.getProductId());

        // 조회한 상품의 ID가 주문 항목에 저장된 상품 ID와 동일한지 검증
        assertThat(foundProduct.getId()).isEqualTo(product.getId());
        // 조회한 상품의 이름이 기존 상품 이름과 동일한지 검증
        assertThat(foundProduct.getName()).isEqualTo(product.getName());

        // 조회한 상품의 가격이 기존 상품 가격과 동일한지 검증
        // BigDecimal은 소수점 표현 방식이 다를 수 있으므로
        // isEqualTo() 대신 isEqualByComparingTo()를 사용하여 값 자체를 비교
        assertThat(foundProduct.getPrice()).isEqualByComparingTo(product.getPrice());
    }
}

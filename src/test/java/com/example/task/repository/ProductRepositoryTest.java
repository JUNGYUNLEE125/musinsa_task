package com.example.task.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.task.model.Product;

@DataJpaTest // 데이터베이스 관련 테스트를 위한 어노테이션
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository; // ProductRepository 주입

    private Product product; // 테스트할 Product 객체

    @BeforeEach
    public void setUp() {
        // 각 테스트가 실행되기 전에 호출되어 테스트용 Product 객체를 초기화
        product = Product.builder()
                .brand("A")         // 브랜드 설정
                .category("스니커즈")  // 카테고리 설정
                .price(1000)              // 가격 설정
                .build();                 // Product 객체 생성

        // 테스트용 Product 객체를 DB에 저장
        productRepository.save(product);
    }

    @Test
    @DisplayName("카테고리와 브랜드가 존재하는지 체크")
    public void testExistsByCategoryAndBrand() {
        boolean exists = productRepository.existsByCategoryAndBrand("스니커즈", "A");
        assertTrue(exists);  // 존재하는 경우 true여야 함
    }

    @Test
    @DisplayName("모든 카테고리에서 최저 가격을 가진 상품을 찾아오는 메서드 테스트")
    public void testFindLowestPricesForAllCategories() {
        List<Product> products = productRepository.findLowestPricesForAllCategories();
        assertNotNull(products);  // 제품 리스트가 null이 아니어야 함
        assertFalse(products.isEmpty());  // 리스트가 비어있지 않아야 함
    }

    @Test
    @DisplayName("모든 카테고리에서 최저 가격 총합을 가진 브랜드를 찾아오는 메서드 테스트")
    public void testFindBrandWithLowestTotalPrice() {
        String brand = productRepository.findBrandWithLowestTotalPrice();
        assertNotNull(brand);  // 브랜드 이름이 null이 아니어야 함
        assertEquals("A", brand);  // A 브랜드여야 함
    }

    @Test
    @DisplayName("특정 브랜드에 대해 카테고리별 최저가 상품을 조회하는 메서드 테스트")
    public void testFindLowestPricesByBrand() {
        List<Product> products = productRepository.findLowestPricesByBrand("A");
        assertNotNull(products);  // 제품 리스트가 null이 아니어야 함
        assertFalse(products.isEmpty());  // 리스트가 비어있지 않아야 함
        assertEquals("A", products.get(0).getBrand());  // 브랜드가 "A"이어야 함
    }

    @Test
    @DisplayName("특정 카테고리에서 최저가 상품을 조회하는 메서드 테스트")
    public void testFindTop1ProductByCategoryOrderByPriceAsc() {
        Product product = productRepository.findTop1ProductByCategoryOrderByPriceAsc("스니커즈");
        assertNotNull(product);  // 상품이 null이 아니어야 함
        assertEquals("A", product.getBrand());  // 브랜드가 "A"이어야 함
    }

    @Test
    @DisplayName("특정 카테고리에서 최고가 상품을 조회하는 메서드 테스트")
    public void testFindTop1ProductByCategoryOrderByPriceDesc() {
        Product product = productRepository.findTop1ProductByCategoryOrderByPriceDesc("스니커즈");
        assertNotNull(product);  // 상품이 null이 아니어야 함
        assertEquals("A", product.getBrand());  // 브랜드가 "A"이어야 함
    }
}

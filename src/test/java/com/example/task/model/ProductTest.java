package com.example.task.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Validator validator; // Validator 객체 선언

    @BeforeEach
    public void setUp() {
        // 각 테스트가 실행되기 전에 호출되어 Validator 객체를 초기화
        // Validator는 Java Bean Validation API를 사용하여 객체의 유효성을 검증하는 역할을 합니다.
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("Product 객체 유효성 검사 테스트") 
    public void testValidProduct() {
        Product product = new Product(1L, "A", "스니커즈", 1000);

        // Product 객체의 유효성 검사를 실행
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // 유효성 검사가 통과된 경우 violations가 비어 있어야 한다.
        // violations는 유효성 검사를 통과하지 못한 필드에 대한 정보를 담고 있습니다.
        assertTrue(violations.isEmpty()); // 유효성 검사 오류가 없어야 함
    }

    @Test
    @DisplayName("Product 객체 유효성 검사 테스트 - 브랜드가 빈 값인 경우")
    public void testValidProductByBrand() {
        Product product = new Product(1L, "", "스니커즈", 1000);

        // 유효성 검사 실행
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // 유효성 검사 오류가 있어야 하므로 violations가 비어 있지 않아야 한다.
        assertFalse(violations.isEmpty()); // 유효성 검사 오류가 있어야 함
        assertEquals(1, violations.size()); // 오류가 정확히 1개 있어야 함
        assertEquals("브랜드는 필수 입력값입니다.", violations.iterator().next().getMessage()); // 오류 메시지가 "브랜드는 필수 입력값입니다."여야 함
    }

    @Test
    @DisplayName("Product 객체 유효성 검사 테스트 - 카테고리가 빈 값인 경우")
    public void testValidProductByCategory() {
        Product product = new Product(1L, "A", "", 1000);

        // 유효성 검사 실행
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // 유효성 검사 오류가 있어야 하므로 violations가 비어 있지 않아야 한다.
        assertFalse(violations.isEmpty()); // 유효성 검사 오류가 있어야 함
        assertEquals(1, violations.size()); // 오류가 정확히 1개 있어야 함
        assertEquals("카테고리는 필수 입력값입니다.", violations.iterator().next().getMessage()); // 오류 메시지가 "카테고리는 필수 입력값입니다."여야 함
    }

    @Test
    @DisplayName("Product 객체 유효성 검사 테스트 - 가격이 음수인 경우")
    void wtestValidProductByPrice() {
        Product product = new Product(1L, "A", "가방", -1000);
        
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty()); // 유효성 검사 오류가 있어야 함
        assertEquals("가격은 0 이상의 값이어야 합니다.", violations.iterator().next().getMessage()); // 오류 메시지가 "가격은 0 이상의 값이어야 합니다."여야 함
    }
}

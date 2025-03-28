package com.example.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.task.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByCategoryAndBrand(String category, String brand);

    // 카테고리 별로 최저가격인 브랜드와 가격을 조회, 카테고리별 최저 가격이 중복 추출되어 중복제거 로직이 필요
    @Query("SELECT p FROM Product p WHERE p.price = (SELECT MIN(p2.price) FROM Product p2 WHERE p2.category = p.category)")
    List<Product> findLowestPricesForAllCategories();

    // 모든 카테고리 구매시 최저가 브랜드 조회
    @Query("SELECT p.brand FROM Product p GROUP BY p.brand ORDER BY SUM(p.price) ASC LIMIT 1")
    String findBrandWithLowestTotalPrice();
    
    // 브랜드, 카테고리별 최저가 상품
    @Query("SELECT p FROM Product p WHERE p.brand = :brand AND p.price = (SELECT MIN(p2.price) FROM Product p2 WHERE p2.category = p.category AND p2.brand = :brand)")
    List<Product> findLowestPricesByBrand(@Param("brand") String brand);
    
    // 해당 카테고리에서 최저가 상품 조회
    Product findTop1ProductByCategoryOrderByPriceAsc(@Param("category") String category);
    
    // 해당 카테고리에서 최고가 상품 조회
    Product findTop1ProductByCategoryOrderByPriceDesc(@Param("category") String category);
    
}

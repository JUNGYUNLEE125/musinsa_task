package com.example.task.service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.task.exception.BaseException;
import com.example.task.model.Product;
import com.example.task.model.result.ResultCode;
import com.example.task.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Cacheable("minPriceByCategory")
    public Map<String, Object> getLowestPricesByCategory() {
        List<Product> productList = productRepository.findLowestPricesForAllCategories();
        if (productList == null)
            throw new BaseException(ResultCode.NO_DATA, "조회할 데이터가 없습니다."); 

        try {
            Map<String, Object> response = new LinkedHashMap<>();
            NumberFormat numberFormat = NumberFormat.getInstance();

            // 카테고리별 최저가격 브랜드 중복 제거 로직
            Map<String, Product> uniqueProductsByCategory = productList.stream()
                .collect(Collectors.toMap(
                    Product::getCategory, 
                    product -> product, 
                    (existing, replacement) -> replacement));

            List<Map<String, String>> dataList = uniqueProductsByCategory.values().stream()
            .map(product -> {
                Map<String, String> item = new HashMap<>();
                item.put("카테고리", product.getCategory());
                item.put("브랜드", product.getBrand());
                item.put("가격", numberFormat.format(product.getPrice())); // 가격을 문자열로 변환
                return item;
            })
            .collect(Collectors.toList());

            int total = uniqueProductsByCategory.values().stream().mapToInt(Product::getPrice).sum();

            response.put("상품리스트", dataList);
            response.put("총액", numberFormat.format(total));

            return response;
        } catch (Exception e){
            throw new BaseException(ResultCode.INTERNAL_SERVER_ERROR, String.format("서버 내부 처리 중 오류 발생, Reason: %d", e.getMessage()));
        }
    }

    @Cacheable("minPriceByBrand")
    public Map<String, Object> getLowestPricesBySingleBrand() {
        NumberFormat numberFormat = NumberFormat.getInstance();

        // 1️⃣ 최저가 브랜드 조회
        String brand = productRepository.findBrandWithLowestTotalPrice();
        if (brand == null) {
            throw new BaseException(ResultCode.NO_DATA, "최저가 브랜드를 찾을 수 없습니다.");
        }

        // 2️⃣ 해당 브랜드의 카테고리별 최저가 상품 조회
        List<Product> productList = productRepository.findLowestPricesByBrand(brand);
        if (productList.isEmpty()) {
            throw new BaseException(ResultCode.NO_DATA, "해당 브랜드의 상품 데이터가 없습니다.");
        }

        try {
            // 3️⃣ 응답 데이터 생성
            Map<String, Object> response = new LinkedHashMap<>();
            List<Map<String, Object>> categoryList = new ArrayList<>();
            int total = 0;
    
            for (Product product : productList) {
                Map<String, Object> item = new HashMap<>();
                item.put("카테고리", product.getCategory());
                item.put("가격", numberFormat.format(product.getPrice()));
                categoryList.add(item);
                total += product.getPrice();
            }
    
            Map<String, Object> lowestPriceData = new LinkedHashMap<>();
            lowestPriceData.put("브랜드", brand);
            lowestPriceData.put("카테고리", categoryList);
            lowestPriceData.put("총액", numberFormat.format(total));
    
            response.put("최저가", lowestPriceData);
            return response;

        } catch(Exception e) {
            throw new BaseException(ResultCode.INTERNAL_SERVER_ERROR, String.format("서버 내부 처리 중 오류 발생, Reason: %d", e.getMessage()));
        }
    }

    @Cacheable("infoPriceByCategory")
    public Map<String, Object> getPriceInfoByCategory(String category) {
        NumberFormat numberFormat = NumberFormat.getInstance();

        // 카테고리로 최저가 및 최고가 상품 조회
        Product lowestPriceProduct = productRepository.findTop1ProductByCategoryOrderByPriceAsc(category);
        Product highestPriceProduct = productRepository.findTop1ProductByCategoryOrderByPriceDesc(category);

        // 카테고리 정보가 없다면 예외 처리
        if (lowestPriceProduct == null || highestPriceProduct == null) {
            throw new BaseException(ResultCode.NO_DATA, "해당 카테고리의 상품 정보가 없습니다.");
        }

        try {
            // 최저가, 최고가 가격 응답 데이터 생성
            Map<String, Object> response = new HashMap<>();
            response.put("카테고리", category);

            List<Map<String, Object>> lowestPriceList = new ArrayList<>();
            Map<String, Object> lowestPrice = new HashMap<>();
            lowestPrice.put("브랜드", lowestPriceProduct.getBrand());
            lowestPrice.put("가격", numberFormat.format(lowestPriceProduct.getPrice()));
            lowestPriceList.add(lowestPrice);

            List<Map<String, Object>> highestPriceList = new ArrayList<>();
            Map<String, Object> highestPrice = new HashMap<>();
            highestPrice.put("브랜드", highestPriceProduct.getBrand());
            highestPrice.put("가격", numberFormat.format(highestPriceProduct.getPrice()));
            highestPriceList.add(highestPrice);

            response.put("최저가", lowestPriceList);
            response.put("최고가", highestPriceList);

            return response;
        } catch(Exception e) {
            throw new BaseException(ResultCode.INTERNAL_SERVER_ERROR, String.format("서버 내부 처리 중 오류 발생, Reason: %d", e.getMessage()));
        }
    }
        
    // 1) 상품 추가
    @CacheEvict(value = { "minPriceByCategory", "minPriceByBrand", "infoPriceByCategory" }, allEntries = true)
    public Product addProduct(Product product) {        
        if (productRepository.existsByCategoryAndBrand(product.getCategory(), product.getBrand())) {
            throw new BaseException(ResultCode.ALREADY_EXISTS, "상품 정보를 확인해주세요.");
        }
        return productRepository.save(product);
    }

    // 2) 상품 업데이트
    @CacheEvict(value = { "minPriceByCategory", "minPriceByBrand", "infoPriceByCategory" }, allEntries = true)
    public void updateProduct(Long id, Product product) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isEmpty()) {
            throw new BaseException(ResultCode.NO_DATA, "업데이트할 상품을 찾을 수 없습니다.");
        }
        Product updatedProduct = existingProduct.get();
        updatedProduct.setCategory(product.getCategory());
        updatedProduct.setBrand(product.getBrand());
        updatedProduct.setPrice(product.getPrice());
        productRepository.save(updatedProduct);
    }

    // 3) 상품 삭제
    @CacheEvict(value = { "minPriceByCategory", "minPriceByBrand", "infoPriceByCategory" }, allEntries = true)
    public void deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new BaseException(ResultCode.NO_DATA, "삭제할 상품을 찾을 수 없습니다.");
        }
        productRepository.deleteById(id);
    }
}

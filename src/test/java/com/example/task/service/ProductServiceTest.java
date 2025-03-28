package com.example.task.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.task.exception.BaseException;
import com.example.task.model.Product;
import com.example.task.model.result.ResultCode;
import com.example.task.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("getLowestPricesByCategory 메서드 테스트")
    public void testGetLowestPricesByCategory() {
        Product product1 = new Product(1L, "A", "가방", 1000);
        Product product2 = new Product(2L, "B", "아우터", 2000);
        Product product3 = new Product(3L, "C", "스니커즈", 5000);
        List<Product> products = Arrays.asList(product1, product2, product3);

        given(productRepository.findLowestPricesForAllCategories()).willReturn(products);
        
        Map<String, Object> response = productService.getLowestPricesByCategory();

        assertNotNull(response);
        assertTrue(response.containsKey("상품리스트"));
        assertTrue(response.containsKey("총액"));

        @SuppressWarnings("unchecked")
        List<Map<String, String>> dataList = (List<Map<String, String>>) response.get("상품리스트");
        assertEquals(3, dataList.size());
        String totalAmount = (String) response.get("총액");
        assertEquals("8,000", totalAmount);
    }

    @Test
    @DisplayName("getLowestPricesBySingleBrand 메서드 테스트")
    public void testGetLowestPricesBySingleBrand() {
        String lowestPriceBrand = "스니커즈";
        given(productRepository.findBrandWithLowestTotalPrice()).willReturn(lowestPriceBrand);

        Product product1 = new Product(1L, "A", lowestPriceBrand, 1000);
        Product product2 = new Product(2L, "B", lowestPriceBrand, 5000);
        List<Product> products = Arrays.asList(product1, product2);        
        given(productRepository.findLowestPricesByBrand(lowestPriceBrand)).willReturn(products);

        Map<String, Object> response = productService.getLowestPricesBySingleBrand();

        assertNotNull(response);
        assertTrue(response.containsKey("최저가"));

        @SuppressWarnings("unchecked")
        Map<String, Object> lowestPriceData = (Map<String, Object>) response.get("최저가");
        assertEquals(lowestPriceBrand, lowestPriceData.get("브랜드"));
        assertTrue(lowestPriceData.containsKey("카테고리"));
        assertTrue(lowestPriceData.containsKey("총액"));
    }

    @Test
    @DisplayName("getPriceInfoByCategory 메서드 테스트")
    public void tesetGetPriceInfoByCategory() {
        String category = "가방";
        Product lowestProduct = new Product(1L, "A", category, 1000);
        Product highestProduct = new Product(2L, "B", category, 3000);
        
        given(productRepository.findTop1ProductByCategoryOrderByPriceAsc(category)).willReturn(lowestProduct);
        given(productRepository.findTop1ProductByCategoryOrderByPriceDesc(category)).willReturn(highestProduct);

        Map<String, Object> response = productService.getPriceInfoByCategory(category);

        assertNotNull(response);
        assertEquals(category, response.get("카테고리"));
        assertTrue(response.containsKey("최저가"));
        assertTrue(response.containsKey("최고가"));
    }

    @Test
    @DisplayName("addProduct 메서드 테스트 - 이미 상품이 존재하는 경우 예외 발생 확인")
    public void testAddProductByExistsError() {
        Product product = new Product(1L, "A", "가방", 2000);
        
        given(productRepository.existsByCategoryAndBrand(product.getCategory(), product.getBrand())).willReturn(true);

        BaseException exception = assertThrows(BaseException.class, () -> {
            productService.addProduct(product);
        });
        assertEquals(ResultCode.ALREADY_EXISTS, exception.getResultCode());
    }

    @Test
    @DisplayName("addProduct 메서드 테스트 - 상품이 존재하지 않는 경우 저장 성공 확인")
    public void testAddProductSave() {
        Product product = new Product(1L, "A", "가방", 2000);
                
        given(productRepository.existsByCategoryAndBrand(product.getCategory(), product.getBrand())).willReturn(false);
        given(productRepository.save(product)).willReturn(product);
        
        Product savedProduct = productService.addProduct(product);

        assertNotNull(savedProduct);
        assertEquals(product, savedProduct);
    }

    @Test
    @DisplayName("updateProduct 메서드 테스트 - 상품이 존재하는 경우 업데이트 확인")
    public void testUpdateProductExistsSuccess() {
        Long productId = 1L;
        Product existingProduct = new Product(1L, "A", "가방", 1000);
        Product updatedInfo = new Product(1L, "B", "아우터", 2000);
        given(productRepository.findById(productId)).willReturn(Optional.of(existingProduct));
        given(productRepository.save(any(Product.class))).willAnswer(invocation -> invocation.getArgument(0));

        productService.updateProduct(productId, updatedInfo);

        verify(productRepository).save(existingProduct);
        assertEquals("아우터", existingProduct.getCategory());
        assertEquals("B", existingProduct.getBrand());
        assertEquals(2000, existingProduct.getPrice());
    }

    @Test
    @DisplayName("updateProduct 메서드 테스트 - 상품이 존재하지 않는 경우 예외 발생 확인")
    public void testUpdateProductNotExistError() {
        Long productId = 1L;
        Product updatedInfo = new Product(1L, "B", "가방", 2000);
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        // when & then
        BaseException exception = assertThrows(BaseException.class, () -> {
            productService.updateProduct(productId, updatedInfo);
        });
        assertEquals(ResultCode.NO_DATA, exception.getResultCode());
    }

    @Test
    @DisplayName("deleteProduct 메서드 테스트 - 상품이 존재하는 경우 삭제 확인")
    public void testDeleteProductExistsSuccess() {
        Long productId = 1L;
        Product product = new Product(1L, "A", "가방", 100);
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        productService.deleteProduct(productId);

        verify(productRepository).deleteById(productId);
    }

    @Test
    @DisplayName("deleteProduct 메서드 테스트 - 상품이 존재하지 않는 경우 예외 발생 확인")
    public void testDeleteProductNotExistError() {
        Long productId = 1L;
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        BaseException exception = assertThrows(BaseException.class, () -> {
            productService.deleteProduct(productId);
        });
        assertEquals(ResultCode.NO_DATA, exception.getResultCode());
    }
}

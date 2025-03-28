package com.example.task.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.task.exception.BaseException;
import com.example.task.model.Product;
import com.example.task.model.result.ResultCode;
import com.example.task.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    // MockMvc는 실제 서버를 구동하지 않고 MVC 테스트를 수행할 수 있게 해줍니다.
    @Autowired
    private MockMvc mockMvc;

    // ProductService는 실제 서비스 객체 대신 가짜(Mock) 객체로 주입됩니다.
    @MockBean
    private ProductService productService;

    // ObjectMapper는 Java 객체를 JSON 문자열로 직렬화하거나 역직렬화할 때 사용됩니다.
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("카테고리별 최저가 조회 테스트")
    void testGetLowestPricesByCategory() throws Exception {
        // 예상되는 응답 데이터 설정
        Map<String, Object> mockResponse = new HashMap<>();
        List<Map<String, String>> productList = new ArrayList<>();

        productList.add(Map.of("카테고리", "액세서리", "브랜드", "F", "가격", "1,900"));
        productList.add(Map.of("카테고리", "양말", "브랜드", "I", "가격", "1,700"));
        productList.add(Map.of("카테고리", "아우터", "브랜드", "E", "가격", "5,000"));
        productList.add(Map.of("카테고리", "바지", "브랜드", "D", "가격", "3,000"));
        productList.add(Map.of("카테고리", "스니커즈", "브랜드", "G", "가격", "9,000"));
        productList.add(Map.of("카테고리", "가방", "브랜드", "A", "가격", "2,000"));
        productList.add(Map.of("카테고리", "모자", "브랜드", "D", "가격", "1,500"));
        productList.add(Map.of("카테고리", "상의", "브랜드", "C", "가격", "10,000"));

        mockResponse.put("상품리스트", productList);
        mockResponse.put("총액", "34,100");

        // Mock 서비스 응답 설정
        given(productService.getLowestPricesByCategory()).willReturn(mockResponse);

        // API 호출 및 검증
        mockMvc.perform(get("/api/products/minPriceByCategory"))
                .andExpect(status().isOk()) // HTTP 200 상태 코드 확인
                .andExpect(jsonPath("$.resultCode").value("000")) // resultCode 검증
                .andExpect(jsonPath("$.resultMessage").value("성공")) // resultMessage 검증
                .andExpect(jsonPath("$.resultData.상품리스트").isArray()) // 상품 리스트가 배열인지 확인
                .andExpect(jsonPath("$.resultData.상품리스트.length()").value(8)) // 상품 리스트 길이 확인
                .andExpect(jsonPath("$.resultData.총액").value("34,100")); // 총액 검증
    }


    @Test
    @DisplayName("브랜드별 최저가 조회 테스트")
    void testGetLowestPricesBySingleBrand() throws Exception {
        // 1. Mock 데이터 설정 (실제 응답과 일치하도록 구성)
        Map<String, Object> mockResponse = new HashMap<>();
        
        // 최저가 정보를 담은 맵 생성
        Map<String, Object> lowestPriceData = new HashMap<>();
        lowestPriceData.put("브랜드", "D"); // 최저가 브랜드 설정

        // 카테고리별 최저가 리스트 생성
        List<Map<String, Object>> categoryList = List.of(
                Map.of("카테고리", "상의", "가격", "10,100"),
                Map.of("카테고리", "아우터", "가격", "5,100"),
                Map.of("카테고리", "바지", "가격", "3,000"),
                Map.of("카테고리", "스니커즈", "가격", "9,500"),
                Map.of("카테고리", "가방", "가격", "2,500"),
                Map.of("카테고리", "모자", "가격", "1,500"),
                Map.of("카테고리", "양말", "가격", "2,400"),
                Map.of("카테고리", "액세서리", "가격", "2,000")
        );

        // 카테고리 리스트 및 총액 추가
        lowestPriceData.put("카테고리", categoryList);
        lowestPriceData.put("총액", "36,100");

        // 최종 응답 데이터 구성
        mockResponse.put("최저가", lowestPriceData);

        // 2. Mock 객체 동작 설정 (서비스가 호출되면 위 데이터 반환)
        given(productService.getLowestPricesBySingleBrand()).willReturn(mockResponse);

        // 3. API 호출 및 응답 검증
        mockMvc.perform(get("/api/products/minPriceByBrand") // GET 요청
                .contentType(MediaType.APPLICATION_JSON)) // JSON 형식 요청
                .andExpect(status().isOk()) // HTTP 응답 코드 200 확인
                .andExpect(jsonPath("$.resultCode").value("000")) // resultCode가 "000"인지 확인
                .andExpect(jsonPath("$.resultMessage").value("성공")) // resultMessage가 "성공"인지 확인
                .andExpect(jsonPath("$.resultData.최저가.브랜드").value("D")) // 최저가 브랜드가 "D"인지 확인
                .andExpect(jsonPath("$.resultData.최저가.카테고리.length()").value(8)) // 카테고리 목록 길이 8개인지 확인
                .andExpect(jsonPath("$.resultData.최저가.총액").value("36,100")); // 총액이 "36,100"인지 확인
    }

    @Test
    @DisplayName("카테고리별 가격 정보 조회 테스트")
    void testGetPriceInfoByCategory() throws Exception {
        // 1. Mock 데이터 설정 (응답 데이터에 맞게 설정)
        Map<String, Object> mockResponse = Map.of(
                "카테고리", "상의", // 카테고리명 설정
                "최고가", Collections.singletonList(
                        Map.of("브랜드", "I", "가격", "11,400") // 최고가 정보
                ),
                "최저가", Collections.singletonList(
                        Map.of("브랜드", "C", "가격", "10,000") // 최저가 정보
                )
        );

        // 2. Mock 객체 동작 설정
        given(productService.getPriceInfoByCategory("상의")).willReturn(mockResponse);

        // 3. API 호출 및 응답 검증
        mockMvc.perform(get("/api/products/infoPriceByCategory") // GET 요청
                .param("category", "상의") // 카테고리 파라미터 설정
                .contentType(MediaType.APPLICATION_JSON)) // JSON 형식 요청
                .andExpect(status().isOk()) // HTTP 응답 코드 200 확인
                .andExpect(jsonPath("$.resultCode").value("000")) // resultCode가 "000"인지 확인
                .andExpect(jsonPath("$.resultMessage").value("성공")) // resultMessage가 "성공"인지 확인
                .andExpect(jsonPath("$.resultData.카테고리").value("상의")) // 카테고리가 "상의"인지 확인
                .andExpect(jsonPath("$.resultData.최고가[0].브랜드").value("I")) // 최고가 브랜드가 "I"인지 확인
                .andExpect(jsonPath("$.resultData.최고가[0].가격").value("11,400")) // 최고가 가격이 "11,400"인지 확인
                .andExpect(jsonPath("$.resultData.최저가[0].브랜드").value("C")) // 최저가 브랜드가 "C"인지 확인
                .andExpect(jsonPath("$.resultData.최저가[0].가격").value("10,000")); // 최저가 가격이 "10,000"인지 확인
    }

    @Test
    @DisplayName("상품 추가 테스트")
    void testAddProductSuccess() throws Exception {
        // 요청에 사용할 Product 객체 생성
        Product product = new Product(1L, "A", "가방", 2000);

        given(productService.addProduct(any(Product.class))).willReturn(product);

        // 요청 데이터
        String requestData = objectMapper.writeValueAsString(product);

        // API 호출 및 응답 검증
        mockMvc.perform(post("/api/products/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestData)) // POST 요청을 보냄
                .andDo(print()) // 응답 내용 출력
                .andExpect(status().isCreated()) // HTTP 201 상태 코드 검증
                .andExpect(jsonPath("$.resultCode").value("000")) // resultCode가 "000"인지 확인 (성공 코드)
                .andExpect(jsonPath("$.resultMessage").value("성공")) // resultMessage가 "성공"인지 확인
                .andExpect(jsonPath("$.resultData.category").value(product.getCategory()))
                .andExpect(jsonPath("$.resultData.brand").value(product.getBrand()))
                .andExpect(jsonPath("$.resultData.price").value(product.getPrice()));
    }
    
    @Test
    @DisplayName("상품 추가 테스트 : 잘못된 요청 (카테고리 없이 요청 시)")
    void testAddProductByCategory() throws Exception {        
        // 요청에 사용할 Product 객체 생성 (카테고리 비어있는 값)        
        Product product = new Product(1L, "A", "", 2000);

        // 요청 데이터
        String requestData = objectMapper.writeValueAsString(product);

        // 예외를 던지도록 설정 (BaseException)
        given(productService.addProduct(any(Product.class))).willThrow(new BaseException(ResultCode.INVALID_INPUT, "카테고리는 필수 입력값입니다."));

        // API 호출 및 응답 검증 (BAD_REQUEST 400 응답 검증)
        mockMvc.perform(post("/api/products/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestData))
                .andExpect(status().isBadRequest()) // HTTP 400 상태 코드 검증
                .andExpect(jsonPath("$.resultCode").value("103")) // resultCode가 "103"인지 확인
                .andExpect(jsonPath("$.resultMessage").value("입력오류")); // 에러 메시지 검증
    }

    @Test
    @DisplayName("상품 추가 테스트 : 잘못된 요청 (브랜드 없이 요청 시)")
    void testAddProductByBrand() throws Exception {        
        // 요청에 사용할 Product 객체 생성 (브랜드 비어있는 값)
        Product product = new Product(1L, "", "가방", 2000);
        String requestData = objectMapper.writeValueAsString(product);

        // 예외를 던지도록 설정 (BaseException)
        given(productService.addProduct(any(Product.class))).willThrow(new BaseException(ResultCode.INVALID_INPUT, "브랜드는 필수 입력값입니다."));

        // API 호출 및 응답 검증 (BAD_REQUEST 400 응답 검증)
        mockMvc.perform(post("/api/products/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestData))
                .andExpect(status().isBadRequest()) // HTTP 400 상태 코드 검증
                .andExpect(jsonPath("$.resultCode").value("103")) // resultCode가 "103"인지 확인
                .andExpect(jsonPath("$.resultMessage").value("입력오류")); // 에러 메시지 검증
    }

    @Test
    @DisplayName("상품 추가 실패 테스트 - 가격이 음수일 경우")
    void addProductByPrice() throws Exception {
        // 유효하지 않은 상품 객체 (가격이 음수)
        Product product = new Product(1L, "A", "가방", -1000);
        String requestData = objectMapper.writeValueAsString(product);

        // 예외를 던지도록 설정 (BaseException)
        given(productService.addProduct(any(Product.class))).willThrow(new BaseException(ResultCode.INVALID_INPUT, "가격은 0 이상의 값이어야 합니다."));
    
        // when & then: mockMvc를 통해 POST 요청 → 400 응답 확인
        mockMvc.perform(post("/api/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value("103"))
                .andExpect(jsonPath("$.resultMessage").value("입력오류")); // 실제 메시지는 상황에 맞게 조정
    }

    @Test
    @DisplayName("정상적인 상품 업데이트 요청에 대한 테스트")
    void testUpdateProductSuccess() throws Exception {
        Long productId = 1L;        
        Product product = new Product(productId, "A", "가방", 2000);
        String requestData = objectMapper.writeValueAsString(product);

        // Mock Service 동작 정의
        Mockito.doNothing().when(productService).updateProduct(productId, product);

        // when & then: PUT 요청 → 200 OK 및 메시지 검증
        mockMvc.perform(put("/api/products/update/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestData))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("000"))
                .andExpect(jsonPath("$.resultMessage").value("성공"));
            
        
    }

    @Test
    @DisplayName("상품 업데이트 실패 테스트 - 가격이 음수일 경우")
    void updateProductFailureBadRequestTest() throws Exception {                
        Product product = new Product(1L, "A", "가방", -1000);
        String requestData = objectMapper.writeValueAsString(product);

        // 서비스에서 BaseException 발생하도록 설정
        doThrow(new BaseException(ResultCode.INVALID_INPUT, "입력오류")).when(productService).updateProduct(eq(1L), any(Product.class));
    
        // API 호출 및 예외 검증 (HTTP 400 상태 코드 및 오류 메시지 검증)
        mockMvc.perform(put("/api/products/update/{id}", 1L) // PUT 요청을 보내며 {id} 값은 1로 설정
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestData)) // 요청 데이터를 JSON 형식으로 전달
                    .andDo(print()) // 응답 내용 출력
                    .andExpect(status().isBadRequest()) // HTTP 400 상태 코드 검증
                    .andExpect(jsonPath("$.resultCode").value("103")) // resultCode가 "103"인지 확인 (잘못된 요청 코드)
                    .andExpect(jsonPath("$.resultMessage").value("입력오류")); // resultMessage가 "잘못된 요청"인지 확인
    }

    @Test
    @DisplayName("서버 오류 발생 시 테스트: 데이터베이스 연결 문제")
    void testUpdateProductServerError() throws Exception {                  
        Product product = new Product(1L, "A", "가방", 3000);
        String requestData = objectMapper.writeValueAsString(product);
    
        // 예외를 던지도록 설정 (서비스에서 예외 발생 시)
        doThrow(new RuntimeException("서버 내부 오류")).when(productService).updateProduct(eq(1L), any(Product.class));
    
        // API 호출 및 예외 검증 (HTTP 500 상태 코드 및 오류 메시지 검증)
        mockMvc.perform(put("/api/products/update/{id}", 1L) // PUT 요청을 보내며 {id} 값은 1로 설정
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestData)) // 요청 데이터를 JSON 형식으로 전달
                    .andDo(print()) // 응답 내용 출력
                    .andExpect(status().isInternalServerError()) // HTTP 500 상태 코드 검증
                    .andExpect(jsonPath("$.resultCode").value("102")) // resultCode가 "102"인지 확인 (서버 오류 코드)
                    .andExpect(jsonPath("$.resultMessage").value("서버 내부 오류")); // resultMessage가 "서버 내부 오류"인지 확인
    }
    

    @Test
    @DisplayName("정상적인 상품 삭제 요청에 대한 테스트")
    void testDeleteProductSuccess() throws Exception {    
        // 상품 삭제가 성공적으로 처리되었음을 나타내는 mock 설정
        doNothing().when(productService).deleteProduct(1L); // 상품 ID 1을 삭제하도록 설정
    
        // 요청 데이터 (삭제할 상품 ID)
        // DELETE 요청을 보내며 {id}는 1L로 설정
        mockMvc.perform(delete("/api/products/delete/{id}", 1L)) // DELETE 요청을 보냄
                    .andDo(print()) // 응답 내용 출력
                    .andExpect(status().isOk()) // HTTP 200 상태 코드 검증
                    .andExpect(jsonPath("$.resultCode").value("000")) // resultCode가 "000"인지 확인 (성공 코드)
                    .andExpect(jsonPath("$.resultMessage").value("성공")); // resultMessage가 "성공"인지 확인
    }
    
    @Test
    @DisplayName("잘못된 상품 삭제 요청에 대한 테스트: 존재하지 않는 상품 ID")
    void testDeleteProductByBadRequest() throws Exception {
        // BaseException을 던지는 서비스 메서드 설정 (존재하지 않는 상품을 삭제하려는 경우)
        doThrow(new BaseException(ResultCode.INVALID_INPUT, "입력오류")).when(productService).deleteProduct(999L);
    
        // 요청 데이터 (잘못된 상품 ID)
        // DELETE 요청을 보내며 {id}는 존재하지 않는 999L로 설정
        mockMvc.perform(delete("/api/products/delete/{id}", 999L)) // DELETE 요청을 보냄
                    .andDo(print()) // 응답 내용 출력
                    .andExpect(status().isBadRequest()) // HTTP 400 상태 코드 검증
                    .andExpect(jsonPath("$.resultCode").value("103")) // resultCode가 "103"인지 확인 (입력오류 코드)
                    .andExpect(jsonPath("$.resultMessage").value("입력오류")); // resultMessage가 "입력오류"인지 확인
    }
    
    @Test
    @DisplayName("서버 오류 발생 시 테스트: 데이터베이스 연결 문제")
    void tesetDeleteProductByServerError() throws Exception {    
        // 예외를 던지도록 설정 (서비스에서 예외 발생 시)
        doThrow(new RuntimeException("서버 오류")).when(productService).deleteProduct(1L);
    
        // 요청 데이터 (삭제할 상품 ID)
        // DELETE 요청을 보내며 {id}는 1L로 설정
        mockMvc.perform(delete("/api/products/delete/{id}", 1L)) // DELETE 요청을 보냄
                    .andDo(print()) // 응답 내용 출력
                    .andExpect(status().isInternalServerError()) // HTTP 500 상태 코드 검증
                    .andExpect(jsonPath("$.resultCode").value("102")) // resultCode가 "102"인지 확인 (서버 오류 코드)
                    .andExpect(jsonPath("$.resultMessage").value("서버 내부 오류")); // resultMessage가 "서버 내부 오류"인지 확인
    }
        
}

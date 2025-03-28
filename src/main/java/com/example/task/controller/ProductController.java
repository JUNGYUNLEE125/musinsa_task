package com.example.task.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.task.exception.BaseException;
import com.example.task.model.Product;
import com.example.task.model.result.ResultCode;
import com.example.task.model.result.ResultValue;
import com.example.task.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    // 생성자: ProductService를 주입하여 사용
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1) 카테고리별 최저가 조회
    @GetMapping("/minPriceByCategory")
    public ResponseEntity<ResultValue> getLowestPricesByCategory() {
        try {
            // 상품 서비스에서 카테고리별 최저가 조회
            Map<String, Object> result =  productService.getLowestPricesByCategory();
            // 성공적인 결과 반환 (HTTP 200 OK)
            return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
        } catch (Exception e) {
            // 예외가 발생한 경우 서버 오류 응답 (HTTP 500 INTERNAL_SERVER_ERROR)
            return new ResponseEntity<>(new ResultValue(ResultCode.INTERNAL_SERVER_ERROR, "서버 내부 오류 발생"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2) 브랜드별 최저가 조회
    @GetMapping("/minPriceByBrand")
    public ResponseEntity<ResultValue> getLowestPricesBySingleBrand() {
        try {
            // 상품 서비스에서 브랜드별 최저가 조회
            Map<String, Object> result = productService.getLowestPricesBySingleBrand();
            // 성공적인 결과 반환 (HTTP 200 OK)
            return new ResponseEntity<>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
        } catch (Exception e) {
            // 예외가 발생한 경우 서버 오류 응답 (HTTP 500 INTERNAL_SERVER_ERROR)
            return new ResponseEntity<>(new ResultValue(ResultCode.INTERNAL_SERVER_ERROR, "서버 내부 오류 발생"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 3) 카테고리별 가격 정보 조회
    @GetMapping("/infoPriceByCategory")
    public ResponseEntity<ResultValue> getPriceInfoByCategory(@RequestParam(value="category") String category) {
        try {
            // 상품 서비스에서 카테고리별 가격 정보 조회
            Map<String, Object> result = productService.getPriceInfoByCategory(category);
            // 성공적인 결과 반환 (HTTP 200 OK)
            return new ResponseEntity<>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
        } catch (BaseException e) {
            // BaseException 발생 시 클라이언트의 잘못된 요청으로 판단하여 400 BAD_REQUEST 응답
            return new ResponseEntity<>(new ResultValue(e.getResultCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // 예외가 발생한 경우 서버 오류 응답 (HTTP 500 INTERNAL_SERVER_ERROR)
            return new ResponseEntity<>(new ResultValue(ResultCode.INTERNAL_SERVER_ERROR, "서버 내부 오류 발생"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 4) 상품 추가
    @PostMapping("/add")
    public ResponseEntity<ResultValue> addProduct(@Valid @RequestBody Product product) {
        try {            
            // 상품 추가 요청을 처리하고 결과 반환
            Product addedProduct = productService.addProduct(product);
            // 상품이 성공적으로 추가되었음을 나타내는 응답 (HTTP 201 CREATED)
            return new ResponseEntity<>(new ResultValue(ResultCode.SUCCESS, addedProduct), HttpStatus.CREATED);
        } catch (BaseException e) {
            // BaseException 발생 시 클라이언트의 잘못된 요청으로 판단하여 응답
            return new ResponseEntity<>(new ResultValue(e.getResultCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // 예외가 발생한 경우 서버 오류 응답 (HTTP 500 INTERNAL_SERVER_ERROR)
            return new ResponseEntity<>(new ResultValue(ResultCode.INTERNAL_SERVER_ERROR, "서버 내부 오류 발생"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 5) 상품 업데이트
    @PutMapping("/update/{id}")
    public ResponseEntity<ResultValue> updateProduct(@PathVariable("id") Long id, @Valid @RequestBody Product product) {
        try {
            // 상품 업데이트 처리
            productService.updateProduct(id, product);
            // 업데이트가 성공적으로 완료되었음을 나타내는 응답 (HTTP 200 OK)
            return new ResponseEntity<>(new ResultValue(ResultCode.SUCCESS, "상품이 성공적으로 업데이트되었습니다."), HttpStatus.OK);
        } catch (BaseException e) {
            // BaseException 발생 시 클라이언트의 잘못된 요청으로 판단하여 400 BAD_REQUEST 응답
            return new ResponseEntity<>(new ResultValue(e.getResultCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // 예외가 발생한 경우 서버 오류 응답 (HTTP 500 INTERNAL_SERVER_ERROR)
            return new ResponseEntity<>(new ResultValue(ResultCode.INTERNAL_SERVER_ERROR, "서버 내부 오류 발생"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 6) 상품 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResultValue> deleteProduct(@PathVariable("id") Long id) {
        try {
            // 상품 삭제 요청 처리
            productService.deleteProduct(id);
            // 상품 삭제가 성공적으로 완료되었음을 나타내는 응답 (HTTP 200 OK)
            return new ResponseEntity<>(new ResultValue(ResultCode.SUCCESS, "상품이 성공적으로 삭제되었습니다."), HttpStatus.OK);
        } catch (BaseException e) {
            // BaseException 발생 시 클라이언트의 잘못된 요청으로 판단하여 400 BAD_REQUEST 응답
            return new ResponseEntity<>(new ResultValue(e.getResultCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // 예외가 발생한 경우 서버 오류 응답 (HTTP 500 INTERNAL_SERVER_ERROR)
            return new ResponseEntity<>(new ResultValue(ResultCode.INTERNAL_SERVER_ERROR, "서버 내부 오류 발생"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

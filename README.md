# 프로젝트: [MUSINSA] Java(Kotlin) Backend Engineer Task

## 구현 범위

### Product 데이터 구조
| 항목 | 설명 |
|------|------|
| 보유 할 목록 | brand, category, price |
| 값 정보 | @Min, @NotBlank 등 유효성 검증 포함 |

### 주요 API 목록
| Method | Endpoint                                 | 설명                        |
|--------|------------------------------------------|-----------------------------|
| GET    | /api/products/lowest-price-by-category   | 카테고리별 최저가 상품 조회 |
| GET    | /api/products/lowest-price-by-brand      | 브랜드별 최저가 상품 묶음 조회 |
| GET    | /api/products/price-info-by-category     | 카테고리별 최저/최고가 조회 |
| POST   | /api/products/add                        | 상품 추가                   |
| PUT    | /api/products/update/{id}                | 상품 정보 업데이트           |
| DELETE | /api/products/delete/{id}                | 상품 삭제                   |

### 응답 코드 및 메시지
| 응답 코드 | 설명                                 | 메시지 예시                        |
|--------|------------------------------------------|-----------------------------|
| 000    | 성공   | "성공" |
| 101    | 데이터 없음      | "조회할 데이터가 없습니다." |
| 102    | 서버 내부 오류     | "서버 내부 오류" 또는 상세 예외 메시지 |
| 103   | 잘못된 입력 (Validation)                       | "입력오류", "카테고리는 필수 입력값입니다." 등                   |
| 104    | 이미 존재함                | "상품 정보가 이미 존재합니다."           |

### 테스트 범위
Controller, Service, Repository 계층 단위의 JUnit5 + Mockito 테스트 진행
Validation, Exception, Null 체크 포함 



### 코드 빌드, 테스트, 실행 방법

- 환경 요구 사항
JDK 17, Gradle 8.5

- Build 파일: build.gradle
  
- 빌드
./gradlew clean build

- 실행
./gradlew bootRun

- 테스트
./gradlew test



### 기타 추가 정보

- 플랫폼: Spring Boot + Spring Data JPA + Lombok + H2 (Test)
- 연결 DB: H2 및 현재는 Memory DB (MySQL 변경 가능)
- 테스트 모델: @WebMvcTest, @DataJpaTest, @MockBean 등을 통한 진관적 테스트
- 예외 처리: BaseException 및 ResultCode, ResultValue 기반 통일 응답 구조


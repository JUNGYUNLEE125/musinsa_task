CREATE TABLE product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    brand VARCHAR(50),
    category VARCHAR(50),
    price DECIMAL(10, 2)
);


-- 브랜드 A
INSERT INTO products (brand, category, price) VALUES ('A', '상의', 11200);
INSERT INTO products (brand, category, price) VALUES ('A', '아우터', 5500);
INSERT INTO products (brand, category, price) VALUES ('A', '바지', 4200);
INSERT INTO products (brand, category, price) VALUES ('A', '스니커즈', 9000);
INSERT INTO products (brand, category, price) VALUES ('A', '가방', 2000);
INSERT INTO products (brand, category, price) VALUES ('A', '모자', 1700);
INSERT INTO products (brand, category, price) VALUES ('A', '양말', 1800);
INSERT INTO products (brand, category, price) VALUES ('A', '액세서리', 2300);

-- 브랜드 B
INSERT INTO products (brand, category, price) VALUES ('B', '상의', 10500);
INSERT INTO products (brand, category, price) VALUES ('B', '아우터', 5900);
INSERT INTO products (brand, category, price) VALUES ('B', '바지', 3800);
INSERT INTO products (brand, category, price) VALUES ('B', '스니커즈', 9100);
INSERT INTO products (brand, category, price) VALUES ('B', '가방', 2100);
INSERT INTO products (brand, category, price) VALUES ('B', '모자', 2000);
INSERT INTO products (brand, category, price) VALUES ('B', '양말', 2000);
INSERT INTO products (brand, category, price) VALUES ('B', '액세서리', 2200);

-- 브랜드 C
INSERT INTO products (brand, category, price) VALUES ('C', '상의', 10000);
INSERT INTO products (brand, category, price) VALUES ('C', '아우터', 6200);
INSERT INTO products (brand, category, price) VALUES ('C', '바지', 3300);
INSERT INTO products (brand, category, price) VALUES ('C', '스니커즈', 9200);
INSERT INTO products (brand, category, price) VALUES ('C', '가방', 2200);
INSERT INTO products (brand, category, price) VALUES ('C', '모자', 1900);
INSERT INTO products (brand, category, price) VALUES ('C', '양말', 2200);
INSERT INTO products (brand, category, price) VALUES ('C', '액세서리', 2100);

-- 브랜드 D
INSERT INTO products (brand, category, price) VALUES ('D', '상의', 10100);
INSERT INTO products (brand, category, price) VALUES ('D', '아우터', 5100);
INSERT INTO products (brand, category, price) VALUES ('D', '바지', 3000);
INSERT INTO products (brand, category, price) VALUES ('D', '스니커즈', 9500);
INSERT INTO products (brand, category, price) VALUES ('D', '가방', 2500);
INSERT INTO products (brand, category, price) VALUES ('D', '모자', 1500);
INSERT INTO products (brand, category, price) VALUES ('D', '양말', 2400);
INSERT INTO products (brand, category, price) VALUES ('D', '액세서리', 2000);

-- 브랜드 E
INSERT INTO products (brand, category, price) VALUES ('E', '상의', 10700);
INSERT INTO products (brand, category, price) VALUES ('E', '아우터', 5000);
INSERT INTO products (brand, category, price) VALUES ('E', '바지', 3800);
INSERT INTO products (brand, category, price) VALUES ('E', '스니커즈', 9900);
INSERT INTO products (brand, category, price) VALUES ('E', '가방', 2300);
INSERT INTO products (brand, category, price) VALUES ('E', '모자', 1800);
INSERT INTO products (brand, category, price) VALUES ('E', '양말', 2100);
INSERT INTO products (brand, category, price) VALUES ('E', '액세서리', 2100);

-- 브랜드 F
INSERT INTO products (brand, category, price) VALUES ('F', '상의', 11200);
INSERT INTO products (brand, category, price) VALUES ('F', '아우터', 7200);
INSERT INTO products (brand, category, price) VALUES ('F', '바지', 4000);
INSERT INTO products (brand, category, price) VALUES ('F', '스니커즈', 9300);
INSERT INTO products (brand, category, price) VALUES ('F', '가방', 2100);
INSERT INTO products (brand, category, price) VALUES ('F', '모자', 1600);
INSERT INTO products (brand, category, price) VALUES ('F', '양말', 2300);
INSERT INTO products (brand, category, price) VALUES ('F', '액세서리', 1900);

-- 브랜드 G
INSERT INTO products (brand, category, price) VALUES ('G', '상의', 10500);
INSERT INTO products (brand, category, price) VALUES ('G', '아우터', 5800);
INSERT INTO products (brand, category, price) VALUES ('G', '바지', 3900);
INSERT INTO products (brand, category, price) VALUES ('G', '스니커즈', 9000);
INSERT INTO products (brand, category, price) VALUES ('G', '가방', 2200);
INSERT INTO products (brand, category, price) VALUES ('G', '모자', 1700);
INSERT INTO products (brand, category, price) VALUES ('G', '양말', 2100);
INSERT INTO products (brand, category, price) VALUES ('G', '액세서리', 2000);

-- 브랜드 H
INSERT INTO products (brand, category, price) VALUES ('H', '상의', 10800);
INSERT INTO products (brand, category, price) VALUES ('H', '아우터', 6300);
INSERT INTO products (brand, category, price) VALUES ('H', '바지', 3100);
INSERT INTO products (brand, category, price) VALUES ('H', '스니커즈', 9700);
INSERT INTO products (brand, category, price) VALUES ('H', '가방', 2100);
INSERT INTO products (brand, category, price) VALUES ('H', '모자', 1600);
INSERT INTO products (brand, category, price) VALUES ('H', '양말', 2000);
INSERT INTO products (brand, category, price) VALUES ('H', '액세서리', 2000);

-- 브랜드 I
INSERT INTO products (brand, category, price) VALUES ('I', '상의', 11400);
INSERT INTO products (brand, category, price) VALUES ('I', '아우터', 6700);
INSERT INTO products (brand, category, price) VALUES ('I', '바지', 3200);
INSERT INTO products (brand, category, price) VALUES ('I', '스니커즈', 9500);
INSERT INTO products (brand, category, price) VALUES ('I', '가방', 2400);
INSERT INTO products (brand, category, price) VALUES ('I', '모자', 1700);
INSERT INTO products (brand, category, price) VALUES ('I', '양말', 1700);
INSERT INTO products (brand, category, price) VALUES ('I', '액세서리', 2400);

CREATE TABLE category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(50)
);

INSERT INTO category (category) VALUES ('상의');
INSERT INTO category (category) VALUES ('아우터');
INSERT INTO category (category) VALUES ('바지');
INSERT INTO category (category) VALUES ('스니커즈');
INSERT INTO category (category) VALUES ('가방');
INSERT INTO category (category) VALUES ('모자');
INSERT INTO category (category) VALUES ('양말');
INSERT INTO category (category) VALUES ('액세서리');
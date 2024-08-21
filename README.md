# Tạp hóa Anh Béo

## Mục lục
1. [Giới thiệu](#giới-thiệu)
2. [Các tính năng](#các-tính-năng)
3. [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
4. [Cài đặt](#cài-đặt)
5. [Cấu trúc dự án](#cấu-trúc-dự-án)
6. [Sử dụng](#sử-dụng)
7. [Thông tin liên hệ](#thông-tin-liên-hệ)

## 1. Giới thiệu

Đây là dự án Bán hàng và Quản lý bán hàng dành cho cả Người bán và Người mua

## 2. Các tính năng

Các tính năng cơ bản của hệ thống bao gồm
- Phía người dùng:
  - Tìm kiếm sản phẩm
  - Đặt hàng
    - Đơn lẻ
    - Nhiều mặt hàng
  - Tra cứu đơn hàng
  - Hủy đơn hàng

## 3. Yêu cầu hệ thống

Các yêu cầu về phần mềm và phần cứng để chạy dự án:
- Sử dụng JDK 8
- Apache Tomcat 9
- MySQL 5.7
- Maven 3.6.x

## 4. Cài đặt

Hướng dẫn chi tiết để cài đặt dự án trên máy của bạn:

1. **Clone dự án**:
    ```bash
    git clone https://github.com/maskman97a/cg_ql_banhang.git

    ```
2. **Cấu hình cơ sở dữ liệu**: 
- Thông tin cấu hình được đưa vào file DatabaseConnection.java

3. **Cấu hình Maven**: Chạy lệnh sau để cài đặt các dependencies:
    ```bash
    mvn install
    ```
4. **Triển khai trên Tomcat**: Triển khai tệp `ROOT.war` trên Apache Tomcat.

## 5. Cấu trúc dự án

Mô tả cấu trúc thư mục của dự án, ví dụ:
```
/src
---/main
    ---/java
    ---/resources
    ---/webapp
        ---/WEB-INF
        ---/css
        ---/js
        ---/images
        ---/views
```

## 6. Sử dụng

- Chức năng Quản trị
  - Thông tin đăng nhâp: admin/admin

## 7. Thông tin liên hệ

Nếu có bất kỳ câu hỏi nào, bạn có thể liên hệ với tôi qua email: tungpham2311@gmail.com


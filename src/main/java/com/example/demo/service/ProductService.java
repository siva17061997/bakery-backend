package com.example.demo.service;

import com.example.demo.dto.ProductDto;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /* =================================================
       PUBLIC – LIST PRODUCTS (Pagination)
    ================================================= */
    public Page<ProductDto> getAll(Pageable pageable) {

        Page<Product> productPage;

        try {
            // If you have available-only query
            productPage = productRepository.findAllAvailable(pageable);
        } catch (Exception e) {
            // Fallback if custom method not present
            productPage = productRepository.findAll(pageable);
        }

        List<ProductDto> dtos = productPage
                .getContent()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(
                dtos,
                pageable,
                productPage.getTotalElements()
        );
    }

    /* =================================================
       PUBLIC – SIMPLE PAGE (3x3 grid)
    ================================================= */
    public Page<ProductDto> getAll(int page) {
        Pageable pageable = Pageable.ofSize(9).withPage(page);
        return getAll(pageable);
    }

    /* =================================================
       PUBLIC – GET PRODUCT BY ID (DTO)
    ================================================= */
    public ProductDto getById(Long id) {
        return productRepository.findById(id)
                .filter(Product::isAvailable)
                .map(this::convertToDto)
                .orElse(null);
    }

    /* =================================================
       ADMIN – GET ENTITY BY ID (EDIT)
       ❗ REQUIRED FOR UPDATE
    ================================================= */
    public Product findEntityById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    /* =================================================
       ADMIN – SAVE / UPDATE PRODUCT
    ================================================= */
    public Product save(Product product) {
        return productRepository.save(product);
    }

    /* =================================================
       ADMIN – DELETE PRODUCT
    ================================================= */
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    /* =================================================
       DTO CONVERSION
    ================================================= */
    private ProductDto convertToDto(Product product) {

        ProductDto dto = new ProductDto();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setImageUrl(product.getImageUrl());
        dto.setCategory(product.getCategory());
        dto.setSubcategory(product.getSubcategory());
        dto.setPrice(product.getPrice());
        dto.setMrp(product.getMrp());
        dto.setDiscount(product.getDiscount());
        dto.setGst(product.getGst());
        dto.setQtyType(product.getQtyType());
        dto.setQuantity(product.getQuantity());
        dto.setAvailable(product.isAvailable());

        return dto;
    }
}

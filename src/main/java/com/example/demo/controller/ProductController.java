package com.example.demo.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.dto.ProductDto;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService service;
    private final Cloudinary cloudinary;

    public ProductController(ProductService service, Cloudinary cloudinary) {
        this.service = service;
        this.cloudinary = cloudinary;
    }

    /* =====================================
       PUBLIC – LIST PRODUCTS
    ===================================== */
    @GetMapping
    public ResponseEntity<Page<ProductDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getAll(pageable));
    }

    /* =====================================
       PUBLIC – GET PRODUCT BY ID
    ===================================== */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        ProductDto dto = service.getById(id);
        return dto != null
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
    }

    /* =====================================
       ADMIN – ADD PRODUCT
    ===================================== */
    @PostMapping(
            value = "/admin",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> addProduct(
            @RequestParam String name,
            @RequestParam String category,
            @RequestParam String subcategory,
            @RequestParam Double price,
            @RequestParam Double mrp,
            @RequestParam Integer discount,
            @RequestParam Integer gst,
            @RequestParam String qtyType,
            @RequestParam Integer quantity,
            @RequestParam MultipartFile image) {

        try {
            if (image == null || image.isEmpty()) {
                return error("Image required", HttpStatus.BAD_REQUEST);
            }

            String imageUrl = saveImage(image);

            Product product = new Product();
            product.setName(name);
            product.setCategory(category);
            product.setSubcategory(subcategory);
            product.setPrice(price);
            product.setMrp(mrp);
            product.setDiscount(discount);
            product.setGst(gst);
            product.setQtyType(qtyType);
            product.setQuantity(quantity);
            product.setImageUrl(imageUrl);
            product.setAvailable(true);

            Product saved = service.save(product);

            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("id", saved.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(res);

        } catch (Exception e) {
            return error("Product upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* =====================================
       ADMIN – UPDATE PRODUCT
    ===================================== */
    @PutMapping(
            value = "/admin/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String category,
            @RequestParam String subcategory,
            @RequestParam Double price,
            @RequestParam Double mrp,
            @RequestParam Integer discount,
            @RequestParam Integer gst,
            @RequestParam String qtyType,
            @RequestParam Integer quantity,
            @RequestParam(required = false) MultipartFile image) {

        try {
            Product product = service.findEntityById(id);
            if (product == null) {
                return ResponseEntity.notFound().build();
            }

            product.setName(name);
            product.setCategory(category);
            product.setSubcategory(subcategory);
            product.setPrice(price);
            product.setMrp(mrp);
            product.setDiscount(discount);
            product.setGst(gst);
            product.setQtyType(qtyType);
            product.setQuantity(quantity);

            if (image != null && !image.isEmpty()) {
                String imageUrl = saveImage(image);
                product.setImageUrl(imageUrl);
            }

            service.save(product);
            return ResponseEntity.ok("Product updated successfully");

        } catch (Exception e) {
            return error("Product update failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* =====================================
       ADMIN – DELETE PRODUCT
    ===================================== */
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return ResponseEntity.ok("Product deleted");
        } catch (Exception e) {
            return error("Delete failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* =====================================
       CLOUDINARY IMAGE UPLOAD  ✅
    ===================================== */
    private String saveImage(MultipartFile image) throws Exception {

        Map uploadResult = cloudinary.uploader().upload(
                image.getBytes(),
                ObjectUtils.emptyMap()
        );

        return uploadResult.get("secure_url").toString();
    }

    /* =====================================
       ERROR RESPONSE
    ===================================== */
    private ResponseEntity<Map<String, Object>> error(String msg, HttpStatus status) {
        Map<String, Object> e = new HashMap<>();
        e.put("success", false);
        e.put("error", msg);
        return ResponseEntity.status(status).body(e);
    }
}

package hu.unideb.inf.ordertrackerbackend.api.service;

import hu.unideb.inf.ordertrackerbackend.api.dto.common.BaseResponse;
import hu.unideb.inf.ordertrackerbackend.api.dto.common.ExtendedResponse;
import hu.unideb.inf.ordertrackerbackend.api.dto.product.ChangeProductVisibilityRequest;
import hu.unideb.inf.ordertrackerbackend.api.dto.product.FindProductByIdDto;
import hu.unideb.inf.ordertrackerbackend.api.dto.product.FindProducts;
import hu.unideb.inf.ordertrackerbackend.api.dto.product.ProductsDto;
import hu.unideb.inf.ordertrackerbackend.api.enums.ImageType;
import hu.unideb.inf.ordertrackerbackend.api.model.Image;
import hu.unideb.inf.ordertrackerbackend.api.model.ImageWithBinary;
import hu.unideb.inf.ordertrackerbackend.api.model.Product;
import hu.unideb.inf.ordertrackerbackend.api.repository.ImageWithBinaryRepository;
import hu.unideb.inf.ordertrackerbackend.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ImageWithBinaryRepository imageWithBinaryRepository;

    @Autowired
    ImageService imageService;


    public ResponseEntity<ProductsDto> findAll(FindProducts findProductsRequest, Boolean onlyVisible) {
        List<Product> products;

        if (onlyVisible) {
            products = productRepository.findVisibleProducts();
        } else {
            products = productRepository.findAll();
        }

        ProductsDto productsDto = new ProductsDto();

        ImageType imageType = ImageType.findByName(findProductsRequest.getImageType());
        if (imageType != null) {
            Image image;

            for (Product product : products) {
                image = imageService.findProductImageByType(product.getId(), imageType);
                if (image != null) {
                    product.setImages(new HashSet<>(List.of(imageService.findProductImageByType(product.getId(), imageType))));
                }
            }

        }

        productsDto.setProductList(products);

        return ResponseEntity.ok(productsDto);
    }

    public ResponseEntity<Product> addProduct(Product product) {
        product.setId(null);
        return ResponseEntity.ok(productRepository.save(product));
    }

    public ResponseEntity<Product> saveProduct(Product product) {
        return ResponseEntity.ok(productRepository.save(product));
    }

    @Transactional
    public ResponseEntity<Product> findById(FindProductByIdDto findProductByIdDto) {
        if (findProductByIdDto.isWithBinary()) {
            Product product = productRepository.findByIdWithoutImages(findProductByIdDto.getId());
            product.setImages(imageService.findProductImages(findProductByIdDto.getId()));

            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.ok(productRepository.findById(findProductByIdDto.getId()).get());
        }
    }

    public ResponseEntity<ExtendedResponse> deleteById(String productId) {
        ExtendedResponse response = new ExtendedResponse();

        try {
            productRepository.deleteById(Long.parseLong(productId));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.getErrorMessages().add("Failed to delete by ID.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Transactional
    public ResponseEntity<ExtendedResponse> changeVisibilityById(String productId) {
        ExtendedResponse response = new ExtendedResponse();

        try {
            productRepository.changeVisibilityById(Long.parseLong(productId));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.getErrorMessages().add("Failed to change product visibility by ID.");
            return ResponseEntity.badRequest().body(response);
        }

    }

}

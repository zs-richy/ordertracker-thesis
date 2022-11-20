package hu.unideb.inf.ordertrackerbackend.api.controller;

import hu.unideb.inf.ordertrackerbackend.api.dto.common.ExtendedResponse;
import hu.unideb.inf.ordertrackerbackend.api.dto.product.FindProductByIdDto;
import hu.unideb.inf.ordertrackerbackend.api.dto.product.FindProducts;
import hu.unideb.inf.ordertrackerbackend.api.dto.product.ProductsDto;
import hu.unideb.inf.ordertrackerbackend.api.model.Product;
import hu.unideb.inf.ordertrackerbackend.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product/")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    @Autowired
    ProductService productService;

    private static final String ID = "id";

    @PostMapping("/get-products")
    public ResponseEntity<ProductsDto> getVisibleProducts(@RequestBody FindProducts findProductsRequest) {
        return productService.findAll(findProductsRequest, true);
    }

    @PostMapping("/admin/get-products")
    public ResponseEntity<ProductsDto> getProducts(@RequestBody FindProducts findProductsRequest) {
        return productService.findAll(findProductsRequest, false);
    }

    @PostMapping("/find-product-by-id")
    public ResponseEntity<Product> findProductById(@RequestBody FindProductByIdDto findProductByIdDto) {
        return productService.findById(findProductByIdDto);
    }

    @PostMapping("/admin/add-product")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PostMapping("/admin/save-product")
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @GetMapping("/admin/delete/{id}")
    public ResponseEntity<ExtendedResponse> deleteProductById(@PathVariable(ID) String productId) {
        return productService.deleteById(productId);
    }

    @GetMapping("/admin/change-visibility/{id}")
    public ResponseEntity<ExtendedResponse> changeVisibilityById(@PathVariable(ID) String productId) {
        return productService.changeVisibilityById(productId);
    }

//    @GetMapping("/extended")
//    public ResponseEntity<ExtendedResponse> testExtended() {
//        return productService.testExtended();
//    }

}

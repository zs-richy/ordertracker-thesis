package hu.unideb.inf.ordertrackerbackend.api.controller;

import hu.unideb.inf.ordertrackerbackend.api.dto.image.FindImageDto;
import hu.unideb.inf.ordertrackerbackend.api.dto.image.ImageDto;
import hu.unideb.inf.ordertrackerbackend.api.model.Image;
import hu.unideb.inf.ordertrackerbackend.api.model.ImageWithBinary;
import hu.unideb.inf.ordertrackerbackend.api.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/image/")
public class ImageController {

    @Autowired
    ImageService imageService;

    @PostMapping("/admin/add-image")
    public ResponseEntity addImage(@RequestBody ImageDto image) {
        return imageService.addImage(image);
    }

    @GetMapping("/find-image-by-id")
    public ImageWithBinary findImageById(@RequestBody ImageDto dto) {
        return imageService.getImageById(dto);
    }

    @GetMapping("/find-image-by-product-id")
    public Set<Image> findImageByProductId(@RequestBody FindImageDto findImageDto) {
        return imageService.findImageByProductId(findImageDto);
    }
}

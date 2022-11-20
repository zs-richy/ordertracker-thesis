package hu.unideb.inf.ordertrackerbackend.api.service;

import hu.unideb.inf.ordertrackerbackend.api.dto.image.FindImageDto;
import hu.unideb.inf.ordertrackerbackend.api.dto.image.ImageDto;
import hu.unideb.inf.ordertrackerbackend.api.enums.ImageType;
import hu.unideb.inf.ordertrackerbackend.api.model.Image;
import hu.unideb.inf.ordertrackerbackend.api.model.ImageWithBinary;
import hu.unideb.inf.ordertrackerbackend.api.repository.ImageRepository;
import hu.unideb.inf.ordertrackerbackend.api.repository.ImageWithBinaryRepository;
import hu.unideb.inf.ordertrackerbackend.api.repository.ProductRepository;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ImageWithBinaryRepository imageWithBinaryRepository;
    @Autowired
    ProductRepository productRepository;

    /**
     * Sets the image of the given product with the image in the DTO,
     * also creates a thumbnail from the image for faster load times
     */
    @Transactional
    public ResponseEntity addImage(ImageDto imageDto) {
        try {
            ImageWithBinary imageEntity = imageWithBinaryRepository.findImagesWithBinaryByProductIdAndType(imageDto.getProductId(), ImageType.GENERAL);

            if (imageEntity == null) {
                imageEntity = new ImageWithBinary();
                imageEntity.setType(ImageType.GENERAL);
                imageEntity.setProduct(productRepository.findById(imageDto.getProductId()).get());
            }
            imageEntity.setImageData(Base64.getEncoder().encode(imageDto.getImageData()));

            ImageWithBinary thumbnailEntity = createThumbnailFromImage(imageEntity);

            imageWithBinaryRepository.save(imageEntity);
            imageWithBinaryRepository.save(thumbnailEntity);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    public ImageWithBinary getImageById(ImageDto dto) {
        Optional<ImageWithBinary> image = imageWithBinaryRepository.findById(dto.getId());

        return image.orElse(null);
    }

    @Transactional
    public Set<Image> findImageByProductId(FindImageDto findImageDto) {
        return findProductImages(findImageDto.getId());
    }

    /**
     * Creates a thumbnail entity from the given image binary, scaled to 200px width
     */
    private ImageWithBinary createThumbnailFromImage(ImageWithBinary image) {
        ByteArrayOutputStream compressed = new ByteArrayOutputStream();
        try {
            BufferedImage bufferedImageInput;
            bufferedImageInput = ImageIO.read(new ByteArrayInputStream(Base64.getMimeDecoder().decode(image.getImageData())));
            ImageOutputStream outputStream = ImageIO.createImageOutputStream(compressed);
            bufferedImageInput = Scalr.resize(bufferedImageInput, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, 200, null);
            ImageIO.write(bufferedImageInput, "JPEG", outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] thumbnailBytes = compressed.toByteArray();

        ImageWithBinary thumbnailEntity = imageWithBinaryRepository.findImagesWithBinaryByProductIdAndType(image.getProduct().getId(), ImageType.THUMBNAIL);

        if (thumbnailEntity == null) {
            thumbnailEntity = new ImageWithBinary();
            thumbnailEntity.setType(ImageType.THUMBNAIL);
            thumbnailEntity.setProduct(image.getProduct());
        }
        thumbnailEntity.setImageData(Base64Utils.encode(thumbnailBytes));

        return thumbnailEntity;

    }

    @Transactional
    public Set<Image> findProductImages(Long id) {
        Set<Image> imageSet = new HashSet<Image>();
        List<ImageWithBinary> binaries = imageWithBinaryRepository.findImagesWithBinaryByProductId(id);

        for (ImageWithBinary imageWithBinary : binaries) {
            if (imageWithBinary.getImageData() != null) {
                imageWithBinary.decodeBase64Data();
                imageSet.add(imageWithBinary);
            }
        }

        return imageSet;
    }

    @Transactional
    public Image findProductImageByType(Long id, ImageType imageType) {
        ImageWithBinary imageBinary = imageWithBinaryRepository.findImagesWithBinaryByProductIdAndType(id, imageType);

        if (imageBinary != null && imageBinary.getImageData() != null) {
            imageBinary.decodeBase64Data();
        }


        return imageBinary;
    }
}

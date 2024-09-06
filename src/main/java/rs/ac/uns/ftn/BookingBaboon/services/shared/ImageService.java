package rs.ac.uns.ftn.BookingBaboon.services.shared;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.Image;
import rs.ac.uns.ftn.BookingBaboon.dtos.shared.ImageCreateRequest;
import rs.ac.uns.ftn.BookingBaboon.repositories.shared.IImageRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ImageService implements IImageService{
    private final IImageRepository repository;

    @Value("${imagesFolderPath}") // Specify the image upload path in application.properties
    private String uploadPath;
    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());
    @Override
    public HashSet<Image> getAll() {
        return new HashSet<>(repository.findAll());
    }

    @Override
    public Image get(Long imageId) {
        Optional<Image> found = repository.findById(imageId);
        if (found.isEmpty()) {
            String value = bundle.getString("image.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public Image create(Image image) {
        try {
            this.saveImage(image);
            repository.save(image);
            repository.flush();
            return image;
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> errors = ex.getConstraintViolations();
            StringBuilder sb = new StringBuilder(1000);
            for (ConstraintViolation<?> error : errors) {
                sb.append(error.getMessage()).append("\n");
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Image update(Image image) {
        try {
            get(image.getId()); // this will throw ImageNotFoundException if image is not found
            repository.save(image);
            repository.flush();
            return image;
        } catch (RuntimeException ex) {
            Throwable e = ex;
            Throwable c = null;
            while ((e != null) && !((c = e.getCause()) instanceof ConstraintViolationException) ) {
                e = (RuntimeException) c;
            }
            if ((c != null) && (c instanceof ConstraintViolationException)) {
                ConstraintViolationException c2 = (ConstraintViolationException) c;
                Set<ConstraintViolation<?>> errors = c2.getConstraintViolations();
                StringBuilder sb = new StringBuilder(1000);
                for (ConstraintViolation<?> error : errors) {
                    sb.append(error.getMessage() + "\n");
                }
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, sb.toString());
            }
            throw ex;
        }
    }

    @Override
    public Image remove(Long imageId) throws IOException {
        Image found = get(imageId);
        if (found == null || !deleteFile(found)){
            return null;
        }
        repository.delete(found);
        repository.flush();
        return found;
    }

    @Override
    public void removeAll() {
        repository.deleteAll();
        repository.flush();
    }

    private boolean deleteFile(Image image) throws IOException {
        String filePath = uploadPath + image.getPath() + File.separator + image.getFileName();
        Path path = Paths.get(filePath);
        return Files.deleteIfExists(path);
    }


    @Override
    public byte[] getImageData(Long imageId) throws IOException {
        Image image = get(imageId);
        String filePath = uploadPath + image.getPath() + File.separator + image.getFileName();
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }

    public void saveImage(Image image) throws IOException {
        Path directoryPath = Path.of(uploadPath + image.getPath());
        Files.createDirectories(directoryPath);

        Path filePath = Path.of(uploadPath + image.getPath(), image.getFileName());

        Files.write(filePath, image.getContent());
    }

    public MediaType getMediaType(Long imageId) throws IOException {
        Image image = get(imageId);
        if (StringUtils.hasText(image.getFileName())) {
            int lastDotIndex = image.getFileName().lastIndexOf(".");
            if (lastDotIndex != -1) {
                String fileType = image.getFileName().substring(lastDotIndex + 1);
                switch (fileType){
                    case("jpg"):
                        return MediaType.IMAGE_JPEG;
                    case("png"):
                        return MediaType.IMAGE_PNG;
                    case("gif"):
                        return MediaType.IMAGE_GIF;
                }
            }
        }
        throw new IOException("Invalid image type");
    }
}

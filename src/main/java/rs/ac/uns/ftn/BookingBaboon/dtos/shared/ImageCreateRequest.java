package rs.ac.uns.ftn.BookingBaboon.dtos.shared;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageCreateRequest {
    String path;
    String fileName;
    MultipartFile content;
}

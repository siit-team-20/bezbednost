package rs.ac.uns.ftn.BookingBaboon.dtos.shared;

import lombok.Data;

@Data
public class ImageResponse {
    Long id;
    String path;
    String fileName;
    byte[] content;
}

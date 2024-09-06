package rs.ac.uns.ftn.BookingBaboon.services.shared;

import org.springframework.http.MediaType;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.Image;

import java.io.IOException;
import java.util.Set;

public interface IImageService {
    public Set<Image> getAll();
    public Image get(Long id);

    public Image create(Image image);

    public Image update(Image image);

    public Image remove(Long id) throws IOException;

    public void removeAll();

    byte[] getImageData(Long imageId) throws IOException;

    public MediaType getMediaType(Long imageId) throws IOException;
}

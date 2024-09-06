package rs.ac.uns.ftn.BookingBaboon.domain.shared;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "images")
@TableGenerator(name="image_id_generator", table="primary_keys", pkColumnName="key_pk", pkColumnValue="image", initialValue = 23, valueColumnName="value_pk")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "image_id_generator")
    Long id;

    @NotNull
    String path;

    @NotNull
    String fileName;
    @Transient
    byte[] content;

    public Image(String path, String fileName, byte[] contentBytes) {
        this.path = path;
        this.fileName = fileName;
        this.content = contentBytes;
    }

    public Image(){}
}

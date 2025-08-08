package appbarber.model.base;

import jakarta.persistence.Column;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public class Auditable {


    @CreatedDate
    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;


    @LastModifiedDate
    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;
}

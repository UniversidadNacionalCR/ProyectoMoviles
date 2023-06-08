package unknown.Sistema_Inventario_Android.Entidades;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Venta {
    private String id;
    private String comprador;
    private String productos [];
}

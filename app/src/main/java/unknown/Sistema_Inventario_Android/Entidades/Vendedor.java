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
public class Vendedor {
    private String id;
    private String nombre;
    private String rif;
    private String direccion;
    private String telefono;
    private String email;
}

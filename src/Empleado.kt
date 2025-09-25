// Declara los datos de la persona
data class Empleado(
    val rut: String,
    val nombre: String,
    val sueldoBase: Double,
    val afp: AFP,
    val direccion: Direccion,
    val bonosImponibles: Double = 0.0,
    val bonosNoImponibles: Double = 0.0,
    var sueldoLiquido: Double = 0.0
)
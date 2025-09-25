// Actua como objeto de memoria
object Repositorio {
    val afps: MutableList<AFP> = mutableListOf(
        AFP("Habitat", 0.1127),
        AFP("Capital", 0.1144),
        AFP("Cuprum", 0.1144),
        AFP("Provida", 0.1145),
        AFP("Modelo", 0.1058)
    )

    val empleados: MutableList<Empleado> = mutableListOf()
    val liquidaciones: MutableList<LiquidacionSueldo> = mutableListOf()
}
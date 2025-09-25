// Operaciones para los calculos de tasa
data class LiquidacionSueldo(
    val periodo: String,
    val empleado: Empleado,
    val imponible: Double,
    val noImponible: Double,
    val descAfp: Double,
    val descSalud: Double,
    val descCesantia: Double,
    val totalDescuentos: Double,
    val sueldoLiquido: Double
) {
    companion object {
        private const val TASA_SALUD = 0.07
        private const val TASA_CESANTIA = 0.006

        fun calcular(
            periodo: String,
            empleado: Empleado,
            tasaSalud: Double = TASA_SALUD,
            tasaCesantia: Double = TASA_CESANTIA
        ): LiquidacionSueldo {
            val imponible = empleado.sueldoBase + empleado.bonosImponibles
            val noImponible = empleado.bonosNoImponibles
            val descAfp = imponible * empleado.afp.tasa
            val descSalud = imponible * tasaSalud
            val descCesantia = imponible * tasaCesantia
            val totalDescuentos = descAfp + descSalud + descCesantia
            val sueldoLiquido = imponible + noImponible - totalDescuentos

            empleado.sueldoLiquido = sueldoLiquido // Update employee's liquid salary

            return LiquidacionSueldo(
                periodo = periodo,
                empleado = empleado,
                imponible = imponible,
                noImponible = noImponible,
                descAfp = descAfp,
                descSalud = descSalud,
                descCesantia = descCesantia,
                totalDescuentos = totalDescuentos,
                sueldoLiquido = sueldoLiquido
            )
        }
    }
}
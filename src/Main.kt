// Felipe Gonzalez y Christian Soto
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main() {
    menu()
}

fun menu() {
    // se cargan los empleados y las liquidaciones de prueba
    cargarDatosDePrueba()
    do {
        println("\n--- Menú de Gestión de Nómina ---")
        println("1. Lista de empleados")
        println("2. Agregar empleado")
        println("3. Generar liquidación por RUT")
        println("4. Lista de liquidaciones")
        println("5. Filtrar empleados por AFP y ordenar por sueldo líquido")
        println("6. Eliminar empleado")
        println("7. Salir")
        print("Seleccione una opción: ")

        // se lee la opción ingresada y se ejecuta la acción correspondiente
        when (readlnOrNull()?.toIntOrNull()) {
            1 -> listarEmpleados()
            2 -> agregarEmpleado()
            3 -> generarLiquidacion()
            4 -> listarLiquidaciones()
            5 -> filtrarYOrdenarPorAfp()
            6 -> cargarDatosDePrueba()
            7 -> eliminarEmpleado()
            8 -> return
            else -> println("Opción no válida. Intente nuevamente.")
        }
    } while (true)
}

fun listarEmpleados() {
    // aquí se muestra todos los empleados registrados
    if (Repositorio.empleados.isEmpty()) {
        println("No existen empleados registrados.")
        return
    }
    println("\n--- Lista de Empleados ---")
    Repositorio.empleados.forEach { empleado ->
        println("RUT: ${empleado.rut}, Nombre: ${empleado.nombre}, Sueldo Base: ${empleado.sueldoBase}")
    }
}

fun agregarEmpleado() {
    // aquí se permite ingresar un nuevo empleado manualmente
    println("\n--- Agregar Nuevo Empleado ---")
    print("Ingrese el RUT: ")
    val rut = readln()
    print("Ingrese el Nombre: ")
    val nombre = readln()
    print("Ingrese el Sueldo Base: ")
    val sueldoBase = readln().toDoubleOrNull() ?: 0.0

    println("Seleccione AFP:")
    Repositorio.afps.forEachIndexed { index, afp ->
        println("${index + 1}. ${afp.nombre} (${afp.tasa * 100}%)")
    }
    print("Opción: ")
    val afpIndex = readln().toIntOrNull()?.minus(1)
    val afp = if (afpIndex != null && afpIndex in Repositorio.afps.indices) {
        Repositorio.afps[afpIndex]
    } else {
        println("Selección de AFP no válida. Se asignará la primera de la lista.")
        Repositorio.afps[0]
    }

    //se ingresa dirección del usuario
    print("Ingrese la dirección: ")
    val calle = readln()
    print("Ingrese número de la dirección: ")
    val numero = readln().toIntOrNull() ?: 0
    print("Ingrese ciudad: ")
    val ciudad = readln()
    print("Ingrese región: ")
    val region = readln()

    val direccion = Direccion(calle, numero, ciudad, region)
    val nuevoEmpleado = Empleado(rut, nombre, sueldoBase, afp, direccion)
    Repositorio.empleados.add(nuevoEmpleado)
    println("Empleado agregado exitosamente.")
}

fun generarLiquidacion() {
    // se genera una liquidación de sueldo a partir del ruyt del empleado
    println("\n--- Generar Liquidación de Sueldo ---")
    print("Ingrese el RUT del empleado: ")
    val rut = readln()
    val empleado = Repositorio.empleados.find { it.rut == rut }

    if (empleado == null) {
        println("No se encontró el empleado.")
        return
    }

    val periodo = LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY-MM"))
    val liquidacion = LiquidacionSueldo.calcular(periodo, empleado)
    Repositorio.liquidaciones.add(liquidacion)

    println("Liquidación generada con éxito para ${empleado.nombre}.")
    println("Sueldo Líquido: ${liquidacion.sueldoLiquido}")
}

fun listarLiquidaciones() {
    // se lista todas las liquidaciones generadas
    if (Repositorio.liquidaciones.isEmpty()) {
        println("No hay liquidaciones generadas.")
        return
    }
    println("\n--- Lista de Liquidaciones ---")
    Repositorio.liquidaciones.forEach { liquidacion ->
        println("Periodo: ${liquidacion.periodo}, Empleado: ${liquidacion.empleado.nombre}, Sueldo Líquido: ${liquidacion.sueldoLiquido}")
    }
    // Se calcula el total de descuentos de todas las liquidaciones
    val totalDescuentosNomina = Repositorio.liquidaciones.sumOf { it.totalDescuentos }
    println("\nTotal de descuentos de la nómina: $totalDescuentosNomina")
}

fun filtrarYOrdenarPorAfp() {
    // filtra empleados por AFP seleccionada y los ordena por sueldo líquido
    println("\n--- Filtrar Empleados por AFP ---")
    println("Seleccione AFP para filtrar:")
    Repositorio.afps.forEachIndexed { index, afp ->
        println("${index + 1}. ${afp.nombre}")
    }
    print("Opción: ")
    val afpIndex = readln().toIntOrNull()?.minus(1)
    if (afpIndex == null || afpIndex !in Repositorio.afps.indices) {
        println("Opción no válida.")
        return
    }
    val afpSeleccionada = Repositorio.afps[afpIndex]

    // Se filtran empleados de la AFP elegida y se ordenan de mayor a menor sueldo líquido

    val empleadosFiltrados = Repositorio.empleados.filter { it.afp == afpSeleccionada }.sortedByDescending { it.sueldoLiquido }

    if (empleadosFiltrados.isEmpty()) {
        println("No hay empleados en la AFP ${afpSeleccionada.nombre}.")
        return
    }

    println("\nEmpleados de ${afpSeleccionada.nombre} (ordenados por sueldo líquido de mayor a menor):")
    empleadosFiltrados.forEach { empleado ->
        println("Nombre: ${empleado.nombre}, Sueldo Líquido: ${empleado.sueldoLiquido}")
    }
}



fun eliminarEmpleado() {
    println("\n--- Eliminar Empleado ---")
    print("Ingrese el RUT del empleado a eliminar: ")
    val rut = readln()
    val empleadoAEliminar = Repositorio.empleados.find { it.rut == rut }

    if (empleadoAEliminar == null) {
        println("No se encontró el empleado.")
        return
    }

    Repositorio.empleados.remove(empleadoAEliminar)
    println("Empleado eliminado exitosamente.")
}
fun cargarDatosDePrueba() {
    // carga un set de empleados y liquidaciones predefinidas para pruebas
    println("Cargando datos de prueba...")

    val afpModelo = Repositorio.afps.find { it.nombre == "Modelo" }!!
    val afpHabitat = Repositorio.afps.find { it.nombre == "Habitat" }!!

    // Se crean empleados de prueba
    val emp1 = Empleado(
        rut = "21.962.832-3", nombre = "Felipe Tirado", sueldoBase = 800000.0, afp = afpModelo,
        direccion = Direccion("13 norte", 3675, "Talca", "Maule")
    )
    val emp2 = Empleado(
        rut = "11.101.010-K", nombre = "Lionel Scaloni", sueldoBase = 2500000.0, afp = afpHabitat,
        direccion = Direccion("Mejor del mundo", 10, "Talca", "Maule"),
        bonosImponibles = 500000.0
    )
    val emp3 = Empleado(
        rut = "22.014.246-6", nombre = "Pupi Galarce", sueldoBase = 5000000.0, afp = afpModelo,
        direccion = Direccion("2 Norte", 1, "Talca", "Maule"),
        bonosNoImponibles = 1000000.0
    )

    Repositorio.empleados.clear()
    Repositorio.empleados.addAll(listOf(emp1, emp2, emp3))


    val periodoActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))

    Repositorio.liquidaciones.clear()
    Repositorio.liquidaciones.add(LiquidacionSueldo.calcular(periodoActual, emp1))
    Repositorio.liquidaciones.add(LiquidacionSueldo.calcular(periodoActual, emp2))
    Repositorio.liquidaciones.add(LiquidacionSueldo.calcular(periodoActual, emp3))


    println(" ¡Datos cargados exitosamente!")
    println("   - Empleados en el repositorio: ${Repositorio.empleados.size}")
    println("   - Liquidaciones generadas: ${Repositorio.liquidaciones.size}")
}
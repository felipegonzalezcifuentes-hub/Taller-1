import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main() {
    menu()
}

fun menu() {
    do {
        println("\n--- Menú de Gestión de Nómina ---")
        println("1. Listar empleados")
        println("2. Agregar empleado")
        println("3. Generar liquidación por RUT")
        println("4. Listar liquidaciones")
        println("5. Filtrar empleados por AFP y ordenar por sueldo líquido")
        println("6. Eliminar empleado")
        println("7. Salir")
        print("Seleccione una opción: ")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> listarEmpleados()
            2 -> agregarEmpleado()
            3 -> generarLiquidacion()
            4 -> listarLiquidaciones()
            5 -> filtrarYOrdenarPorAfp()
            6 -> eliminarEmpleado()
            7 -> return
            else -> println("Opción no válida. Intente de nuevo.")
        }
    } while (true)
}

fun listarEmpleados() {
    if (Repositorio.empleados.isEmpty()) {
        println("No hay empleados registrados.")
        return
    }
    println("\n--- Lista de Empleados ---")
    Repositorio.empleados.forEach { empleado ->
        println("RUT: ${empleado.rut}, Nombre: ${empleado.nombre}, Sueldo Base: ${empleado.sueldoBase}")
    }
}

fun agregarEmpleado() {
    println("\n--- Agregar Nuevo Empleado ---")
    print("Ingrese RUT: ")
    val rut = readln()
    print("Ingrese Nombre: ")
    val nombre = readln()
    print("Ingrese Sueldo Base: ")
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
    println("\n--- Generar Liquidación de Sueldo ---")
    print("Ingrese el RUT del empleado: ")
    val rut = readln()
    val empleado = Repositorio.empleados.find { it.rut == rut }

    if (empleado == null) {
        println("Empleado no encontrado.")
        return
    }

    val periodo = LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY-MM"))
    val liquidacion = LiquidacionSueldo.calcular(periodo, empleado)
    Repositorio.liquidaciones.add(liquidacion)

    println("Liquidación generada con éxito para ${empleado.nombre}.")
    println("Sueldo Líquido: ${liquidacion.sueldoLiquido}")
}

fun listarLiquidaciones() {
    if (Repositorio.liquidaciones.isEmpty()) {
        println("No hay liquidaciones generadas.")
        return
    }
    println("\n--- Lista de Liquidaciones ---")
    Repositorio.liquidaciones.forEach { liquidacion ->
        println("Periodo: ${liquidacion.periodo}, Empleado: ${liquidacion.empleado.nombre}, Sueldo Líquido: ${liquidacion.sueldoLiquido}")
    }
    val totalDescuentosNomina = Repositorio.liquidaciones.sumOf { it.totalDescuentos }
    println("\nTotal de descuentos de la nómina: $totalDescuentosNomina")
}

fun filtrarYOrdenarPorAfp() {
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
        println("Empleado no encontrado.")
        return
    }

    Repositorio.empleados.remove(empleadoAEliminar)
    println("Empleado eliminado exitosamente.")
}
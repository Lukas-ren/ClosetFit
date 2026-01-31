package com.example.closetfit.viewmodel

import com.example.closetfit.model.Carrito
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldContain

class CarritoViewmodelTest : FunSpec({

    lateinit var viewModel: CarritoViewmodel

    beforeTest {
        viewModel = CarritoViewmodel()
    }

    test("agregarProducto debe añadir un producto nuevo al carrito") {
        val producto = Carrito(id = 1, nombre = "Camiseta", precio = 100, cantidad = 1, imagen = "")
        
        viewModel.agregarProducto(producto)
        
        viewModel.carrito.value shouldHaveSize 1
        viewModel.carrito.value shouldContain producto
    }

    test("agregarProducto debe incrementar la cantidad si el producto ya existe") {
        val producto = Carrito(id = 1, nombre = "Camiseta", precio = 100, cantidad = 1, imagen = "")
        
        viewModel.agregarProducto(producto)
        viewModel.agregarProducto(producto)
        
        viewModel.carrito.value shouldHaveSize 1
        viewModel.carrito.value.first().cantidad shouldBe 2
    }

    test("quitarProducto debe eliminar el producto por ID") {
        val producto = Carrito(id = 1, nombre = "Camiseta", precio = 100, cantidad = 1, imagen = "")
        viewModel.agregarProducto(producto)
        
        viewModel.quitarProducto(1)
        
        viewModel.carrito.value shouldHaveSize 0
    }

    test("total debe calcular la suma correcta de precios por cantidad") {
        val p1 = Carrito(id = 1, nombre = "A", precio = 100, cantidad = 2, imagen = "")
        val p2 = Carrito(id = 2, nombre = "B", precio = 50, cantidad = 1, imagen = "")
        
        viewModel.agregarProducto(p1)
        viewModel.agregarProducto(p2)
        
        // El total es un StateFlow, en este caso unitario podemos acceder al value
        // Nota: total se calcula con map sobre carrito
        viewModel.total.value shouldBe 250
    }

    test("vaciarCarrito debe dejar la lista limpia") {
        viewModel.agregarProducto(Carrito(id = 1, nombre = "A", precio = 10, cantidad = 1, imagen = ""))
        viewModel.vaciarCarrito()
        viewModel.carrito.value shouldHaveSize 0
    }
})
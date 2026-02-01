package com.example.closetfit.viewmodel

import android.app.Application
import com.example.closetfit.model.Usuario
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioViewModelTest : FunSpec({

    val testDispatcher = UnconfinedTestDispatcher()
    val application = mockk<Application>(relaxed = true)
    val dao = mockk<DAOUsuario>(relaxed = true)
    lateinit var viewModel: UsuarioViewModel

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    beforeTest {
        viewModel = UsuarioViewModel(application)
        viewModel.usuarioDAO = dao // Inyectamos el mock
    }

    test("registrar debe guardar usuario si contraseñas coinciden") {
        val nombre = "Juan"
        val pass = "123"
        
        viewModel.registrar(nombre, "juan@mail.com", pass, pass, "1-1", "Calle 1")
        
        coVerify { dao.insert(any()) }
        viewModel.mensaje.value shouldBe "Usuario registrado correctamente"
    }

    test("registrar debe fallar si las contraseñas no coinciden") {
        viewModel.registrar("Juan", "j@m.com", "123", "456", "1", "C")
        
        coVerify(exactly = 0) { dao.insert(any()) }
        viewModel.mensaje.value shouldBe "Las contraseñas no coinciden"
    }

    test("login debe ser exitoso con credenciales correctas") {
        val user = Usuario(nombre = "user1", email = "u@m.com", password = "123", run = "1", direccion = "D")
        coEvery { dao.login("user1", "123") } returns user
        
        viewModel.login("user1", "123")
        
        viewModel.currentUser.value shouldBe user
        viewModel.mensaje.value shouldBe "Login exitoso"
    }

    test("login debe fallar con credenciales incorrectas") {
        coEvery { dao.login(any(), any()) } returns null
        
        viewModel.login("wrong", "pass")
        
        viewModel.currentUser.value shouldBe null
        viewModel.mensaje.value shouldBe "Usuario o contraseña incorrectos"
    }

    test("login de administrador debe funcionar directamente") {
        viewModel.login("adminadmin", "admin123")
        
        viewModel.mensaje.value shouldBe "Login exitoso"
        viewModel.currentUser.value?.nombre shouldBe "adminadmin"
    }
})
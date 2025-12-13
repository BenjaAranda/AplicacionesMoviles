package com.example.app_prueba.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.app_prueba.data.model.User
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Base de datos en memoria (se borra al terminar el test)
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    // --- PRUEBA 1: Guardado y Lectura Básica ---
    @Test
    fun writeUserAndReadInList() = runBlocking {
        // GIVEN: Un usuario nuevo
        val user = User(
            email = "dreik@test.com",
            pass = "123456",
            name = "Dreik",
            hasDuocDiscount = false
        )

        // WHEN: Lo guardamos
        userDao.insert(user)

        // THEN: Deberíamos poder recuperarlo por su email
        val byEmail = userDao.getUserByEmail("dreik@test.com")

        assertNotNull("El usuario debería existir en la BD", byEmail)
        assertEquals("El nombre debe coincidir", "Dreik", byEmail?.name)
    }

    // --- PRUEBA 2: Estrategia REPLACE (La corrección que hiciste) ---
    @Test
    fun whenInsertingDuplicateEmail_shouldReplaceUser() = runBlocking {
        // GIVEN: Un usuario insertado originalmente
        val user1 = User(
            email = "mismo@correo.com",
            pass = "antigua123",
            name = "Nombre Viejo", // <-- Fíjate en el nombre
            hasDuocDiscount = false
        )
        userDao.insert(user1)

        // WHEN: Insertamos OTRO usuario con el MISMO correo
        val user2 = User(
            email = "mismo@correo.com", // Mismo correo
            pass = "nueva123",
            name = "Nombre Nuevo",     // <-- Nombre diferente
            hasDuocDiscount = true
        )

        // Al usar REPLACE, esto no debería fallar, sino sobrescribir
        userDao.insert(user2)

        // THEN: Al buscar, deberíamos tener los datos del SEGUNDO usuario
        val savedUser = userDao.getUserByEmail("mismo@correo.com")

        assertNotNull(savedUser)
        assertEquals("El nombre debería haberse actualizado", "Nombre Nuevo", savedUser?.name)
        assertEquals("La contraseña debería haberse actualizado", "nueva123", savedUser?.pass)
    }

    // --- PRUEBA 3: Usuario No Encontrado ---
    @Test
    fun whenSearchingNonExistentEmail_shouldReturnNull() = runBlocking {
        // WHEN: Buscamos un correo que nadie ha guardado
        val result = userDao.getUserByEmail("fantasma@gmail.com")

        // THEN: Debe ser null
        assertNull("No debería encontrar nada", result)
    }
}
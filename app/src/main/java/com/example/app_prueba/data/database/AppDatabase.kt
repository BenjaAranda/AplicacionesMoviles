package com.example.app_prueba.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.app_prueba.data.model.CartItem
import com.example.app_prueba.data.model.Product
import com.example.app_prueba.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Product::class, User::class, CartItem::class], version = 4, exportSchema = false) // Subí la versión a 4 por seguridad
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "level_up_gamer_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.productDao())
                }
            }
        }

        suspend fun populateDatabase(productDao: ProductDao) {
            // Actualizado para coincidir con el nuevo modelo Product.kt (Backend compatible)
            // id = 0 le dice a Room que autogenere el ID localmente.
            val products = listOf(
                Product(0, "Catan", "Un clásico juego de estrategia donde los jugadores compiten por colonizar y expandirse en la isla de Catan.", 29990.0, 50, "Juegos de Mesa", null, "JM001"),
                Product(0, "Carcassonne", "Un juego de colocación de fichas donde los jugadores construyen el paisaje alrededor de la fortaleza medieval de Carcassonne.", 24990.0, 50, "Juegos de Mesa", null, "JM002"),
                Product(0, "Controlador Inalámbrico Xbox Series X", "Ofrece una experiencia de juego cómoda con botones mapeables y una respuesta táctil mejorada.", 59990.0, 50, "Accesorios", null, "AC001"),
                Product(0, "Auriculares Gamer HyperX Cloud II", "Proporcionan un sonido envolvente de calidad con un micrófono desmontable y almohadillas de espuma viscoelástica.", 79990.0, 50, "Accesorios", null, "AC002"),
                Product(0, "PlayStation 5", "La consola de última generación de Sony, con gráficos impresionantes y tiempos de carga ultrarrápidos.", 549990.0, 20, "Consolas", null, "CO001"),
                Product(0, "PC Gamer ASUS ROG Strix", "Un potente equipo diseñado para los gamers más exigentes, equipado con los últimos componentes.", 1299990.0, 10, "Computadores Gamers", null, "CG001"),
                Product(0, "Silla Gamer Secretlab Titan", "Diseñada para el máximo confort, esta silla ofrece un soporte ergonómico y personalización ajustable.", 349990.0, 15, "Sillas Gamers", null, "SG001"),
                Product(0, "Mouse Gamer Logitech G502 HERO", "Con sensor de alta precisión y botones personalizables, ideal para gamers que buscan un control preciso.", 49990.0, 50, "Mouse", null, "MS001"),
                Product(0, "Mousepad Razer Goliathus Extended Chroma", "Ofrece un área de juego amplia con iluminación RGB personalizable, asegurando una superficie suave y uniforme.", 29990.0, 50, "Mousepad", null, "MP001"),
                Product(0, "Polera Gamer Personalizada 'Level-Up'", "Una camiseta cómoda y estilizada, con la posibilidad de personalizarla con tu gamer tag o diseño favorito.", 14990.0, 100, "Poleras Personalizadas", null, "PP001")
            )
            productDao.insertAll(products)
        }
    }
}
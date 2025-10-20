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

@Database(entities = [Product::class, User::class, CartItem::class], version = 3, exportSchema = false)
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
            val products = listOf(
                Product("JM001", "Juegos de Mesa", "Catan", 29990.0, "Un clásico juego de estrategia donde los jugadores compiten por colonizar y expandirse en la isla de Catan."),
                Product("JM002", "Juegos de Mesa", "Carcassonne", 24990.0, "Un juego de colocación de fichas donde los jugadores construyen el paisaje alrededor de la fortaleza medieval de Carcassonne."),
                Product("AC001", "Accesorios", "Controlador Inalámbrico Xbox Series X", 59990.0, "Ofrece una experiencia de juego cómoda con botones mapeables y una respuesta táctil mejorada."),
                Product("AC002", "Accesorios", "Auriculares Gamer HyperX Cloud II", 79990.0, "Proporcionan un sonido envolvente de calidad con un micrófono desmontable y almohadillas de espuma viscoelástica."),
                Product("CO001", "Consolas", "PlayStation 5", 549990.0, "La consola de última generación de Sony, con gráficos impresionantes y tiempos de carga ultrarrápidos."),
                Product("CG001", "Computadores Gamers", "PC Gamer ASUS ROG Strix", 1299990.0, "Un potente equipo diseñado para los gamers más exigentes, equipado con los últimos componentes."),
                Product("SG001", "Sillas Gamers", "Silla Gamer Secretlab Titan", 349990.0, "Diseñada para el máximo confort, esta silla ofrece un soporte ergonómico y personalización ajustable."),
                Product("MS001", "Mouse", "Mouse Gamer Logitech G502 HERO", 49990.0, "Con sensor de alta precisión y botones personalizables, ideal para gamers que buscan un control preciso."),
                Product("MP001", "Mousepad", "Mousepad Razer Goliathus Extended Chroma", 29990.0, "Ofrece un área de juego amplia con iluminación RGB personalizable, asegurando una superficie suave y uniforme."),
                Product("PP001", "Poleras Personalizadas", "Polera Gamer Personalizada 'Level-Up'", 14990.0, "Una camiseta cómoda y estilizada, con la posibilidad de personalizarla con tu gamer tag o diseño favorito.")
            )
            productDao.insertAll(products)
        }
    }
}
**level-up gamer**
level-up gamer es una aplicación de Android desarrollada en el lenguaje de programación Kotlin. El proyecto sirve como una plantilla o ejemplo base que implementa funcionalidades esenciales en una aplicación moderna(al estilo app gamer), incluyendo un sistema de autenticación de usuarios (registro e inicio de sesión), compras,  navegación entre pantallas e otros

Funcionalidades Implementadas

Registro de Usuarios:
Formulario para crear una nueva cuenta con correo electrónico y contraseña.
Validación de que las contraseñas coincidan.
Campo condicional para ingresar un código de referido.
Requisito de confirmación de mayoría de edad (+18 años) para poder registrarse.
Manejo y visualización de errores durante el proceso de registro.
Redirección automática a la pantalla principal (Home) tras un registro exitoso.

Inicio de Sesión (Login):
Pantalla para que los usuarios existentes inicien sesión (inferido por la navegación navController.popBackStack() y Routes.Login.route).
Opción para navegar a la pantalla de registro si el usuario no tiene una cuenta.

Navegación:
Implementación de un grafo de navegación utilizando NavHost y NavController de Jetpack Navigation.
Flujo de navegación definido entre las pantallas de Login, Registro y Home.
Limpieza de la pila de navegación (backstack) después de un registro o inicio de sesión exitoso para evitar que el usuario regrese a las pantallas de autenticación con el botón "atrás".

UI Moderna con Material Design 3:
La interfaz de usuario está construida completamente con Jetpack Compose.
Uso de componentes de Material Design 3 (OutlinedTextField, Button, Checkbox, TextButton).
Estilo personalizado para componentes, como colores de borde y de cursor en los campos de texto, para alinearse con el tema de la aplicación.

Pasos para Ejecutar el Proyecto
Clonar el Repositorio:
git clone <URL_DEL_REPOSITORIO>

Abrir en Android Studio:
Abre Android Studio
Selecciona "Open" y navega hasta la carpeta raíz del proyecto clonado.

Sincronizar Gradle:
Android Studio debería iniciar automáticamente el proceso de sincronización de Gradle. Si no lo hace, puedes iniciarlo manualmente desde File > Sync Project with Gradle Files.
Este paso descargará todas las dependencias necesarias definidas en los archivos build.gradle.kts.

Configurar un Emulador o Dispositivo Físico:
Asegúrate de tener un dispositivo virtual (AVD) configurado en el Device Manager de Android Studio.
Alternativamente, puedes conectar un dispositivo Android físico con la depuración USB habilitada.

Ejecutar la Aplicación:
Selecciona el dispositivo (emulador o físico) en el menú desplegable de la barra de herramientas.
Haz clic en el botón "Run 'app'
La aplicación se compilará, instalará y se iniciará en el dispositivo seleccionado.

👥 Integrantes
Joaquin Robles
Benjamin Aranda
Martin Tobar

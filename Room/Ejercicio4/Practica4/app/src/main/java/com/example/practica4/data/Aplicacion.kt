package com.example.practica4.data

import android.app.Application
import androidx.room.Room
import com.example.practica4.Entity.NoticiaEntity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Aplicacion : Application() {
    companion object {
        lateinit var baseDeDatos: BaseDeDatos
    }

    override fun onCreate() {
        super.onCreate()

        baseDeDatos = Room.databaseBuilder(
            this,
            BaseDeDatos::class.java,
            "BaseDeDatos"
        )
            .fallbackToDestructiveMigration()
            .build()

        agregarNoticias()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun agregarNoticias() {
        // Se usa GlobalScope porque esta operación de inserción inicial de datos
        // no está ligada a ningún componente específico de la UI y debe ejecutarse
        // independientemente del ciclo de vida de una Activity.
        // Usar lifecycleScope aquí podría hacer que la operación se cancele si
        // la Activity se destruye antes de completarse.
        GlobalScope.launch(Dispatchers.IO) {
            val noticiasObtenidas = baseDeDatos
                .noticiaDao()
                .obtenerTodasLasNoticias()
            if (noticiasObtenidas.size <= 0) {
                listOf(
                    NoticiaEntity(
                        titulo = "OXO Museo del Videojuego, el ocio electrónico encuentra su sitio en la oferta cultural de Madrid.",
                        descripcion = "OXO Museo del Videojuego inaugura en Madrid.",
                        fecha = "2024-12-04",
                        esFavorita = false,
                        imagenUrl = "https://img2.rtve.es/i/?w=1600&i=01733207829945.jpg",
                        noticiaUrl = "https://www.rtve.es/noticias/20241203/oxo-museo-del-videojuego-madrid/16355153.shtml"
                    ),
                    NoticiaEntity(
                        titulo = "Un equipo de astrónomos descubre que Venus nunca ha sido habitable.",
                        descripcion = "Con esta averiguación, los expertos dejan atrás las especulaciones de que Venus y la Tierra eran similares.",
                        fecha = "2024-12-03",
                        esFavorita = false,
                        imagenUrl = "https://img2.rtve.es/i/?w=1600&i=01733224158774.jpg",
                        noticiaUrl = "https://www.rtve.es/noticias/20241203/astronomos-descubren-venus-no-habitable/16356982.shtml"
                    ),
                    NoticiaEntity(
                        titulo = "Ucrania, ante el invierno más duro de la guerra: Estamos sin electricidad 12 horas al día.",
                        descripcion = "El 70% de las infraestructuras energéticas están dañadas por los continuos ataques rusos.",
                        fecha = "2024-12-04",
                        esFavorita = false,
                        imagenUrl = "https://img2.rtve.es/i/?w=1600&i=01733265482457.jpg",
                        noticiaUrl = "https://www.rtve.es/noticias/20241204/ucrania-ante-invierno-mas-duro-guerra-estamos-sin-electricidad-12-horas-dia/16358019.shtml"
                    ),
                    NoticiaEntity(
                        titulo = "Almeida apoya a Ayuso: No es exagerado decir que hay una maquinaria del Estado que busca su perjuicio.",
                        descripcion = "Cree que la actuación del fiscal general del Estado no se justifica de ninguna de las maneras.",
                        fecha = "2024-12-04",
                        esFavorita = false,
                        imagenUrl = "https://img2.rtve.es/a/16358351/square/?h=320",
                        noticiaUrl = "https://www.rtve.es/noticias/20241204/almeida-apoya-ayuso-hay-maquinaria-estado-busca-perjuicio-politico-personal/16358350.shtml"
                    ),
                    NoticiaEntity(
                        titulo = "Ford Almussafes reduce los turnos en su área de motores para 2025 tras un año de caída productiva.",
                        descripcion = "Desde el próximo 2 de enero, operará en un solo turno de mañana y otro mini turno de noche voluntario de domingo a jueves.",
                        fecha = "2024-12-04",
                        esFavorita = false,
                        imagenUrl = "https://img2.rtve.es/i/?w=1600&i=01733326736523.jpg",
                        noticiaUrl = "https://www.rtve.es/noticias/20241204/ford-almussafes-reduce-turnos-fabrica-motores-2025-caida-productiva/16359282.shtml"
                    ),
                    NoticiaEntity(
                        titulo = "Las fuerzas israelíes avanzan en Jan Yunis y matan al menos a 20 personas en esa zona de Gaza.",
                        descripcion = "Una delegación israelí visitará El Cairo para abordar un posible acuerdo de tregua en Gaza.",
                        fecha = "2024-12-04",
                        esFavorita = false,
                        imagenUrl = "https://img2.rtve.es/i/?w=1600&i=01733315459874.jpg",
                        noticiaUrl = "https://www.rtve.es/noticias/20241204/gaza-israel-palestina-libano-oriente-proximo/16358653.shtml"
                    ),
                    NoticiaEntity(
                        titulo = "Cuba sufre un nuevo apagón general por problemas en su red eléctrica.",
                        descripcion = "El octubre se registraron tres apagones, y en noviembre el huracán Rafael dañó de nuevo la red.",
                        fecha = "2024-12-04",
                        esFavorita = false,
                        imagenUrl = "https://img2.rtve.es/i/?w=1600&i=01733313619485.jpg",
                        noticiaUrl = "https://www.rtve.es/noticias/20241204/cuba-apagones-general-red-electrica/16358762.shtml"
                    ),
                    NoticiaEntity(
                        titulo = "Lotería Navidad 2024: los números más 'raros', curiosos y buscados del sorteo.",
                        descripcion = "La terminación 29, día de la riada que asoló Valencia, está siendo una de las más demandadas.",
                        fecha = "2024-12-04",
                        esFavorita = false,
                        imagenUrl = "https://img2.rtve.es/i/?w=1600&i=01732726362730.jpg",
                        noticiaUrl = "https://www.rtve.es/rtve/20241130/loteria-navidad-2024-numeros-raros-curiosos-buscados-sorteo/16341403.shtml"
                    ),
                    NoticiaEntity(
                        titulo = "Carlos Sainz, a un mes del asalto de su quinto Dakar en su estreno con Ford.",
                        descripcion = "Un mes para que Carlos Sáinz intente asaltar el que sería su quinto Dakar.",
                        fecha = "2024-12-04",
                        esFavorita = false,
                        imagenUrl = "https://img2.rtve.es/v/16359152/vertical/?h=400",
                        noticiaUrl = "https://www.rtve.es/play/videos/telediario-1/carlos-sainz-asalto-quinto-dakar-estreno-ford/16359152/"
                    ),
                    NoticiaEntity(
                        titulo = "Día Internacional de las Personas con Discapacidad.",
                        descripcion = "Hoy martes 3 de diciembre se celebra el Día Internacional de las Personas con Discapacidad.",
                        fecha = "2024-12-03",
                        esFavorita = false,
                        imagenUrl = "https://img2.rtve.es/a/16357913/square/?h=320",
                        noticiaUrl = "https://www.rtve.es/play/audios/24-horas/24-horas-dia-internacional-personas-discapacidad-hay-llevar-terreno-dice-articulo-49/16357913/"
                    )
                ).forEach { noticia ->
                    baseDeDatos
                        .noticiaDao()
                        .agregarNoticia(noticia)
                }
            }
        }
    }
}

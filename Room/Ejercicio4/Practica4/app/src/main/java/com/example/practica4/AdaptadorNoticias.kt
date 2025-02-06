package com.example.practica4

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practica4.Entity.NoticiaEntity
import com.example.practica4.databinding.ItemNewBinding

class AdaptadorNoticias(
    private var listaNoticias: MutableList<NoticiaEntity>,
    private var listener: OnClickListener
) : RecyclerView.Adapter<AdaptadorNoticias.ViewHolder>() {

    private lateinit var contexto: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemNewBinding.bind(view)

        fun establecerListener(noticia: NoticiaEntity) {
            with(binding) {
                root.setOnClickListener { listener.alHacerClic(noticia) }

                btnLike.setOnClickListener { listener.alDarleAFavorito(noticia) }

                root.setOnLongClickListener {
                    listener.alEliminar(noticia)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contexto = parent.context
        val vista = LayoutInflater.from(contexto).inflate(R.layout.item_new, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount(): Int = listaNoticias.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noticia = listaNoticias[position]

        with(holder) {
            establecerListener(noticia)

            with(binding) {
                tvTitle.text = noticia.titulo
                tvDescription.text = noticia.descripcion
                tvDate.text = noticia.fecha

                val icono = if (noticia.esFavorita) R.drawable.ic_like else R.drawable.ic_unlike
                btnLike.setImageResource(icono)
            }
        }
    }

    fun agregar(noticia: NoticiaEntity) {
        listaNoticias.add(noticia)
        notifyDataSetChanged()
    }

    fun establecerNoticias(noticias: MutableList<NoticiaEntity>) {
        this.listaNoticias = noticias
        notifyDataSetChanged()
    }

    fun actualizar(noticia: NoticiaEntity) {
        val indice = listaNoticias.indexOfFirst { it.id == noticia.id }

        if (indice != -1) {
            listaNoticias[indice] = noticia
            notifyItemChanged(indice)
        }
    }

    fun eliminar(noticia: NoticiaEntity) {
        val indice = listaNoticias.indexOf(noticia)

        if (indice != -1) {
            listaNoticias.removeAt(indice)
            notifyItemRemoved(indice)
        }
    }
}

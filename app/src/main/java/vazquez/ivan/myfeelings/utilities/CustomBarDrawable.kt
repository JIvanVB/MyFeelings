package vazquez.ivan.myfeelings.utilities

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import vazquez.ivan.myfeelings.R

class CustomBarDrawable(
    var coordenadas: RectF? = null,
    var contexto: Context? = null,
    var emocion: Emociones? = null
) : Drawable() {

    constructor(contexto: Context, emocion: Emociones) : this(null, contexto, emocion) {
        this.contexto = contexto
        this.emocion = emocion
    }

    override fun draw(canvas: Canvas) {
        val fondo = Paint()
        fondo.style = Paint.Style.FILL
        fondo.isAntiAlias = true
        fondo.color = ContextCompat.getColor(contexto!!, R.color.gray)

        val ancho = (bounds.width() - 10).toFloat()
        val alto = (bounds.height() - 10).toFloat()
        coordenadas = RectF(0.0F, 0.0F, ancho, alto)
        canvas.drawRect(coordenadas!!, fondo)

        if (this.emocion != null) {
            val porcentaje: Float = this.emocion!!.porcentage * (bounds.width() - 10) / 100
            val coordenadas2 = RectF(0.0F, 0.0F, porcentaje, alto)
            val seccion = Paint()
            seccion.style = Paint.Style.FILL
            seccion.isAntiAlias = true
            seccion.color = ContextCompat.getColor(this.contexto!!, emocion!!.color)
            canvas.drawRect(coordenadas2, seccion)
        }
    }

    override fun setAlpha(alpha: Int) = Unit

    override fun setColorFilter(colorFilter: ColorFilter?) = Unit

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("PixelFormat.OPAQUE", "android.graphics.PixelFormat")
    )
    override fun getOpacity(): Int = PixelFormat.OPAQUE
}

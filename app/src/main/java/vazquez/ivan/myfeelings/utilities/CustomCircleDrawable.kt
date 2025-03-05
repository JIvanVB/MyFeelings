package vazquez.ivan.myfeelings.utilities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import vazquez.ivan.myfeelings.R

class CustomCircleDrawable(
    var coords: RectF? = null,
    var angBarrido: Float = 0.0F,
    var angInicio: Float = 0.0F,
    var grosorMetrica: Int = 0,
    var grosorFodno: Int = 0,
    var contexto: Context? = null,
    var emociones: ArrayList<Emociones> = ArrayList<Emociones>()
) : Drawable() {

    constructor(contexto: Context, emociones: ArrayList<Emociones>) : this(
        contexto = contexto,
        grosorMetrica = 10,
        grosorFodno = 15,
        emociones = emociones
    )

    override fun draw(canvas: Canvas) {

        val fondo: Paint = Paint()
        fondo.style = Paint.Style.STROKE
        fondo.strokeWidth = this.grosorFodno.toFloat()
        fondo.isAntiAlias = true
        fondo.strokeCap = Paint.Cap.ROUND
        fondo.color = ContextCompat.getColor(contexto!!, R.color.gray)
        val ancho: Float = (bounds.width() - 25).toFloat()
        val alto: Float = (bounds.height() - 25).toFloat()
        val coordenadas = RectF(25.0F, 25.0F, ancho, alto)

        canvas.drawArc(coordenadas, 0.0F, 360.0F, false, fondo)

        if (emociones.size!=0)
            for (e in emociones){

                this.angBarrido =((e.porcentage*360)/100).toFloat()

                val seccion = Paint().apply {
                    style=Paint.Style.STROKE
                    isAntiAlias=true
                    strokeWidth=grosorMetrica.toFloat()
                    strokeCap=Paint.Cap.SQUARE
                    color=ContextCompat.getColor(contexto!!,e.color)
                }

                canvas.drawArc(coordenadas,this.angInicio,angBarrido,false,seccion)
                this.angInicio+=this.angBarrido


            }
    }


    override fun setAlpha(alpha: Int) {

    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    @Deprecated("Deprecated in Java",
        ReplaceWith("PixelFormat.OPAQUE", "android.graphics.PixelFormat")
    )
    override fun getOpacity(): Int = PixelFormat.OPAQUE

}

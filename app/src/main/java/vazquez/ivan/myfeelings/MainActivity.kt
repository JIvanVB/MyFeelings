package vazquez.ivan.myfeelings

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import vazquez.ivan.myfeelings.databinding.ActivityMainBinding
import vazquez.ivan.myfeelings.utilities.CustomBarDrawable
import vazquez.ivan.myfeelings.utilities.CustomCircleDrawable
import vazquez.ivan.myfeelings.utilities.Emociones
import vazquez.ivan.myfeelings.utilities.JSONFile

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    var jsonFile: JSONFile? = null
    var veryHappy = 0.0F
    var happy = 0.0F
    var neutral = 0.0F
    var sad = 0.0F
    var verysad = 0.0F
    var data: Boolean = false
    var lista = ArrayList<Emociones>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val jsonfile = JSONFile()
        fetchingData()

        if (!data) {
            var emociones = ArrayList<Emociones>()
            val fondo = CustomCircleDrawable(this, emociones)
            binding.graph.background = fondo
            binding.tvh.background =
                CustomBarDrawable(this, Emociones("Muy feliz", 0.0F, R.color.mustard, veryHappy))
            binding.th.background =
                CustomBarDrawable(this, Emociones("Feliz", 0.0F, R.color.orange, happy))
            binding.tn.background =
                CustomBarDrawable(this, Emociones("Neutral", 0.0F, R.color.greenie, neutral))
            binding.ts.background =
                CustomBarDrawable(this, Emociones("Triste", 0.0F, R.color.blue, sad))
            binding.tvs.background =
                CustomBarDrawable(this, Emociones("Muy triste", 0.0F, R.color.deepBlue, verysad))
        } else {
            actualizarGrafica()
            iconoMayoria()
        }

        binding.guardar.setOnClickListener { guardar() }

        binding.bvs.setOnClickListener { verysad++;iconoMayoria();actualizarGrafica() }
        binding.bs.setOnClickListener { sad++;iconoMayoria();actualizarGrafica() }
        binding.bn.setOnClickListener { neutral++;iconoMayoria();actualizarGrafica() }
        binding.bh.setOnClickListener { happy++;iconoMayoria();actualizarGrafica() }
        binding.bvh.setOnClickListener { veryHappy++;iconoMayoria();actualizarGrafica() }


    }

    fun fetchingData() {
        try {
            var json: String = jsonFile?.getData(this) ?: ""
            if (json != "") {
                this.data = true
                var jsonArray = JSONArray(json)
                this.lista = parseJson(jsonArray)

                for (i in lista) {
                    when (i.nombre) {
                        "Muy feliz" -> veryHappy = i.total
                        "Feliz" -> happy = i.total
                        "Neutral" -> neutral = i.total
                        "Triste" -> sad = i.total
                        "Muy triste" -> verysad = i.total
                    }
                }
            } else
                this.data = false
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
    }

    private fun parseJson(jsonArray: JSONArray): ArrayList<Emociones> {
        val lista = ArrayList<Emociones>()

        for (i in 0 until jsonArray.length()) {
            try {
                val jsonObject = jsonArray.getJSONObject(i)
                val colorName = jsonObject.getString("color")

                // Obtener el ID del color desde colors.xml
                val colorResId = resources.getIdentifier(colorName, "color", packageName)
                if (colorResId == 0) {
                    Log.e("ErrorColor", "Color no encontrado: $colorName")
                    continue
                }

                // Obtener el color sin usar métodos obsoletos
                val colorInt = ContextCompat.getColor(this, colorResId)

                lista.add(
                    Emociones(
                        jsonObject.getString("nombre"),
                        jsonObject.getString("porcentage").toFloat(),
                        colorInt,  // Ahora es un color real, no solo el ID
                        jsonObject.getString("total").toFloat()
                    )
                )
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return lista
    }




    fun actualizarGrafica() {
        val total = veryHappy + happy + neutral + verysad + sad
        if (total == 0.0F) return

        var pVH = (veryHappy * 100 / total).toFloat()
        var pH = (happy * 100 / total).toFloat()
        var pN = (neutral * 100 / total).toFloat()
        var pS= (sad * 100 / total).toFloat()
        var pVS = (verysad * 100 / total).toFloat()


        Log.d("porcentajes", "very happy ${pVH}")
        Log.d("porcentajes", " happy ${pH}")
        Log.d("porcentajes", "neutral ${pN}")
        Log.d("porcentajes", "sad ${pS}")
        Log.d("porcentajes", "very sad ${pVS}")

        lista.clear()

        lista.add(Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        lista.add(Emociones("Feliz", pH, R.color.orange, happy))
        lista.add(Emociones("Neutral", pN, R.color.greenie, neutral))
        lista.add(Emociones("Triste", pS, R.color.blue, sad))
        lista.add(Emociones("Muy triste", pVS, R.color.deepBlue, verysad))

        val fondo = CustomCircleDrawable(this, lista)


        binding.tvh.background =
            CustomBarDrawable(this, Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        binding.th.background =
            CustomBarDrawable(this, Emociones("Feliz", pH, R.color.orange, happy))
        binding.tn.background =
            CustomBarDrawable(this, Emociones("Neutral", pN, R.color.greenie, neutral))
        binding.ts.background = CustomBarDrawable(this, Emociones("Triste", pS, R.color.blue, sad))
        binding.tvs.background =
            CustomBarDrawable(this, Emociones("Muy triste", pVS, R.color.deepBlue, verysad))

        binding.graph.background = fondo
    }

    fun iconoMayoria() {
        val moods = mapOf(
            "happy" to R.drawable.ic_happy,
            "veryHappy" to R.drawable.ic_veryhappy,
            "neutral" to R.drawable.ic_neutral,
            "sad" to R.drawable.ic_sad,
            "verySad" to R.drawable.ic_verysad
        )

        val maxMood = listOf(happy, veryHappy, neutral, sad, verysad).maxOrNull()

        when (maxMood) {
            happy -> binding.icon.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    moods["happy"]!!
                )
            )

            veryHappy -> binding.icon.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    moods["veryHappy"]!!
                )
            )

            neutral -> binding.icon.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    moods["neutral"]!!
                )
            )

            sad -> binding.icon.setImageDrawable(ContextCompat.getDrawable(this, moods["sad"]!!))
            verysad -> binding.icon.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    moods["verySad"]!!
                )
            )
        }
    }


    fun guardar() {
        var jsonArray = JSONArray()
        var o: Int = 0
        for (i in lista) {
            Log.d("objetos", i.toString())
            var j = JSONObject()
            j.put("nombre", i.nombre)
            j.put("porcentaje", i.porcentage)
            j.put("color", i.color)
            j.put("total", i.total)
            jsonArray.put(j)
            o++
        }
        jsonFile?.saveData(this, jsonArray.toString())
        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
    }
}
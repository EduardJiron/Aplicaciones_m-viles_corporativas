package com.example.procesojx

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.json.responseJson
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.button)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button4)
        button.setOnClickListener {
            obtenerRepositorios("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")
        }
        button2.setOnClickListener {
            crearRepositorio("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")
        }
        button3.setOnClickListener {
            eliminarRepositorio("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", "NuevoRepositorio2", "EduardJiron")
        }
    }

    fun obtenerRepositorios(token: String) {
        val url = "https://api.github.com/user/repos"
        val headers = mapOf("Authorization" to "token $token")

        Fuel.get(url)
            .header(headers)
            .responseJson { _, response, result ->
                if (response.isSuccessful) {
                    val repos = result.get().content
                    val jsonArray = JSONArray(repos)
                    for (i in 0 until jsonArray.length()) {
                        val repo = jsonArray.getJSONObject(i)
                        val repoName = repo.getString("name")
                        println("Repositorio: $repoName")
                    }
                } else {
                    println("Error al obtener repositorios: ${response.statusCode}")
                }

            }
    }

    fun crearRepositorio(token: String) {
        val url = "https://api.github.com/user/repos"
        val headers = mapOf("Authorization" to "token $token", "Content-Type" to "application/json")
        val repoData = JSONObject()
        repoData.put("name", "NuevoRepositorio2")
        repoData.put(
            "description",
            "Este es un nuevo repositorio creado desde una aplicación de Android"
        )

        Fuel.post(url)
            .header(headers)
            .body(repoData.toString())
            .response { _, response, result ->
                if (response.isSuccessful) {
                    println("Repositorio creado exitosamente")
                } else {
                    println("Error al crear el repositorio: ${response.statusCode}")

                }
            }
    }
    fun eliminarRepositorio(token: String, nombreRepo: String,user:String) {
        val url = "https://api.github.com/repos/$user/$nombreRepo"
        val headers = mapOf("Authorization" to "token $token", "Content-Type" to "application/json")

        Fuel.delete(url)
            .header(headers)
            .responseString { _, response, result ->
                when (response.statusCode) {
                    204 -> println("Repositorio eliminado exitosamente")
                    else -> println("Error al eliminar el repositorio: ${response.statusCode}")
                }
            }

    }
}



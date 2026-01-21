package com.example.mobile2.api

import com.example.mobile2.data.Ingredient
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object Api {
    private const val BASE_URL = "http://192.168.0.17:8080"

    fun getIngredients(): List<Ingredient> {
        val url = URL("$BASE_URL/ingredients")
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 8000
            readTimeout = 8000
        }

        val code = conn.responseCode
        val stream = if (code in 200..299) conn.inputStream else conn.errorStream
        val body = stream.bufferedReader().use(BufferedReader::readText)
        conn.disconnect()

        if (code !in 200..299) throw RuntimeException(body)

        val arr = JSONArray(body)
        val list = ArrayList<Ingredient>(arr.length())
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(
                Ingredient(
                    id = if (o.has("id") && !o.isNull("id")) o.getLong("id") else null,
                    name = o.getString("name"),
                    category = o.getString("category"),
                    expiryDate = o.getString("expiryDate"),
                    storageType = o.getString("storageType"),
                    status = if (o.has("status") && !o.isNull("status")) o.getString("status") else null
                )
            )
        }
        return list
    }

    fun addIngredient(body: Ingredient): Ingredient {
        val url = URL("$BASE_URL/ingredients")
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 8000
            readTimeout = 8000
            doOutput = true
            setRequestProperty("Content-Type", "application/json; charset=utf-8")
        }

        val json = JSONObject().apply {
            put("name", body.name)
            put("category", body.category)
            put("expiryDate", body.expiryDate)
            put("storageType", body.storageType)
        }

        OutputStreamWriter(conn.outputStream, Charsets.UTF_8).use { it.write(json.toString()) }

        val code = conn.responseCode
        val stream = if (code in 200..299) conn.inputStream else conn.errorStream
        val resp = stream.bufferedReader().use(BufferedReader::readText)
        conn.disconnect()

        if (code !in 200..299) throw RuntimeException(resp)

        val o = JSONObject(resp)
        return Ingredient(
            id = if (o.has("id") && !o.isNull("id")) o.getLong("id") else null,
            name = o.getString("name"),
            category = o.getString("category"),
            expiryDate = o.getString("expiryDate"),
            storageType = o.getString("storageType"),
            status = if (o.has("status") && !o.isNull("status")) o.getString("status") else null
        )
    }
}
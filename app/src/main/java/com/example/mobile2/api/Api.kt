package com.example.mobile2.api

import com.example.mobile2.data.FoodItem
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object Api {
    private const val BASE_URL = "http://192.168.0.17:8080"

    fun getIngredients(): List<FoodItem> {
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
        val list = ArrayList<FoodItem>(arr.length())
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(
                FoodItem(
                    id = o.getInt("id"),
                    name = o.getString("name"),
                    category = o.getString("category"),
                    expiryDate = o.getString("expiryDate"),
                    storageType = o.getString("storageType"),
                    status = o.getString("status")
                )
            )
        }
        return list
    }

    fun addIngredient(body: FoodItem): FoodItem {
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
        return FoodItem(
            id = o.getInt("id"),
            name = o.getString("name"),
            category = o.getString("category"),
            expiryDate = o.getString("expiryDate"),
            storageType = o.getString("storageType"),
            status = o.getString("status")
        )

    }

    fun getIngredientIds(): List<Int> {
        val url = URL("$BASE_URL/ingredients/ids")
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 8000
            readTimeout = 8000
        }

        val code = conn.responseCode
        val stream = if (code in 200..299) conn.inputStream else conn.errorStream
        val body = stream.bufferedReader().readText()
        conn.disconnect()

        if (code !in 200..299) throw RuntimeException(body)

        val arr = JSONArray(body)
        val ids = ArrayList<Int>(arr.length())
        for (i in 0 until arr.length()) {
            ids.add(arr.getInt(i))
        }
        return ids
    }


}
package com.example.mobile2.Repository

import com.example.mobile2.data.AlarmItem

object AlarmRepository {

    private val items = mutableListOf<AlarmItem>()

    fun getAll(): List<AlarmItem> {
        return items
    }

    fun add(item: AlarmItem) {
        // 같은 내용 중복 방지
        if (items.any { it.content == item.content }) return
        items.add(0, item) // 최신 알림을 위로
    }

    fun clear() {
        items.clear()
    }
}

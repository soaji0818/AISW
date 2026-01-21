package com.example.mobile2.Repository

import com.example.mobile2.data.AlarmItem

object AlarmRepository {

    private val alarmList = mutableListOf<AlarmItem>()

    fun add(alarm: AlarmItem) {
        alarmList.add(0, alarm) // 최신 알림 위로
    }

    fun getAll(): List<AlarmItem> {
        return alarmList
    }
}

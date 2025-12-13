package com.example.app_prueba.data.repository

import com.example.app_prueba.data.model.BenefitItem
import com.example.app_prueba.data.model.HistoryItem
import com.example.app_prueba.data.model.RewardItem
import com.example.app_prueba.data.model.UserPointsSummary
import com.example.app_prueba.data.remote.RetrofitInstance

class PointsRepository {
    val api = RetrofitInstance.api

    suspend fun getSummary(): UserPointsSummary? {
        val response = api.getPointsSummary()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun getHistory(filter: String): List<HistoryItem> {
        val response = api.getPointsHistory(filter)
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }

    suspend fun getBenefits(): List<BenefitItem> {
        val response = api.getBenefits()
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }

    suspend fun getRewards(): List<RewardItem> {
        val response = api.getRewards()
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }

    suspend fun redeemReward(rewardId: String): Boolean {
        val response = api.redeemReward(rewardId)
        return response.isSuccessful
    }
}
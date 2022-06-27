package com.plcoding.core.domain.model

import com.ivan.m.core.domain.model.ActivityLevel
import com.ivan.m.core.domain.model.Gender
import com.ivan.m.core.domain.model.GoalType

data class UserInfo(
    val gender: Gender,
    val age: Int,
    val weight: Float,
    val height: Int,
    val activityLevel: ActivityLevel,
    val goalType: GoalType,
    val carbRatio: Float,
    val proteinRatio: Float,
    val fatRatio: Float
)

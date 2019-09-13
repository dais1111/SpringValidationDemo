package com.dais1111.SpringValidationDemo.entity

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(value = Parent::class, name ="Parent"),
    JsonSubTypes.Type(value = Child::class, name ="Child")
)
interface BaseEntity

data class Parent(
        val type: Type,
        @field:NotEmpty
        val id: String,
        val name: String? = null,
        @field:Min(18)
        val age: Int,
        val liveInPrefect: Prefecture,
        val premiumFlag: Boolean = false,
        val premiumPrice: Int? = null
) : BaseEntity

data class Child(
        val type: Type,
        @field:NotEmpty
        val id: String,
        @field:NotEmpty
        val parentId: String,
        val name: String? = null,
        @field:Max(17)
        val age: Int,
        val liveInPrefect: Prefecture,
        val premiumFlag: Boolean = false,
        val premiumPrice: Int? = null
) : BaseEntity

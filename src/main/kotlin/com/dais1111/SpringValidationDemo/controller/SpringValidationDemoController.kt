package com.dais1111.SpringValidationDemo.controller

import com.dais1111.SpringValidationDemo.entity.*
import com.dais1111.SpringValidationDemo.validation.ChildValidator
import com.dais1111.SpringValidationDemo.validation.ParentValidator
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.validation.BindingResult
import org.springframework.validation.DataBinder
import org.springframework.validation.FieldError
import org.springframework.validation.SmartValidator
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SpringValidationDemoController(
        private val smartValidator: SmartValidator,
        private val objectMapper: ObjectMapper
) {
    val users = arrayOf(
            Parent(type = Type.Parent, id = "C001", name = "Dai", age = 30, liveInPrefect = Prefecture.Tokyo, premiumFlag = true, premiumPrice = 3990),
            Parent(type = Type.Parent, id = "", name = null, age = 19, liveInPrefect = Prefecture.Tokyo, premiumFlag = false, premiumPrice = null),
            Parent(type = Type.Parent, id = "A001", name = "Mahiro", age = 2, liveInPrefect = Prefecture.Tokyo, premiumFlag = false, premiumPrice = null),
            Parent(type = Type.Parent, id = "", name = "Mahiro", age = 2, liveInPrefect = Prefecture.Tokyo, premiumFlag = true, premiumPrice = null),
            Child(type = Type.Child, id = "C001", parentId = "P001", name = "Mahiro", age = 2, liveInPrefect = Prefecture.Tokyo),
            Child(type = Type.Child, id = "C001", parentId = "", name = "Masahiro", age = 20, liveInPrefect = Prefecture.Tokyo)
    )
    val usersJson = arrayOf(
            "{\"type\":\"Parent\",\"id\":\"C001\",\"name\":\"Dai\",\"age\":30,\"liveInPrefect\":\"Tokyo\",\"premiumFlag\":true,\"premiumPrice\":3990}",
            "{\"type\":\"Parent\",\"id\":\"\",\"age\":19,\"liveInPrefect\":\"Tokyo\",\"premiumFlag\":false},{\"type\":\"Parent\",\"id\":\"A001\",\"name\":\"Mahiro\",\"age\":2,\"liveInPrefect\":\"Tokyo\",\"premiumFlag\":false}",
            "{\"type\":\"Parent\",\"id\":\"\",\"name\":\"Mahiro\",\"age\":2,\"liveInPrefect\":\"Tokyo\",\"premiumFlag\":true}",
            "{\"type\":\"Child\",\"id\":\"C001\",\"parentId\":\"P001\",\"name\":\"Mahiro\",\"age\":2,\"liveInPrefect\":\"Tokyo\",\"premiumFlag\":false}",
            "{\"type\":\"Child\",\"id\":\"C001\",\"parentId\":\"\",\"name\":\"Masahiro\",\"age\":20,\"liveInPrefect\":\"Tokyo\",\"premiumFlag\":false}"
    )

    @GetMapping("/validuser")
    fun validuser(@RequestParam("num") num: Int): String {
        // spring-validator and bean validation
        val errors = DataBinder(users[num], "userEntity").also {
            it.addValidators(if (num < 4) ParentValidator() else ChildValidator())
            it.validate()
        }.bindingResult
        smartValidator.validate(users[num], errors)

        return if (errors.hasErrors()) {
            getErrorsString(errors)
        } else {
            "valid"
        }
    }

    fun getErrorsString(errors: BindingResult): String {
        val str = StringBuilder("----invalid----\n")

        errors.allErrors.forEach {
            str.append("objectName=${it.objectName}, field=${(it as? FieldError)?.field
                    ?: ""}, message=${it.defaultMessage}\n")
        }

        return str.toString()
    }

    @GetMapping("/serializeuser")
    fun serializeuser() = objectMapper.writeValueAsString(users)

    @GetMapping("/deserializeuser")
    fun deserializeuser(@RequestParam("num") num: Int) =
            "${usersJson[num]}\n${objectMapper.readValue(usersJson[num], BaseEntity::class.java)}"
}
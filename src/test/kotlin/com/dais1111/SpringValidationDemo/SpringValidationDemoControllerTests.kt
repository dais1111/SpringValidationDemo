package com.dais1111.SpringValidationDemo

import com.dais1111.SpringValidationDemo.controller.SpringValidationDemoController
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.validation.SmartValidator

class SpringValidationDemoControllerTests {
    // mock
    val smartValidator = mockk<SmartValidator>()
    val objectMapper = mockk<ObjectMapper>()

    // target
    val target = SpringValidationDemoController(smartValidator, objectMapper)

    // test data
    val errorField = BeanPropertyBindingResult("", "").also {
        it.addError(FieldError("objA", "fldA", "msgA"))
    }
    val errorsObject = BeanPropertyBindingResult("", "").also {
        it.addError(ObjectError("objB", "msgB"))
    }
    val errors = BeanPropertyBindingResult("", "").also {
        it.addError(FieldError("objA", "fldA", "msgA"))
        it.addError(ObjectError("objB", "msgB"))
    }

    @Test
    fun `check getErrorString`() {
        val expect1 = "----invalid----\nobjectName=objA, field=fldA, message=msgA\n"
        val test1 = target.getErrorsString(errorField)
        Assertions.assertEquals(expect1, test1)

        val expect2 = "----invalid----\nobjectName=objB, field=, message=msgB\n"
        val test2 = target.getErrorsString(errorsObject)
        Assertions.assertEquals(expect2, test2)

        val expect3 = "----invalid----\nobjectName=objA, field=fldA, message=msgA\nobjectName=objB, field=, message=msgB\n"
        val test3 = target.getErrorsString(errors)
        Assertions.assertEquals(expect3, test3)
    }
}
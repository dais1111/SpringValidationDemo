package com.dais1111.SpringValidationDemo.validation

import com.dais1111.SpringValidationDemo.entity.Child
import com.dais1111.SpringValidationDemo.entity.Parent
import org.springframework.validation.Errors
import org.springframework.validation.Validator

class ParentValidator: Validator {
    override fun supports(clazz: Class<*>) = Parent::class.java.equals(clazz)

    override fun validate(target: Any, errors: Errors) {
        if(target !is Parent){
            return
        }

        val userEntity:Parent = target

        if(userEntity.premiumFlag && userEntity.premiumPrice == null){
            errors.rejectValue("premiumPrice", "A0001", "premiumPrice is required, for premium member")
        }
    }
}

class ChildValidator: Validator {
    override fun supports(clazz: Class<*>) = Child::class.java.equals(clazz)

    override fun validate(target: Any, errors: Errors) {
        if(target !is Child){
            return
        }

        val userEntity:Child = target

        if(userEntity.premiumFlag && userEntity.premiumPrice == null){
            errors.rejectValue("premiumPrice", "A0001", "premiumPrice is required, for premium member")
        }
    }
}
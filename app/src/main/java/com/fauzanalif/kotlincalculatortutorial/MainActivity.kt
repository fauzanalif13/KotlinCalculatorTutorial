package com.fauzanalif.kotlincalculatortutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Double.parseDouble


class MainActivity : AppCompatActivity()
{
    private var canAddOperation = false
    private var canAddDecimal = true

    private fun calculateResults(): String
    {
        val digitsOperator = digitsOperator()
        if (digitsOperator.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate (digitsOperator)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate (timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float
    {
        var result = passedList[0] as Float
//        val operator = ""
        for (i in passedList.indices){
            if (passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i].toString()
                val nextDigit = passedList [i + 1] as Float
                if (operator =="+"){
                    result = result + nextDigit
                }

                if (operator =="-"){
                    result = result - nextDigit
                }

            }
        }

        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('x') || list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()

        var restartIndex = passedList.size

        for(i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as Float

                when(operator){
                    'x'-> {
                        newList.add(prevDigit*nextDigit)
                        restartIndex = i + 1
                    }
                    '/'-> {
                        newList.add(prevDigit/nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }

                }
            }

            if (i > restartIndex){
                newList.add(passedList[i])
            }
        }

        return newList
    }

    private fun digitsOperator(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""

        for (character in workingsTV.text){
            if(character.isDigit() || character =='.')
                currentDigit = currentDigit + character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit!="")
            list.add(currentDigit.toFloat())

        return list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun allClearSection(view: View)
    {
        workingsTV.text=""
        resultsTV.text=""
    }
    fun clearSection(view: View)
    {
        workingsTV.text=""
    }
    fun backspaceAction(view: View)
    {
        val length = workingsTV.length()
        if (length>0)
            workingsTV.text = workingsTV.text.subSequence(0,length-1)
    }
    fun equalsAction(view: View)
    {
        resultsTV.text = calculateResults()
    }

    fun numberAction(view: View)
    {
        if (view is Button)
        {
//            try {
//                val num = parseDouble(workingsTV.text as String)
//            } catch (e: NumberFormatException) {
//                isNumeric = false
//            }
//
//            if (isNumeric){
//                workingsTV.append(view.text)
//            } else {
//                workingsTV.text = ""
//                isNumeric = true
//            }

            //decimal
            if (view.text == ".")
            {
                if(canAddDecimal)
                    workingsTV.append(view.text)
                canAddDecimal = false
            } else
                workingsTV.append(view.text)
//            workingsTV.append(view.text)
            canAddOperation = true
        }
    }
    fun operatorAction(view: View)
    {
        if (view is Button && canAddOperation)
        {
            workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }
}
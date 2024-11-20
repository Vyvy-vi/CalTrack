package com.vyvyvi.caltrack.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View

class ProgressDrawing(
    context: Context?,
    caloriesCurr: Float,
    waterCurr: Float,
    caloriesMax: Int,
    waterMax: Int
) : View(context) {
    lateinit var paint: Paint
    var sweepCal: Float = 1f
    var sweepWater: Float = 1f
    var calories: String = ""
    var water: String = ""

    init {
        init()
        this.calories = (caloriesCurr).toInt().toString()
        this.water = (waterCurr).toInt().toString()
        this.sweepCal = ((caloriesCurr + 1) / caloriesMax) * 360f
        this.sweepWater = ((waterCurr + 0.01f) / waterMax) * 360f
    }

    private fun init() {
        paint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        val centerX: Float = width / 2.0f
        val centerY: Float = 400f
        val radius1: Float = 150f
        val radius2: Float = 210f
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 30f
        paint.strokeCap = Paint.Cap.ROUND

        paint.color = Color.CYAN
        canvas.drawArc(
            RectF(
                centerX - radius1,
                centerY - radius1,
                centerX + radius1,
                centerY + radius1
            ), -90f, sweepWater, false, paint
        )

        paint.color = Color.GREEN
        canvas.drawArc(
            RectF(
                centerX - radius2,
                centerY - radius2,
                centerX + radius2,
                centerY + radius2
            ), -90f, sweepCal, false, paint
        )
        val paint = Paint()
        paint.textSize = 86f
        paint.color = Color.GREEN

        var textX = (width - paint.measureText(calories)) / 2.0f // Center the text horizontally
        var textY = centerX - radius2 + paint.textSize
        canvas.drawText(calories, textX, textY, paint)
        textX = (width - paint.measureText(water)) / 2.0f
        textY = centerX - radius2 + paint.textSize + paint.textSize
        paint.textSize = 64f
        paint.color = Color.CYAN
        canvas.drawText(water, textX, textY, paint)
    }
}
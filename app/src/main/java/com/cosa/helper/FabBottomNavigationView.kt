package com.cosa.helper

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorInt
import com.cosa.R
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel


@SuppressLint("RestrictedApi")
class FabBottomNavigationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

    private var topCurvedEdgeTreatment: BottomAppBarTopEdgeTreatment
    private var materialShapeDrawable: MaterialShapeDrawable
    private var fabSize = 0F
    var fabCradleMargin = 0F
    var fabCradleRoundedCornerRadius = 0F
    var cradleVerticalOffset = 0F

    init {
        val ta =
            context.theme.obtainStyledAttributes(attrs, R.styleable.FabBottomNavigationView, 0, 0)
        fabSize = ta.getDimension(R.styleable.FabBottomNavigationView_fab_size, 0F)
        fabCradleMargin = ta.getDimension(R.styleable.FabBottomNavigationView_fab_cradle_margin, 0F)
        fabCradleRoundedCornerRadius =
            ta.getDimension(
                R.styleable.FabBottomNavigationView_fab_cradle_rounded_corner_radius,
                0F
            )
        cradleVerticalOffset =
            ta.getDimension(R.styleable.FabBottomNavigationView_cradle_vertical_offset, 0F)

        topCurvedEdgeTreatment = BottomAppBarTopEdgeTreatment(
            fabCradleMargin,
            fabCradleRoundedCornerRadius,
            cradleVerticalOffset
        ).apply {
            fabDiameter = fabSize
        }

        val shapeAppearanceModel = ShapeAppearanceModel.Builder()
            .setTopEdge(topCurvedEdgeTreatment)
            .build()
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.backgroundColorMore, typedValue, true)
        @ColorInt val color = typedValue.data
        materialShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            setTint(color)
            elevation = 10F
            paintStyle = Paint.Style.FILL_AND_STROKE
        }
        background = materialShapeDrawable
    }

    fun transform(showHole: Boolean) {
        if (!showHole) {
            ValueAnimator.ofFloat(materialShapeDrawable.interpolation, 0F).setDuration(700).apply {
                addUpdateListener {
                    materialShapeDrawable.interpolation = it.animatedValue as Float
                }
                start()
            }
        } else {
            ValueAnimator.ofFloat(materialShapeDrawable.interpolation, 1F).setDuration(200).apply {
                addUpdateListener {
                    materialShapeDrawable.interpolation = it.animatedValue as Float
                }
                start()
            }
        }

    }
}
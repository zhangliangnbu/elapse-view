package com.liang.elapseview

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView

/**
 * created by zhangliang on 2018/12/27
 * profile: zhangliangnbu@163.com
 */
class ElapseView : ConstraintLayout {
    companion object {
        private const val MAX_COEXIST_COMMENT = 3
    }

    private val tvs = mutableListOf<TextView>()
    private val oas = mutableListOf<ObjectAnimator>()
    private var commentIndex = 0

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
            : super(context, attributeSet, defStyleAttr) {
        init(context, attributeSet, defStyleAttr)
    }

    private fun init(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) {
        initView(this)
    }

    private fun initView(cl: ConstraintLayout) {
        for (i in 1..MAX_COEXIST_COMMENT) {
            // tv
            val tv = TextView(cl.context)
            tv.id = View.generateViewId()
            tv.setTextColor(Color.GRAY)
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13f)
            tv.setSingleLine()
            tv.gravity = Gravity.CENTER_VERTICAL or Gravity.START
            cl.addView(tv)
            val set = ConstraintSet()
            set.constrainWidth(tv.id, ConstraintSet.WRAP_CONTENT)
            set.constrainHeight(tv.id, ConstraintSet.WRAP_CONTENT)
            set.connect(tv.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
            set.connect(tv.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
            set.connect(tv.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0)
            set.applyTo(cl)
            // oa
            val oa = ObjectAnimator.ofFloat(tv, "translationX", 0f, 0f).setDuration(5000)

            tvs.add(tv)
            oas.add(oa)
        }
    }


    /**
     * @param text text to elapse
     */
    fun updateText(text: String) {
        val index = commentIndex % MAX_COEXIST_COMMENT
        val oa = oas[index]
        val tv = tvs[index]
        if (oa.isRunning) {
            oa.cancel()
        }
        tv.text = text
        tv.translationX = getWindowWidth()
        tv.post {
            oa.setFloatValues(getWindowWidth(), -1f * tv.width)
            oa.start()
        }
        commentIndex++
    }

    private fun getWindowWidth(): Float {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        return dm.widthPixels * 1f
    }
}
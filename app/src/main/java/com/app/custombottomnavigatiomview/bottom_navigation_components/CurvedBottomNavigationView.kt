package com.app.custombottomnavigatiomview.bottom_navigation_components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.app.custombottomnavigatiomview.R
import com.app.custombottomnavigatiomview.interfaces.OnBottomNavItemClickedListener
import com.app.custombottomnavigatiomview.interfaces.OnBottomNavItemSelectedListener
import com.app.custombottomnavigatiomview.model.BottomNavigationData
import com.app.custombottomnavigatiomview.util.dpToPx


class CurvedBottomNavigationView(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private var mPath: Path? = null
    private var mPaint: Paint? = null

    private var firstCurveStartPoint: Point = Point()
    private var firstCurveEndPoint: Point = Point()
    private var firstCurveControlPoint2: Point = Point()
    private var firstCurveControlPoint1: Point = Point()
    private var secondCurveStartPoint: Point = Point()
    private var secondCurveEndPoint: Point = Point()
    private var secondCurveControlPoint1: Point = Point()
    private var secondCurveControlPoint2: Point = Point()

    private val curveCircleRadius = 28
    private val curveParam1 = 30
    private val curveStartParam = 30
    private val cornerRadius = dpToPx(28).toFloat()
    private var navigationBarWidth = 0
    private var navigationBarHeight = 0


    companion object {
        private const val DEFAULT_SHADOW_RADIUS = 20f
    }

    private var currentSelectedItem: BottomNavigationViewItem? = null
    private var selectedListener: OnBottomNavItemSelectedListener? = null
    private var currentSelectedIndex: Int = 0
    private var data = arrayOf<BottomNavigationData>()

    init {
        LayoutInflater.from(context).inflate(R.layout.bottom_navigation, this, false)
        elevation = context.dpToPx(25).toFloat()
        setBackgroundResource(R.color.white)

        init()
    }

    fun bind(data: Array<BottomNavigationData>) {
        this.data = data

        val lp = LayoutParams(30, LayoutParams.MATCH_PARENT, 1f)

        data.forEach {
            val itemView = BottomNavigationViewItem(context)
            val navItemIV = itemView.findViewById<TextView>(R.id.navItemTV)
            val navItemImageView = itemView.findViewById<ImageView>(R.id.navItemIV)
            navItemIV.text = it.title
            it.backgroundRes?.let { it1 -> navItemImageView.setBackgroundResource(it1) }
            itemView.id = it.id!!
            itemView.index = it.id!!
            itemView.layoutParams = lp
            setPadding(it, itemView)
            if (it.id == 3) {
                val emptyItemView = BottomNavigationViewItem(context)
                emptyItemView.findViewById<TextView>(R.id.navItemTV).text = ""
                it.backgroundRes?.let { "" }
                emptyItemView.index = 0
                emptyItemView.layoutParams = lp
                emptyItemView.visibility = INVISIBLE
                addView(emptyItemView)
            }
            addView(itemView)
        }
    }

    private fun setPadding(it: BottomNavigationData, itemView: BottomNavigationViewItem) {
        val navItemLinear = itemView.findViewById<ConstraintLayout>(R.id.navItemLinear)
        when {
            it.id!! == 0 -> {
                navItemLinear.setPadding(0, 0, 10, 0)
            }
            it.id!! == 1 -> {
                navItemLinear.setPadding(0, 0, 50, 0)
            }
            it.id!! == 2 -> {
                itemView.visibility = INVISIBLE
            }
            it.id!! == 3 -> {
                navItemLinear.setPadding(50, 0, 0, 0)
            }
            it.id!! == 4 -> {
                navItemLinear.setPadding(10, 0, 0, 0)
            }
        }
    }

    fun setOnBottomNavItemClickedListener(listener: OnBottomNavItemClickedListener) {
        children.filterIsInstance<BottomNavigationViewItem>()
            .forEach {
                it.listener = listener
            }
    }

    fun selectItemAtIndex(index: Int) {
        val itemView = getChildAt(index) as BottomNavigationViewItem

        if (currentSelectedItem != null && currentSelectedItem != itemView) {
            currentSelectedItem?.unSelect()
        }

        if (index != 2) {
            itemView.select()
            currentSelectedItem = itemView
            currentSelectedIndex = index
            selectedListener?.onItemSelected(index)
        } else {
            itemView.visibility = INVISIBLE
        }
    }

    private fun init() {
        mPath = Path()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.style = Paint.Style.FILL_AND_STROKE
        mPaint!!.color = Color.WHITE

        mPaint!!.setShadowLayer(
            DEFAULT_SHADOW_RADIUS,
            0F,
            0F,
            ContextCompat.getColor(context, R.color.shadow_color)
        )
        setBackgroundColor(Color.TRANSPARENT)

        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_SOFTWARE, mPaint)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        navigationBarWidth = width
        navigationBarHeight = height

        firstCurveStartPoint.set(
            (navigationBarWidth / 1.9 - dpToPx(curveCircleRadius) * 2 -
                    dpToPx(curveStartParam)).toInt(),
            0
        )

        firstCurveEndPoint.set(
            navigationBarWidth / 2,
            dpToPx(curveCircleRadius) * 2 +
                    dpToPx(curveCircleRadius) / 2
        )

        secondCurveStartPoint = firstCurveEndPoint
        secondCurveEndPoint.set(
            (navigationBarWidth / 2.1 + dpToPx(curveCircleRadius) * 2 +
                    dpToPx(curveStartParam)).toInt(),
            0
        )

        firstCurveControlPoint1.set(
            firstCurveStartPoint.x + dpToPx(curveCircleRadius) +
                    dpToPx(curveCircleRadius) / 4,
            firstCurveStartPoint.y
        )

        firstCurveControlPoint2.set(
            firstCurveEndPoint.x - dpToPx(curveCircleRadius) * 2 +
                    dpToPx(curveCircleRadius) - dpToPx(curveParam1),
            firstCurveEndPoint.y
        )

        secondCurveControlPoint1.set(
            secondCurveStartPoint.x + dpToPx(curveCircleRadius) * 2 -
                    dpToPx(curveCircleRadius) + dpToPx(curveParam1),
            secondCurveStartPoint.y
        )
        secondCurveControlPoint2.set(
            secondCurveEndPoint.x - (dpToPx(curveCircleRadius) + dpToPx(curveCircleRadius) / 4),
            secondCurveEndPoint.y
        )
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPath!!.reset()

        val firstRect = RectF(
            0f,
            20f,
            firstCurveStartPoint.x.toFloat(),
            height.toFloat()
        )

        mPath!!.addPath(
            roundedRectPath
                (
                firstRect,
                cornerRadius,
                0f,
                0f,
                0f
            )
        )

        val secondRect = RectF(
            firstCurveStartPoint.x.toFloat(),
            20f,
            secondCurveEndPoint.x.toFloat(),
            height.toFloat()
        )

        mPath!!.addPath(
            roundedRectPath
                (
                secondRect,
                0f,
                0f,
                0f,
                0f
            )
        )

        val thirdRect = RectF(
            secondCurveEndPoint.x.toFloat(),
            20f,
            width.toFloat(),
            height.toFloat()
        )

        mPath!!.addPath(
            roundedRectPath
                (
                thirdRect,
                0f,
                cornerRadius,
                0f,
                0f
            )
        )

        mPath!!.lineTo(firstCurveStartPoint.x.toFloat(), 20f)

        mPath!!.cubicTo(
            firstCurveControlPoint1.x.toFloat(), firstCurveControlPoint1.y.toFloat() + 20,
            firstCurveControlPoint2.x.toFloat(), firstCurveControlPoint2.y.toFloat(),
            firstCurveEndPoint.x.toFloat(), firstCurveEndPoint.y.toFloat()
        )


        mPath!!.cubicTo(
            secondCurveControlPoint1.x.toFloat(), secondCurveControlPoint1.y.toFloat(),
            secondCurveControlPoint2.x.toFloat(), secondCurveControlPoint2.y.toFloat() + 20,
            secondCurveEndPoint.x.toFloat(), secondCurveEndPoint.y.toFloat() + 20
        )

        mPaint?.let { canvas.drawPath(mPath!!, it) }
    }

    private fun roundedRectPath(
        rect: RectF,
        topLeftDiameter: Float,
        topRightDiameter: Float,
        bottomRightDiameter: Float,
        bottomLeftDiameter: Float
    ): Path {
        var topLeftDim = topLeftDiameter
        var topRightDim = topRightDiameter
        var bottomRightDim = bottomRightDiameter
        var bottomLeftDim = bottomLeftDiameter
        val path = Path()

        topLeftDim = if (topLeftDim < 0) 0f else topLeftDim
        topRightDim = if (topRightDim < 0) 0f else topRightDim
        bottomLeftDim = if (bottomLeftDim < 0) 0f else bottomLeftDim
        bottomRightDim = if (bottomRightDim < 0) 0f else bottomRightDim

        path.moveTo(rect.left + topLeftDim, rect.top)
        path.lineTo(rect.right - topRightDim, rect.top)
        path.quadTo(rect.right, rect.top, rect.right, rect.top + topRightDim)
        path.lineTo(rect.right, rect.bottom - bottomRightDim)
        path.quadTo(rect.right, rect.bottom, rect.right - bottomRightDim, rect.bottom)
        path.lineTo(rect.left + bottomLeftDim, rect.bottom)
        path.quadTo(rect.left, rect.bottom, rect.left, rect.bottom - bottomLeftDim)
        path.lineTo(rect.left, rect.top + topLeftDim)
        path.quadTo(rect.left, rect.top, rect.left + topLeftDim, rect.top)
        path.close()
        return path
    }
}
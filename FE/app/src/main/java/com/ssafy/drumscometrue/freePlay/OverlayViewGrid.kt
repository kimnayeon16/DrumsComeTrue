//package com.ssafy.drumscometrue.freePlay
//
//import android.content.Context
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.Paint
//import android.util.AttributeSet
//import android.view.View
//
//class OverlayViewGrid(context: Context?, attrs: AttributeSet?) :
//    View(context, attrs) {
//
//    // ... (기존 코드)
//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        // 그리드를 그리는 메서드 호출
//        drawGrid(canvas)
//    }
//
//    private fun drawGrid(canvas: Canvas) {
//        val gridSize = 10 // 그리드 크기: 10x10
//        val cellWidth = width / gridSize
//        val cellHeight = height / gridSize
//        val paint = Paint()
//        paint.color = Color.GRAY
//
//        // 수직 라인 그리기
//        for (i in 1 until gridSize) {
//            val x = i * cellWidth
//            canvas.drawLine(x.toFloat(), 0f, x.toFloat(), height.toFloat(), paint)
//        }
//
//        // 수평 라인 그리기
//        for (i in 1 until gridSize) {
//            val y = i * cellHeight
//            canvas.drawLine(0f, y.toFloat(), width.toFloat(), y.toFloat(), paint)
//        }
//
//        // 각 셀에 숫자 표시
//        val textPaint = Paint()
//        textPaint.textSize = 24f
//        textPaint.color = Color.BLACK
//
//        for (row in 0 until gridSize) {
//            for (col in 0 until gridSize) {
//                val cellText = "${row + 1}, ${col + 1}" // 예시로 행과 열 번호 표시
//                val x = col * cellWidth + cellWidth / 2
//                val y = row * cellHeight + cellHeight / 2
//                canvas.drawText(cellText, x.toFloat(), y.toFloat(), textPaint)
//            }
//        }
//    }
//
//    // ... (기존 코드)
//}

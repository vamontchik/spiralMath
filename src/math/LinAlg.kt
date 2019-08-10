package math

import ui.Grid
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow

fun getEquationVerticalUp(startX: Int, startY: Int, grid: Grid) {
    println("Calling getEquationVerticalUp with loc: " +
            "startX: $startX, startY: $startY")

    // populate pairs lists with "pairs" of points,
    // where .first == x and .second == y, so the
    // pairs will be (x, y) coordinates
    val points: MutableList<Double> = ArrayList()
    var currentY = startY
    while (currentY > 0) {
        val square = grid.findSquare(startX, currentY)
        val numberForSquare = square.labelCorner.toInt()
        points.add(numberForSquare.toDouble())
        currentY -= grid.squareLength
    }

    // set-up the matrices
    val matrixA: Array<DoubleArray> = Array(points.size) {
        val doubleRep = it.toDouble()
        doubleArrayOf(doubleRep.pow(2.0), doubleRep, 1.0)
    }
    val matrixB: Array<Double> = points.toTypedArray()

    println(Arrays.deepToString(matrixA))
    println(Arrays.deepToString(matrixB))

    // use linear least squares to find coefficients for
    // an equation of the form ax^2 + bx + c
    // TODO: make the linear algebra library that does this!
}
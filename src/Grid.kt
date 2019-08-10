import java.awt.Dimension
import java.awt.Graphics
import java.lang.Math.sqrt
import java.util.*
import javax.swing.JPanel
import kotlin.collections.ArrayList
import kotlin.math.pow

class Grid : JPanel() {
    private val listOfSquares: List<Square>
    private val squareLength: Int = 75
    private val numRowCol: Int = 15

    init {
        preferredSize = Dimension(numRowCol*squareLength, numRowCol*squareLength)
        val tempList: MutableList<Square> = ArrayList()
        for (i in 0 until numRowCol*squareLength step squareLength) {
            for (j in 0 until numRowCol*squareLength step squareLength) {
                tempList.add(Square(i, j, squareLength, squareLength))
            }
        }
        listOfSquares = tempList
    }

    private fun isPerfectSquare(i: Int): Boolean {
        val sqrtVal = sqrt(i.toDouble())
        val compareVal = sqrtVal.toInt().toDouble()
        val tolerance = 0.0001
        return ((sqrtVal - compareVal) < tolerance)
    }

    override fun paintComponent(g: Graphics?) {
        if (g == null) throw RuntimeException("Graphics object in Grid::paintComponent was null!")
        super.paintComponent(g)
        for (square in listOfSquares) {
            square.draw(g)
        }
    }

    fun spiralPath() {
        val values = Values(width / 2,
                            height / 2,
                            0,
                            0,
                            false,
                            0,
                            Direction.UP)

        for (i in 0 until listOfSquares.size) {
            setNumberAndBold(values.x, values.y, values.count.toString(), values.count)
            updateValues(values)
        }
    }

    private fun setNumberAndBold(x: Int, y: Int, setTo: String, count: Int) {
        val currSquare = findSquare(x, y)
        currSquare.labelCorner = setTo
        currSquare.isBold = isPerfectSquare(count)
    }

    private fun updateValues(values: Values) {
        // update x, y positions
        when (values.direction) {
            Direction.UP -> values.y -= squareLength
            Direction.LEFT -> values.x -= squareLength
            Direction.DOWN -> values.y += squareLength
            Direction.RIGHT -> values.x += squareLength
        }

        // update number to draw on grid
        ++values.count

        // update next direction we should move, or
        // else update how many steps left to walk
        if (values.leftToWalk == 0) {
            when (values.direction) {
                Direction.UP -> values.direction = Direction.LEFT
                Direction.LEFT -> values.direction = Direction.DOWN
                Direction.DOWN -> values.direction = Direction.RIGHT
                Direction.RIGHT -> values.direction = Direction.UP
            }

            // only change total length every two rotations
            if (values.timeToIncrease) {
                ++values.totalLength
                values.timeToIncrease = false
            } else {
                values.timeToIncrease = true
            }

            // update the next amount of steps needed to be taken
            values.leftToWalk = values.totalLength
        } else {
            --values.leftToWalk
        }
    }

    private fun findSquare(x: Int, y: Int): Square {
        return listOfSquares.find { it.contains(x,y) } ?:
               throw RuntimeException("listOfSquares could not find a square at x: $x, y: $y")
    }

    fun getEquationVerticalUp(x: Int, y: Int) {
        // populate pairs lists with "pairs" of points,
        // where .first == x and .second == y, so the
        // pairs will be (x, y) coordinates
        val points: MutableList<Double> = ArrayList()
        var currentY = y
        while (currentY > 0) {
            val square = findSquare(x, currentY)
            val numberForSquare = square.labelCorner.toInt()

            points.add(numberForSquare.toDouble())

            currentY -= squareLength
        }

        // set-up the matrices
        val A: Array<DoubleArray> = Array(points.size) {
            val doubleRep = it.toDouble()
            doubleArrayOf(doubleRep.pow(2.0), doubleRep, 1.0)
        }
        val b: Array<Double> = points.toTypedArray()

        println(Arrays.deepToString(A))
        println(Arrays.deepToString(b))

        // use linear least squares to find coefficients for
        // an equation of the form ax^2 + bx + c
        // TODO: make the linear algebra library that does this!
    }
}
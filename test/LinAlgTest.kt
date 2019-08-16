import math.*
import kotlin.math.abs
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class LinAlgTest {
    companion object {
        lateinit var basicBase: Matrix

        lateinit var addBase: Matrix
        lateinit var toAdd: Matrix
        lateinit var addResult: Matrix

        lateinit var subtractBase: Matrix
        lateinit var toSubtract: Matrix
        lateinit var subtractResult: Matrix

        lateinit var transposeBase: Matrix
        lateinit var transposeResult: Matrix
    }

    @BeforeTest
    fun setup() {
        basicBase = arrayOf(
            arrayOf(7.0, 4.0, 1.0),
            arrayOf(4.0, 4.0, 4.0),
            arrayOf(1.0, 4.0, 7.0)
        )

        //

        addBase = arrayOf(
            arrayOf(1.0, 2.0, 3.0),
            arrayOf(4.0, 5.0, 6.0),
            arrayOf(7.0, 8.0, 9.0)
        )

        toAdd = arrayOf(
            arrayOf(1.0, 1.0, 1.0),
            arrayOf(1.0, 1.0, 1.0),
            arrayOf(1.0, 1.0, 1.0)
        )

        addResult = Array(addBase.size) {
            Array(addBase[0].size) { it2 -> addBase[it][it2] + toAdd[it][it2] }
        }

        //

        subtractBase = addBase

        toSubtract = toAdd

        subtractResult = Array(subtractBase.size) {
            Array(subtractBase[0].size) { it2 -> subtractBase[it][it2] - toSubtract[it][it2] }
        }

        //

        transposeBase = arrayOf(
            arrayOf(1.0, 2.0, 3.0),
            arrayOf(4.0, 5.0, 6.0),
            arrayOf(7.0, 8.0, 9.0)
        )

        transposeResult = arrayOf(
            arrayOf(1.0, 4.0, 7.0),
            arrayOf(2.0, 5.0, 8.0),
            arrayOf(3.0, 6.0, 9.0)
        )
    }

    private fun doubleComparison(value: Double, target: Double): Boolean {
        val tolerance = 0.0001
        return (abs(value) - target < tolerance)
    }

    @Test
    fun rowAndWidthTest() {
        println("rowAndWidthTest...")

        for (row in 0 until getHeight(basicBase)) {
            val v: Vector = getRow(basicBase, row)

            for (col in 0 until getWidth(basicBase)) {
                val firstDouble: Double = v[col]
                val secondDouble: Double = basicBase[row][col]

                println("matrix at ($row, $col): $firstDouble, vector at $row: $secondDouble")

                assertTrue { doubleComparison(v[col], basicBase[row][col]) }
            }
        }
    }

    @Test
    fun colAndHeightTest() {
        println("colAndHeightTest...")

        for (col in 0 until getWidth(basicBase)) {
            val v: Vector = getCol(basicBase, col)

            for (row in 0 until getHeight(basicBase)) {
                val firstDouble: Double = v[row]
                val secondDouble: Double = basicBase[row][col]

                println("matrix at ($row, $col): $firstDouble, vector at $row: $secondDouble")

                assertTrue { doubleComparison(v[row], basicBase[row][col]) }
            }
        }
    }

    @Test
    fun matrixAddTest() {
        println("matrixAddTest...")
        val tempRes: Matrix = add(addBase, toAdd)

        for (row in 0 until getHeight(addResult)) {
            for (col in 0 until getWidth(addResult)) {
                val firstDouble: Double = addResult[row][col]
                val secondDouble: Double = tempRes[row][col]

                println("first matrix at ($row, $col): $firstDouble, second matrix at ($row, $col): $secondDouble")

                assertTrue { doubleComparison(firstDouble, secondDouble) }
            }
        }
    }

    @Test
    fun matrixSubtractTest() {
        println("matrixSubtractTest...")

        val tempRes: Matrix = subtract(subtractBase, toSubtract)

        for (row in 0 until getHeight(subtractResult)) {
            for (col in 0 until getWidth(subtractResult)) {
                val firstDouble: Double = subtractResult[row][col]
                val secondDouble: Double = tempRes[row][col]

                println("first matrix at ($row, $col): $firstDouble, second matrix at ($row, $col): $secondDouble")

                assertTrue { doubleComparison(firstDouble, secondDouble) }
            }
        }
    }

    @Test
    fun matrixTransposeTest() {
        println("matrixTransposeTest...")

        val tempRes: Matrix = transpose(transposeBase)

        for (row in 0 until getHeight(transposeResult)) {
            for (col in 0 until getWidth(transposeResult)) {
                val firstDouble: Double = transposeResult[row][col]
                val secondDouble: Double = tempRes[row][col]

                println("first matrix at ($row, $col): $firstDouble, second matrix at ($row, $col): $secondDouble")

                assertTrue { doubleComparison(firstDouble, secondDouble) }
            }
        }
    }

//    @Test
//    fun eigenTest() {
//        val m: Matrix = arrayOf(
//            arrayOf(7.0, 4.0, 1.0),
//            arrayOf(4.0, 4.0, 4.0),
//            arrayOf(1.0, 4.0, 7.0)
//        )
//
//        val (eigenvalues, eigenvectors) = eigen(basicBase)
//
//        println("eigenvalues: " + eigenvalues.contentDeepToString())
//        printMatrix("eigenvectors", eigenvectors)
//
//        assertTrue { abs(eigenvalues[0] - 12.0) < 0.0001 }
//        assertTrue { abs(eigenvalues[1] - 6.0) < 0.0001 }
//        assertTrue { abs(eigenvalues[2]) < 0.001 }
//    }
}
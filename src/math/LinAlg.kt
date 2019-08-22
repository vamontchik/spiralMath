package math

import kotlin.math.abs
import kotlin.math.pow
import kotlin.random.Random

typealias Matrix = Array<Array<Double>>
typealias Vector = Array<Double>

fun doubleComparison(value: Double, target: Double, precision: Double = 0.0001): Boolean {
    return (abs(value - target) < precision)
}

fun getRow(matrix: Matrix, index: Int): Vector {
    return matrix[index]
}

fun getCol(matrix: Matrix, index: Int): Vector {
    return matrix.map { it[index] }.toTypedArray()
}

/**
 * Width = the amount of values in one inner array, since we are looking row-major
 */
fun getWidth(matrix: Matrix): Int {
    return matrix[0].size
}

/**
 * Height = the amount of inner arrays, since we are looking row-major
 */
fun getHeight(matrix: Matrix): Int {
    return matrix.size
}

fun printMatrix(name: String, matrix: Matrix) {
    println("$name:")
    for (row in 0 until getHeight(matrix)) {
        println(getRow(matrix, row).contentDeepToString())
    }
}

fun transpose(matrix: Matrix): Matrix {
    return Array(getWidth(matrix)) {
        getCol(matrix, it)
    }
}

fun add(first: Matrix, second: Matrix): Matrix {
    return first.mapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, value -> value + second[rowIndex][colIndex] }.toTypedArray()
    }.toTypedArray()
}

fun subtract(first: Matrix, second: Matrix): Matrix {
    return first.mapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, value -> value - second[rowIndex][colIndex] }.toTypedArray()
    }.toTypedArray()
}

fun dot(first: Vector, second: Vector): Double {
    return first.mapIndexed { index, value -> value * second[index] }.sum()
}

fun multiply(first: Matrix, second: Matrix): Matrix {
    // A      = m x n
    // B      = n x p
    // result = m x p

    val result: Matrix = zeroMatrix(getWidth(second), getHeight(first))
    for (row in 0 until getHeight(first)) {
        for (col in 0 until getWidth(second)) {
            result[row][col] = dot(getRow(first, row), getCol(second, col))
        }
    }
    return result
}

// the only meaningful way to multiply a vector by
// a matrix is to multiply the vector by each column or
// row of the matrix. Whether or not it is by each column
// or by each row is determined by which goes first:
//
// matrix * vector == each row of matrix * vector
// vector * matrix == vector * each col of matrix

fun multiply(first: Matrix, second: Vector): Vector {
    // A      = m x n
    // B      = 1 x n
    // Do: A * B^T, or each row multiplied by vector
    // result = m x 1

    return first.map { row -> dot(row, second) }.toTypedArray()
}

fun multiply(first: Vector, second: Matrix): Vector {
    // A = 1 x m
    // B = m x n
    // Do: A * B, or vector multiplied by each column in matrix
    // result = 1 x n

    val result: Vector = zeroVector(getWidth(second))
    for (i in 0 until getWidth(second)) {
        result[i] = dot(first, getCol(second, i))
    }
    return result
}

// since dot(vector, vector) can be used for a vector * vector
// multiplication where the result reduces to a single value,
// use this method for the other way (ie. when vector * vector
// would turn into a matrix)

fun multiply(first: Vector, second: Vector): Matrix {
    // first: m x 1
    // second: n x 1
    // Do: first * second^T, or each value in each vector multiplied once together
    // result: m x n

    val result: Matrix = zeroMatrix(second.size, first.size)
    for (row in 0 until first.size) {
        for (col in 0 until second.size) {
            result[row][col] = first[row] * second[col]
        }
    }
    return result
}

fun norm(v: Vector, degree: Double): Double {
    return (v.fold(0.0) { acc, value -> acc.plus(abs(value).pow(degree)) }).pow(1 / degree)
}

fun zeroVector(size: Int): Vector {
    return Array(size) { 0.0 }
}

fun zeroMatrix(width: Int, height: Int): Matrix {
    return Array(height) {
        Array(width) { 0.0 }
    }
}

fun scalarMultiply(scalar: Double, matrix: Matrix): Matrix {
    return matrix.map { it.map { it.times(scalar) }.toTypedArray() }.toTypedArray()
}

//fun scalarDivide(scalar: Double, matrix: Matrix): Matrix {
//    return matrix.map { it.map { it.div(scalar) }.toTypedArray() }.toTypedArray()
//}

fun scalarDivide(scalar: Double, v: Vector): Vector {
    return v.map { value -> value / scalar }.toTypedArray()
}

//fun identity(amount: Int): Matrix {
//    val matrix: Matrix = zeroMatrix(amount, amount)
//    for (i in 0 until amount) {
//        matrix[i][i] = 1.0
//    }
//    return matrix
//}

//fun adjoint(matrix: Matrix): Matrix {
//    val cofactorMatrix: Matrix = zeroMatrix(getWidth(matrix), getHeight(matrix))
//
//    for (row in 0 until getWidth(matrix)) {
//        for (col in 0 until getHeight(matrix)) {
//            cofactorMatrix[row][col] = determinant(subMatrix(matrix, row, col))
//        }
//    }
//
//    return transpose(cofactorMatrix)
//}

fun subMatrix(matrix: Matrix, removeRow: Int, removeCol: Int): Matrix {
    val subMatrix: Matrix = zeroMatrix(getWidth(matrix) - 1, getHeight(matrix) - 1)

    var trueRow = 0
    var trueCol = 0
    for (row in 0 until getWidth(matrix)) {
        if (row == removeRow) continue
        for (col in 0 until getHeight(matrix)) {
            if (col == removeCol) continue
            subMatrix[trueRow][trueCol] = matrix[row][col]
            ++trueCol
        }
        trueCol = 0
        ++trueRow
    }

    return subMatrix
}

fun determinant(matrix: Matrix): Double {
    // base case
    if (getWidth(matrix) == 2) {
        return matrix[0][0]*matrix[1][1] - matrix[0][1]*matrix[1][0]
    }

    val topRow: Vector = getRow(matrix, 0)
    var accumulator = 0.0
    var negativeSign = false
    for (i in 0 until getWidth(matrix)) {
        if (negativeSign) {
            accumulator -= topRow[i] * determinant(subMatrix(matrix, 0, i))
            negativeSign = false
        } else {
            accumulator += topRow[i] * determinant(subMatrix(matrix, 0, i))
            negativeSign = true
        }
    }
    return accumulator
}

//fun inverse(matrix: Matrix): Matrix {
//    return scalarDivide(determinant(matrix), adjoint(matrix))
//}

fun raleighQuotientEigenvalue(matrix: Matrix, eigenvector: Vector): Double {
    val numerator: Double = dot(eigenvector, multiply(matrix, eigenvector))
    val denominator: Double = dot(eigenvector, eigenvector)
    return numerator / denominator
}

fun powerIteration(matrix: Matrix): Vector {
    var b: Vector = Array(getHeight(matrix)) { Random.nextDouble() }

    for (i in 0 until 50) {
        val numerator: Vector = multiply(matrix, b)
        val denominator: Double = norm(numerator, 2.0)
        b = scalarDivide(denominator, numerator)
    }

    return b
}

fun svd(A: Matrix): Triple<Matrix, Matrix, Matrix> {
    printMatrix("A", A)

    val At: Matrix = transpose(A)
    printMatrix("At", At)

    val product: Matrix = multiply(At, A)
    printMatrix("product", product)

    val (eigenvalues, eigenvectors) = eigen(product)
    println("eigenvalues: ${eigenvalues.contentDeepToString()}")
    printMatrix("eigenvectors", eigenvectors)

    // TODO: finish svd calculation...

    return Triple(A, A, A) // dummy for compiler
}

// Note: this method is fairly inaccurate, since
//       the currently implemented power iteration method
//       is not that stable, so don't expect many points of precision
fun eigen(matrix: Matrix): Pair<Vector, Matrix> {
    val amount: Int =
        if (getWidth(matrix) < getHeight(matrix)) {
            getWidth(matrix)
        } else {
            getHeight(matrix)
        }

    val eigenvalues: Vector = zeroVector(amount)
    val eigenvectors: Matrix = zeroMatrix(amount, amount)

    var currMatrix: Matrix = matrix
    for (i in 0 until amount) {
        eigenvectors[i] = fixForZeroesVector(powerIteration(currMatrix))
        eigenvalues[i] = fixForZero(raleighQuotientEigenvalue(currMatrix, eigenvectors[i]))
        currMatrix = fixForZeroesMatrix(deflation(currMatrix, eigenvectors[i], eigenvalues[i]))
    }

    return Pair(eigenvalues, eigenvectors)
}

fun deflation(currMatrix: Matrix, eigenvector: Vector, eigenvalue: Double): Matrix {
    val rightHandMatrix: Matrix = scalarMultiply(
        eigenvalue / norm(eigenvector, 2.0),
        multiply(eigenvector, eigenvector)
    )
    return subtract(currMatrix, rightHandMatrix)
}

//
// rounds away very small numbers, positive and negative, to 0.0
//

fun fixForZeroesMatrix(matrix: Matrix): Matrix {
    return matrix.map { row -> fixForZeroesVector(row) }.toTypedArray()
}

fun fixForZeroesVector(v: Vector): Vector {
    return v.map { value -> fixForZero(value) }.toTypedArray()
}

fun fixForZero(value: Double): Double {
    val threshold = 10E-10
    return if (abs(value) < threshold) 0.0 else value
}

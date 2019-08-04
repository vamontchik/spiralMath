import java.awt.Dimension
import java.awt.Graphics
import java.lang.RuntimeException
import javax.swing.JPanel

class Grid : JPanel() {
    private val listOfSquares: List<Square>
    private val squareLength: Int = 50

    init {
        preferredSize = Dimension(10*squareLength, 10*squareLength)

        val tempList: MutableList<Square> = ArrayList()
        for (i in 0 until 10*squareLength step squareLength) {
            for (j in 0 until 10*squareLength step squareLength) {
                tempList.add(Square(i, j, squareLength, squareLength))
            }
        }
        listOfSquares = tempList
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
            setNumber(values.x, values.y, values.count.toString())
            updateValues(values)
        }
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

    private fun setNumber(x: Int, y: Int, setTo: String) {
        val currSquare = findSquare(x, y)
        currSquare.labelCorner = setTo
    }

    private fun findSquare(x: Int, y: Int): Square {
        for (square in listOfSquares) {
            if (square.contains(x, y)) {
                return square
            }
        }
        throw RuntimeException("Could not find square based on x: $x y: $y")
    }
}
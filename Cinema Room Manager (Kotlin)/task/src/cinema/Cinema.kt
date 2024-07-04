package cinema

fun main() {
    // write your code here
    println("Enter the number of rows:")
    val numRows = readln().toInt()
    println("Enter the number of seats in each row:")
    val numSeats = readln().toInt()
    val cinema = Cinema(numRows, numSeats)

    var isTerminated = false
    while (!isTerminated) {
        println("""
            
            1. Show the seats
            2. Buy a ticket
            3. Statistics
            0. Exit
        """.trimIndent())
        val choice = readln().toInt()
        when (choice){
            0 -> isTerminated = true
            1 -> println(cinema)
            2 -> {
                var isBought = false
                while (!isBought){
                    try {
                        println("Enter a row number:")
                        val rowNumber = readln().toInt() - 1
                        println("Enter a seat number in that row:")
                        val seatNumber = readln().toInt() - 1
                        println("Ticket price: $${cinema.getSeatPrice(rowNumber, seatNumber)}")
                        cinema.buy(rowNumber, seatNumber)
                        isBought = true
                    }catch (e: IllegalArgumentException){
                        println(e.message)
                    }catch (e: Exception){
                        println("Wrong input!")
                    }
                }
            }
            3 -> cinema.getStats()
        }
    }
}

fun handleBuyTicket(){

}


class Cinema(val rows: Int, val seatsPerRow: Int) {
    val seats: List<List<Seat>>
    val totalNumberOfSeats: Int
    var numberOfPurchases: Int
    var currentIncome = 0
    var totalIncome = 0
    init {
        numberOfPurchases = 0
        totalNumberOfSeats = rows * seatsPerRow
        if (totalNumberOfSeats <= 60) {
            seats = populateSeats(rows, seatsPerRow, 10)
        } else {
            val numberOfExecutivesRows = rows / 2
            val numberOfNormalRows = rows - numberOfExecutivesRows
            seats = buildList<List<Seat>> {
                addAll(populateSeats(numberOfExecutivesRows, seatsPerRow, 10))
                addAll(populateSeats(numberOfNormalRows, seatsPerRow, 8))
            }
        }
    }
    fun populateSeats(rows: Int, seatsPerRow: Int, pricePerSeat: Int): List<List<Seat>> {
        val temp = mutableListOf<MutableList<Seat>>()
        for (i in 1..rows) {
            val row = mutableListOf<Seat>()
            for (i in 1..seatsPerRow) {
                row.add(Seat(pricePerSeat))
                totalIncome += pricePerSeat
            }
            temp.add(row)
        }
        return temp.toList()
    }
    fun profit(): Int{
        var profit = 0
        for (row in seats.indices){
            for (column in seats[row].indices){
                profit += seats[row][column].price
            }
        }
        return profit
    }
    fun buy(row: Int, column: Int){
        if (seats[row][column].status == SeatStatus.B) throw IllegalArgumentException("That ticket has already been purchased!")
        seats[row][column].status = SeatStatus.B
        numberOfPurchases++
        currentIncome += seats[row][column].price
    }
    override fun toString(): String{
        var string = "Cinema:\n  ${seats[0].indices.joinToString(" "){ (it.toInt() + 1).toString()}}\n"
        for (i in 1..seats.size){
            string += "$i "
            for (j in 1..seats[i-1].size){
                string += seats[i-1][j-1]
                string += if (j == seats[i-1].size) "\n" else " "
            }
        }
        return string
    }
    fun getSeatPrice(row: Int, column: Int): Int{
        return seats[row][column].price
    }
    fun getStats(){
        println("Number of purchased tickets: $numberOfPurchases")
        println("Percentage: ${"%.2f".format(100 * (numberOfPurchases.toFloat() / totalNumberOfSeats.toFloat()))}%")
        println("Current income: $$currentIncome")
        println("Total Income: $$totalIncome")
    }
}
class Seat(val price: Int){
    var status = SeatStatus.S
        set(value) {
            field = value
        }
    override fun toString(): String {
        return "${status.status}"
    }
}

enum class SeatStatus(val status: String){
    S("S"),
    B("B");
}
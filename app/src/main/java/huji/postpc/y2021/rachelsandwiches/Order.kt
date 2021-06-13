package huji.postpc.y2021.rachelsandwiches

data class Order(
    var id: String = "",
    var customerName: String = "",
    var pickles: Int = 0,
    var hummus: Boolean = false,
    var tahini: Boolean = false,
    var comment: String = "",
    var status: String = ""
)

package com.sonalisulgadle.expensetracker.ai

import com.sonalisulgadle.expensetracker.util.Constants.DEFAULT_EMOJI

object EmojiResolver {

    fun resolve(description: String): String {
        val input = description.trim().lowercase()
        return findMatch(input) ?: DEFAULT_EMOJI
    }

    private fun findMatch(input: String): String? {
        for ((keywords, emoji) in EMOJI_RULES) {
            if (keywords.any { input.contains(it) }) return emoji
        }
        return null
    }

    private val EMOJI_RULES: List<Pair<List<String>, String>> = listOf(

        // ---- Food & Drink — specific items first ----
        listOf(
            "burger", "burgers", "mcdonald", "lotteria",
            "wendy", "five guys", "whopper"
        ) to "🍔",
        listOf("pizza", "domino", "papa john") to "🍕",
        listOf(
            "sushi", "sashimi", "ramen",
            "udon", "tonkatsu"
        ) to "🍜",
        listOf(
            "coffee", "cafe", "starbucks",
            "latte", "espresso", "americano",
            "cappuccino", "hollys", "ediya"
        ) to "☕",
        listOf(
            "chicken", "fried chicken", "kfc",
            "bbq", "barbeque"
        ) to "🍗",
        listOf("sandwich", "subway", "wrap") to "🥪",
        listOf("salad", "bowl", "grain bowl") to "🥗",
        listOf(
            "beer", "pub", "bar",
            "soju", "makgeolli", "alcohol"
        ) to "🍺",
        listOf("wine", "winery") to "🍷",
        listOf(
            "cake", "bakery", "bread",
            "paris baguette", "tous les jours"
        ) to "🍰",
        listOf(
            "ice cream", "dessert",
            "baskin", "bingsu"
        ) to "🍦",
        listOf("taco", "burrito", "mexican") to "🌮",
        listOf("curry", "indian") to "🍛",
        listOf("breakfast", "brunch", "pancake", "waffle") to "🥞",
        listOf(
            "lunch", "dinner", "meal",
            "restaurant", "eat", "food"
        ) to "🍽️",
        listOf(
            "snack", "chips", "convenience",
            "gs25", "cu ", "7-eleven", "emart24"
        ) to "🛒",
        listOf(
            "water", "juice", "drink",
            "beverage", "smoothie"
        ) to "🥤",

        // ---- Transport ----
        listOf(
            "uber", "ola", "taxi", "cab",
            "lyft", "kakao taxi", "tada"
        ) to "🚕",
        listOf(
            "metro", "subway", "t-money",
            "transit card", "bus", "train",
            "korail", "ktx", "transport"
        ) to "🚇",
        listOf(
            "petrol", "gas", "fuel",
            "diesel", "charging", "ev"
        ) to "⛽",
        listOf("parking") to "🅿️",
        listOf(
            "flight", "airline", "airfare",
            "airport", "plane"
        ) to "✈️",
        listOf("car", "vehicle", "auto") to "🚗",

        // ---- Shopping ----
        listOf(
            "grocery", "mart", "supermarket",
            "emart", "lotte mart", "costco",
            "homeplus"
        ) to "🛒",
        listOf(
            "clothes", "clothing", "shirt",
            "pants", "dress", "zara", "h&m",
            "uniqlo", "nike", "adidas"
        ) to "👕",
        listOf("shoes", "sneakers", "boots") to "👟",
        listOf(
            "bag", "handbag", "backpack",
            "luggage"
        ) to "👜",
        listOf(
            "electronics", "phone", "laptop",
            "tablet", "apple", "samsung",
            "gadget"
        ) to "📱",
        listOf(
            "book", "textbook", "novel",
            "kindle", "yes24", "aladin"
        ) to "📚",
        listOf(
            "skincare", "makeup", "cosmetic",
            "olive young", "innisfree",
            "beauty", "sephora"
        ) to "💄",
        listOf(
            "furniture", "ikea", "sofa",
            "chair", "desk"
        ) to "🪑",

        // ---- Health ----
        listOf(
            "gym", "fitness", "workout",
            "weightlifting"
        ) to "💪",
        listOf("yoga", "pilates", "stretching") to "🧘",
        listOf(
            "hospital", "clinic", "doctor",
            "dentist", "medical"
        ) to "🏥",
        listOf(
            "pharmacy", "medicine",
            "prescription", "drug"
        ) to "💊",
        listOf("massage", "spa", "sauna") to "💆",

        // ---- Entertainment ----
        listOf(
            "netflix", "disney", "hulu",
            "coupang play", "watcha",
            "streaming"
        ) to "🎬",
        listOf(
            "movie", "cinema", "cgv",
            "megabox", "lotte cinema"
        ) to "🎥",
        listOf(
            "game", "steam", "playstation",
            "xbox", "nintendo"
        ) to "🎮",
        listOf(
            "concert", "festival",
            "ticket", "performance"
        ) to "🎵",
        listOf(
            "sport", "stadium", "match",
            "baseball", "football",
            "basketball"
        ) to "🏟️",
        listOf("karaoke", "norebang") to "🎤",
        listOf(
            "museum", "exhibition",
            "gallery", "art"
        ) to "🖼️",

        // ---- Bills & Utilities ----
        listOf(
            "electric", "electricity",
            "power bill", "kepco"
        ) to "⚡",
        listOf("water bill", "waterworks") to "💧",
        listOf("gas bill", "heating") to "🔥",
        listOf(
            "internet", "wifi", "broadband",
            "kt ", "sk telecom", "lg uplus"
        ) to "📶",
        listOf(
            "phone bill", "mobile",
            "telecom", "data plan"
        ) to "📱",
        listOf("insurance") to "🛡️",
        listOf(
            "rent", "mortgage",
            "housing", "maintenance fee"
        ) to "🏠",
        listOf(
            "subscription", "membership",
            "annual fee"
        ) to "🔄",
        listOf(
            "bill", "utility",
            "invoice", "payment"
        ) to "🧾",

        // ---- Travel ----
        listOf(
            "hotel", "accommodation",
            "airbnb", "hostel", "motel"
        ) to "🏨",
        listOf(
            "tour", "travel", "trip",
            "vacation", "holiday"
        ) to "🗺️",
        listOf("visa", "passport") to "📋",

        // ---- Education ----
        listOf(
            "tuition", "course", "class",
            "lecture", "school",
            "university", "college"
        ) to "🎓",
        listOf(
            "stationery", "pen", "notebook",
            "pencil", "supplies"
        ) to "✏️",
        listOf(
            "online course", "udemy",
            "coursera", "inflearn"
        ) to "💻",

        // ---- Other ----
        listOf(
            "salary", "income",
            "transfer", "deposit"
        ) to "💰",
        listOf("atm", "withdrawal", "cash") to "🏧",
        listOf("charity", "donation") to "❤️",
        listOf("gift", "present") to "🎁",
        listOf(
            "pet", "dog", "cat",
            "vet", "grooming"
        ) to "🐾",
        listOf(
            "plant", "flower",
            "garden"
        ) to "🌱"
    )
}
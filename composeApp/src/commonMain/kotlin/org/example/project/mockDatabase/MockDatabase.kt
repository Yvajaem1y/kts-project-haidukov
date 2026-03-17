package org.example.project.mockDatabase


object MockDatabase {
    private val list = mutableListOf<User>(
        User(
            username = "john123",
            userFirstName = "John",
            userLastName = "Doe",
            isUserOnline = true,
            lastUserTimeOnline = null,
            userAvatar = "https://img.freepik.com/free-photo/happy-dog-portrait-looking-front_1194-589243.jpg?semt=ais_hybrid&w=740"
        ),
        User(
            username = "jane456",
            userFirstName = "Jane",
            userLastName = "Smith",
            isUserOnline = false,
            lastUserTimeOnline = "02.03.2026 13.45"
        ),
        User(
            username = "bob789",
            userFirstName = "Bob",
            userLastName = "Johnson",
            isUserOnline = false,
            lastUserTimeOnline = "01.03.2026 09.15",
            userAvatar = "https://avatars.mds.yandex.net/get-yapic/43473/SPhAYCSefS11Ny3Ncy9e1hUOxM-1/orig"
        ),
        User(
            username = "alice_w",
            userFirstName = "Alice",
            userLastName = "Wong",
            isUserOnline = true,
            lastUserTimeOnline = null,
            userAvatar = "https://images.steamusercontent.com/ugc/56957406872066358/6315C4B0270C2DD671E0841EC4564AC5620DF36F/?imw=512&amp;imh=384&amp;ima=fit&amp;impolicy=Letterbox&amp;imcolor=%23000000&amp;letterbox=true"
        ),
        User(
            username = "mike_brown",
            userFirstName = "Mike",
            userLastName = "Brown",
            isUserOnline = false,
            lastUserTimeOnline = "28.02.2026 18.20"
        )
    )

    fun getList(): List<User> {
        return list
    }
}
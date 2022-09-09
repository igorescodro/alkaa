plugins {
    id("quality.detekt")
    id("quality.ktlint")
}

tasks.getByName("check") {
    setDependsOn(
        listOf(
            tasks.getByName("ktlint"),
            tasks.getByName("detekt")
        )
    )
}

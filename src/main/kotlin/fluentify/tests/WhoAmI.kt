package fluentify.tests

data class WhoAmI(
    val name: String,
    val surname: String,
    val twitterAccount: String,
    val workingFor: String,
    val yearsOfKotlinExperience: Int
)

val whoami = WhoAmI(
    "Daniele",
    "Fongo",
    "@DanieleFongo",
    "XPeppers, a Claranet company",
    1
)
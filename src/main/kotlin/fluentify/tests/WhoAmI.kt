package fluentify.tests

data class WhoAmI(
    val name: String,
    val surname: String,
    val workingFor: String,
    val yearsOfKotlinExperience: Int
)

val whoami = WhoAmI(
    "Daniele",
    "Fongo",
    "XPeppers, a Claranet company",
    1
)
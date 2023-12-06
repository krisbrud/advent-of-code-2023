plugins {
    kotlin("jvm") version "1.9.21"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}

dependencies {
//    implementation("junit:junit:5.8.1")
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

import org.gradle.api.JavaVersion

object AndroidConst {
    const val NAMESPACE = "ru.gribbirg.todoapp"
    const val COMPILE_SKD = 34
    const val MIN_SKD = 26
    val COMPILE_JDK_VERSION = JavaVersion.VERSION_1_8
    const val KOTLIN_JVM_TARGET = "1.8"

    const val COMPOSE_COMPILER_VERSION = "1.5.1"
}
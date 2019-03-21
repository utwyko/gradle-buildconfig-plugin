package com.github.gmazzo.gradle.plugins.tasks

import com.github.gmazzo.gradle.plugins.BuildConfigField
import com.github.gmazzo.gradle.plugins.BuildConfigLanguage
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

open class BuildConfigTask : DefaultTask() {

    @Input
    var className = "BuildConfig"

    @Input
    var packageName = ""

    @Input
    lateinit var fields: Collection<BuildConfigField>

    @Input
    var language = BuildConfigLanguage.JAVA

    @Input
    var addGeneratedAnnotation = false

    @OutputDirectory
    lateinit var outputDir: File

    @get:Internal
    internal val distinctFields
        get() = fields
            .map { it.name to it }
            .toMap()
            .values

    init {
        onlyIf { fields.isNotEmpty() }
    }

    @TaskAction
    fun generateBuildConfigFile() {
        when (language) {
            BuildConfigLanguage.JAVA -> BuildConfigJavaGenerator
            BuildConfigLanguage.KOTLIN -> BuildConfigKotlinGenerator
        }(this)
    }

}

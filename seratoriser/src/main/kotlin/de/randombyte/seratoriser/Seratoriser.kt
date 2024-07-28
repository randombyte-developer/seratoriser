@file:OptIn(ExperimentalPathApi::class)

package de.randombyte.seratoriser

import java.nio.file.Paths
import kotlin.io.path.*
import kotlin.system.exitProcess

object Seratoriser {
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size < 3) {
            println("Usage: seratoriser <rootCrate> <inputFolder> <outputFolder>")
            exitProcess(1)
        }

        val rootCrate = args[0]
        val inputFolder = args[1]
        val outputFolder = args[2]
        val input = Paths.get(inputFolder)
        val output = Paths.get(outputFolder)

        if (!input.exists() || !input.isDirectory()) {
            println("$inputFolder is not a folder!")
            exitProcess(1)
        }

        if (!output.exists()) {
            output.createDirectory()
        } else if (!output.isDirectory()) {
            println("$outputFolder is not a folder!")
            exitProcess(1)
        }

        input
            .walk()
            .filter { it.isRegularFile() && it.extension == "ogg" }
            .groupBy { it.parent.name }
            .forEach { (playlistFolder, files) ->
                // `%%` is the hierarchical separator between crate names in Serato.
                // The child crate is saved as a file `parent%%child.crate`.
                // If a folder named like this naming scheme is imported to Serato (e.g. drag and drop)
                // the correct parent and child hierarchy is generated.
                val seratoCrateName = "$rootCrate%%$playlistFolder"
                val folder = output.resolve(seratoCrateName)
                if (!folder.exists()) folder.createDirectory()

                files.forEach { file -> file.copyTo(folder.resolve(file.name)) }
            }
    }
}
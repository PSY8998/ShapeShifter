import app.shapeshifter.configureSpotless
import org.gradle.api.Plugin
import org.gradle.api.Project

class RootConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        // Set project root dir
        setProjectRootDir(rootDir)

        configureSpotless()
    }
}

package app.shapeshifter.data.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.shapeshifter.core.base.inject.ApplicationScope
import me.tatarka.inject.annotations.Provides
import java.io.File

actual interface SqlDelightDatabasePlatformDriverComponent {
  @Provides
  @ApplicationScope
  fun provideDriverFactory(): SqlDriver = JdbcSqliteDriver(
    url = "jdbc:sqlite:${databaseFile.absolutePath}",
  ).also { db ->
      ShapeShifterDatabase.Schema.create(db)
    db.execute(null, "PRAGMA foreign_keys=ON", 0)
  }
}

private val databaseFile: File
  get() = File(appDir.also { if (!it.exists()) it.mkdirs() }, "shapeshifter.db")

private val appDir: File
  get() {
    val os = System.getProperty("os.name").lowercase()
    return when {
      os.contains("win") -> {
        File(System.getenv("AppData"), "shapeshifter/db")
      }

      os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
        File(System.getProperty("user.home"), ".shapeshifter")
      }

      os.contains("mac") -> {
        File(System.getProperty("user.home"), "Library/Application Support/shapeshifter")
      }

      else -> error("Unsupported operating system")
    }
  }

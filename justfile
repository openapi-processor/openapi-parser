default:
  @just --list --unsorted

# update gradle wrapper
wrapper version="8.13":
    ./gradlew wrapper --gradle-version={{version}}

# run gradle dependencyInsight for dependency and configuration
insight dependency configuration="compileClasspath":
    ./gradlew -q dependencyInsight --dependency {{dependency}} --configuration {{configuration}}

# assemble with warning-mode all
compatibility:
    ./gradlew assemble --warning-mode all

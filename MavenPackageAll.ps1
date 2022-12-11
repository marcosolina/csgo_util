$toInstall = @(
    "IxigoParent"
    "IxigoLibrary"
    "IxigoDemManagerContract"
    "IxigoDiscordBotContract"
    "IxigoEventDispatcherContract"
    "IxigoPlayersManagerContract"
    "IxigoRconApiContract"
)

$toPackage = @(
    "IxigoConfigServer"
    "IxigoDemManager"
    "IxigoDiscordBot"
    "IxigoDiscovery"
    "IxigoEventDispatcher"
    "IxigoPlayersManager"
    "IxigoProxy"
    "IxigoRconApi"
    "IxigoServerHelper"
)

foreach ($project in $toInstall) {
    mvn clean install -f "$project\pom.xml"
}

foreach ($project in $toPackage) {
    mvn clean package -f "$project\pom.xml"
}
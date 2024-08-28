$toInstall = @(
    "IxigoEventDispatcherContract"
    "IxigoDemManagerContract"
    "IxigoDiscordBotContract"
    "IxigoPlayersManagerContract"
    "IxigoRconApiContract"
    "IxigoServerHelperContract"
    "IxigoNotificationContract"
    "IxigoLibrary"
    "IxigoParent"
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
    "IxigoNotification"
)

foreach ($project in $toInstall) {
    mvn clean install -f "$project\pom.xml"
}

foreach ($project in $toPackage) {
    mvn clean package -f "$project\pom.xml"
}
$jdbcDriverName = "mysql-connector-j*.jar"
$jdbcDriverPath = "$Env:SWE4_HOME/lib/db/$jdbcDriverName"
if (!(Test-Path "tomcat-config/$jdbcDriverPath")) {
  Copy-Item $jdbcDriverPath tomcat-config
}

$tomcatImageExists = docker images --format "{{.Repository}}:{{.Tag}}" | Select-String -Pattern "tomcat-swe4" -Quiet
if (-not $tomcatImageExists) {
  Write-Host -ForegroundColor Yellow "creating Tomcat image ..."
  docker build --tag tomcat-swe4 .
}

$networkExists = docker network ls --format "{{.Name}}" | Select-String -Pattern "phonebook-net" -Quiet

if (-not $networkExists) {
  Write-Host -ForegroundColor Yellow "creating network 'phonebook-net' ..."
  docker network create phonebook-net | Out-Null
}

$tomcatContainerRuns = $(docker ps -f "name=^/tomcat$" --format "{{.Names}}").length -gt 0
$tomcatContainerExists = $tomcatContainerRuns -or ($(docker ps -a -f "name=^/tomcat$" --format "{{.Names}}").length -gt 0)

if ($tomcatContainerRuns) {
  Write-Host -ForegroundColor Yellow "Tomcat is already running."
}
elseif ($tomcatContainerExists) {
  Write-Host -ForegroundColor Yellow "starting existing Tomcat container ..."
  docker start tomcat | Out-Null
}
else {
  Write-Host -ForegroundColor Yellow "creating and starting Tomcat container ..."
  docker run --name tomcat -d -p 8080:8080 --network phonebook-net tomcat-swe4 2>&1 | Out-Null
}

$sqlContainerRuns = $(docker ps -f "name=^/mysql$" --format "{{.Names}}").length -gt 0
$sqlContainerExists = $sqlContainerRuns -or ($(docker ps -a -f "name=^/mysql$" --format "{{.Names}}").length -gt 0)

if ($sqlContainerRuns) {
  Write-Host -ForegroundColor Yellow "MySQL server is already running."
}
elseif ($sqlContainerExists) {
  Write-Host -ForegroundColor Yellow "starting existing MySQL container ..."
  docker start mysql | Out-Null
}
else {
  Write-Host -ForegroundColor Yellow "creating and starting MySQL container ..."
  docker run --name mysql -d -p 3306:3306 -e "MYSQL_ALLOW_EMPTY_PASSWORD=1" --network phonebook-net mysql:8 2>&1 | Out-Null
}
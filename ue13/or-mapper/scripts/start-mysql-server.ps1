$containerRuns = $(docker ps -f "name=^/mysql$" --format "{{.Names}}").length -gt 0
$containerExists = $containerRuns -or ($(docker ps -a -f "name=^/mysql$" --format "{{.Names}}").length -gt 0)

if ($containerRuns) {
	Write-Host -ForegroundColor Yellow "MySQL server is alread running."
	exit 0 
} 
elseif ($containerExists) {
	Write-Host -ForegroundColor Yellow "starting existing MySQL container ..."
  docker start mysql | Out-Null
} 
else {
	Write-Host -ForegroundColor Yellow "creating and starting MySQL container ..."
	docker run --name mysql -d -p 3306:3306 -e "MYSQL_ALLOW_EMPTY_PASSWORD=1" mysql:8 2>&1 | Out-Null
}



$mysqlDriver = Join-Path $Env:SWE4_HOME "lib/db/mysql-connector-j-8.0.32.jar"

if (-not (Test-Path $mysqlDriver)) {
  Write-Error "Error: MySql JDBC driver $mysqlDriver not found."
  Exit 1
}

java -cp "$mysqlDriver;../bin/classes" swe4.simpledal.PhoneBookApplication

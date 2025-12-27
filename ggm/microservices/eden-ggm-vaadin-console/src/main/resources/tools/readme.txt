Cum se ruleaza:
 - pe PROD instalata:
          pg_dump -U postgres -s --port 5444 -f "E:\ORSYP\Workspace Gaudit\g_updater_tools\lib\old.sql" --no-owner -n schema2012abc gaudit
 - pe baza de date de DEV:
          pg_dump -f "E:\ORSYP\Workspace Gaudit\g_updater_tools\lib\new.sql" -U postgres -s -n schema2011demo --no-owner  gaudit_abo
 
in new.sql se inlocuieste numele schemei cu schema2012abc
  
java -jar apgdiff-2.4.jar --ignore-function-whitespace --ignore-start-with old.sql new.sql > upgrade.sql

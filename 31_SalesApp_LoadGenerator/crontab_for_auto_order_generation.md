Edit linux user's crontab entries to schedule automatic execution of any script.

[cass@metalgear ~]$ `crontab -e`

add following entries to crontab file :

```

# Example of job definition:
# .---------------- minute (0 - 59)
# |  .------------- hour (0 - 23)
# |  |  .---------- day of month (1 - 31)
# |  |  |  .------- month (1 - 12) OR jan,feb,mar,apr ...
# |  |  |  |  .---- day of week (0 - 6) (Sunday=0 or 7) OR sun,mon,tue,...,sat
# |  |  |  |  |
# *  *  *  *  * <user-name> <command to be executed>

# mi hh dd mm d /script_to_run

# following runs every 4 minutes
*/4 * *  *  * /home/cass/SalesApp_JavaCode/SalesApp_GenerateOrders.sh


```

---

linux user can list user's crontab file entries :

[cass@metalgear ~]$ `crontab -l`


select * from pg_stat_activity; where datname = 'resultadofinal';--Sabes cuantas conexiones estan abiertas
update prestacion set horent_pre = replace(horent_pre,'mm','00');
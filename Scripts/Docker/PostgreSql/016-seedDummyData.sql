\c demfiles;

INSERT INTO public.match_stats (match_filename, match_date, mapname, match_id) VALUES ('C:\Temp\demfiles\2020-08-24\auto0-20200824-203025-1694882996-cs_assault-IXI-GO__Monday_Nights.dem', '2020-08-24 20:30:25', 'cs_assault', 1);
INSERT INTO public.player_round_stats (username, steamid, round, team, clutchchance, clutchsuccess, survived, moneyspent, equipmentvalue, mvp, match_id) VALUES ('Sir Pollo Loco', '76561199045993244', 1, 2, 3, false, false, 400, 200, false, 1);
INSERT INTO public.player_stats (username, steamid, match_id, score) VALUES ('Sir Pollo Loco', '76561199045993244', 1, 3);
INSERT INTO public.round_events (eventtype, eventtime, steamid, round, match_id) VALUES ('bomb_planted', 202.328125, '76561199057392192', 1, 1);
INSERT INTO public.round_hit_events (eventtime, steamid, round, weapon, victimsteamid, hitgroup, damagehealth, damagearmour, blindtime, match_id) VALUES (125.796875, '76561198072484969', 1, 'weapon_usp_silencer', '76561197974132960', 5, 28, 0, NULL, 1);
INSERT INTO public.round_kill_events (eventtime, steamid, assister, flashassister, killerflashed, round, weapon, headshot, victimsteamid, isfirstkill, istradekill, istradedeath, match_id) VALUES (126.328125, '76561197974132960', NULL, NULL, 'false', 1, 'deagle', true, '76561198072484969', true, false, false, 1);
INSERT INTO public.round_shot_events (eventtype, eventtime, steamid, round, weapon, match_id) VALUES ('weapon_fire', 114.296875, '76561199045993244', 1, 'weapon_molotov', 1);
INSERT INTO public.round_stats (roundnumber, winnerside, reasonendround, match_id) VALUES (1, 3, 8, 1);
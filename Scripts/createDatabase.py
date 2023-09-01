import psycopg2
import json
import re
import datetime

# Load the data
with open('file.json') as f:
    data = json.load(f)

# Establish a connection to the database
conn = psycopg2.connect(
    dbname="csgo",
    user="maustin",
    password="ixicocsgo",
    host="localhost",
    port="5432"
)

def extract_mapname_date(filename):
    match = re.search(r'auto0-(\d{14})-(\d+)-([\w\d_]+)-', filename)
    if match:
        date_str = match.group(1)
        map_name = match.group(3)

        # Convert date_str to timestamp format
        date = datetime.strptime(date_str, "%Y%m%d%H%M%S")

        return map_name, date
    else:
        return None, None

# Create a cursor object
cur = conn.cursor()

for match, stats in data.items():
    # Insert into MATCH_STATS
    map_name, date = extract_mapname_date(stats['mapStats']['fileName'])
    cur.execute(
        "INSERT INTO MATCH_STATS (match_date, mapName, match_fileName) VALUES (%s, %s, %s) RETURNING match_id;",
        (date, map_name, stats['mapStats']['fileName'])
    )
    match_id = cur.fetchone()[0]  # Get the id of the inserted row
    
    # Insert into PLAYER_ROUND_STATS
    for player_round_stat in stats['allPlayerRoundStats']:
        cur.execute(
            "INSERT INTO PLAYER_ROUND_STATS (userName, steamID, round, team, clutchChance, clutchSuccess, survived, moneySpent, equipmentValue, mvp, match_id) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);",
            (player_round_stat['userName'], player_round_stat['steamID'], player_round_stat['round'], player_round_stat['team'], player_round_stat['clutchChance'], player_round_stat['clutchSuccess'], player_round_stat['survived'], player_round_stat['moneySpent'], player_round_stat['equipmentValue'], player_round_stat['mvp'], match_id)
        )
        
    # Insert into PLAYER_STATS
    for player_stat in stats['allPlayerStats']:
        cur.execute(
            "INSERT INTO PLAYER_STATS (userName, steamID, match_id, score) VALUES (%s, %s, %s, %s);",
            (player_stat['userName'], player_stat['steamID'], match_id, player_stat.get('score'))
        )

    # Insert into ROUND_STATS
    for round_stat in stats['allRoundStats']:
        cur.execute(
            "INSERT INTO ROUND_STATS (roundNumber, winnerSide, reasonEndRound, match_id) VALUES (%s, %s, %s, %s);",
            (round_stat['roundNumber'], round_stat['winnerSide'], round_stat['reasonEndRound'], match_id)
        )

    # Insert into ROUND_EVENTS
    for round_event in stats['allRoundEvents']:
        cur.execute(
            "INSERT INTO ROUND_EVENTS (eventType, eventtime, steamID, round, match_id) VALUES (%s, %s, %s, %s, %s);",
            (round_event['eventType'], round_event['time'], round_event['steamID'], round_event['round'], match_id)
        )

    # Insert into ROUND_KILL_EVENTS
    for kill_event in stats['allRoundKillEvents']:
        cur.execute(
            "INSERT INTO ROUND_KILL_EVENTS (eventtime, steamID, assister, flashAssister, killerFlashed, round, weapon, headshot, victimSteamId, isFirstKill, isTradeKill, isTradeDeath, match_id) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);",
            (kill_event['time'], kill_event['steamID'], kill_event.get('assister'), kill_event.get('flashAssister'), kill_event.get('killerFlashed'), kill_event['round'], kill_event['weapon'], kill_event['headshot'], kill_event.get('victimSteamId'), kill_event['isFirstKill'], kill_event['isTradeKill'], kill_event['isTradeDeath'], match_id)
        )

    # Insert into ROUND_SHOT_EVENTS
    for shot_event in stats['allRoundShotEvents']:
        cur.execute(
            "INSERT INTO ROUND_SHOT_EVENTS (eventType, eventtime, steamID, round, weapon, match_id) VALUES (%s, %s, %s, %s, %s, %s);",
            (shot_event['eventType'], shot_event['time'], shot_event['steamID'], shot_event['round'], shot_event['weapon'], match_id)
        )

    # Insert into ROUND_HIT_EVENTS
    for hit_event in stats['allRoundHitEvents']:
        cur.execute(
            "INSERT INTO ROUND_HIT_EVENTS (eventtime, steamID, round, weapon, victimSteamId, hitGroup, damageHealth, damageArmour, blindTime, match_id) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s);",
            (hit_event['time'], hit_event['steamID'], hit_event['round'], hit_event['weapon'], hit_event.get('victimSteamId'), hit_event['hitGroup'], hit_event['damageHealth'], hit_event['damageArmour'], hit_event.get('blindTime'), match_id)
        )


    
# Commit the changes
conn.commit()

# Close the connection
cur.close()
conn.close()

import {
    parseEvent,
    parseTicks,
    listGameEvents,
    parseGrenades,
    parseHeader,
    parsePlayerInfo,
  } from "@laihoe/demoparser2";
  
  const pathToDemo =
    "C:\\temp\\auto-20231009-1930-de_overpass-IXI-GO__Monday_Nights.dem";



// 1vX
const X = 4;


function find_if_1vx(deaths: string | any[], round_idx: number, round_ends: { [x: string]: { winner: number; }; }, tickData: any[], X: number){
    for (let i = 0; i < deaths.length; i++){
        if (deaths[i].total_rounds_played == round_idx){
            
            let tickData_slice = tickData.filter((t: { tick: any; }) => t.tick == deaths[i].tick)
            let ctAlive = tickData_slice.filter((t: { team_name: string; is_alive: boolean; }) => t.team_name == "CT" && t.is_alive == true)
            let tAlive = tickData_slice.filter((t: { team_name: string; is_alive: boolean; }) => t.team_name == "TERRORIST" && t.is_alive == true)
            // 3 = CT
            if (ctAlive.length == 1 && tAlive.length == X && round_ends[round_idx].winner == 3){
                return ctAlive[0].name
            }
            // 2 = T
            if (tAlive.length == 1 && ctAlive.length == X && round_ends[round_idx].winner == 2){
                return tAlive[0].name
            }
        }
    }
}

let deaths = parseEvent(pathToDemo, "player_death", [], ["total_rounds_played"])
let wantedTicks = deaths.map((x: { tick: any; }) => x.tick)
let round_ends = parseEvent(pathToDemo, "round_end")
let tickData = parseTicks(pathToDemo, ["is_alive", "team_name", "team_rounds_total"], wantedTicks)
let maxRound = Math.max(...deaths.map((x: { total_rounds_played: any; }) => x.total_rounds_played))

for (let i = 0; i <= maxRound; i++){
    let res = find_if_1vx(deaths, i, round_ends, tickData, X);
    if (res != undefined){
        console.log("Round", i , res, "clutched a 1 v", X);
    }
}